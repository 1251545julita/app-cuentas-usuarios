package ar.edu.utn.diplomatura.cuentasusuarios.controladores;

import java.net.URI;
import java.util.List;
import java.util.Map;

import ar.edu.utn.diplomatura.cuentasusuarios.dto.CuentaRequest;
import ar.edu.utn.diplomatura.cuentasusuarios.dto.CuentaResponse;
import ar.edu.utn.diplomatura.cuentasusuarios.dto.CuentaUpdateRequest;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.CamposInvalidosException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.CuentaNoEncontradaException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.TipoCuentaInvalidosException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.UsuarioNoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Cuenta;
import ar.edu.utn.diplomatura.cuentasusuarios.servicios.CuentaService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Puerta de entrada HTTP. Recibe la peticion, llama al service y devuelve el
 * resultado. No lleva reglas de negocio.
 *
 * @RestController convierte a JSON lo que devuelven los metodos.
 * @RequestMapping("/clientes") es el prefijo comun de todas las rutas.
 */
@RestController
@RequestMapping("/cuentas")
@Tag(name = "Cuentas", description = "Alta, baja, consulta y operaciones sobre las cuentas del banco.")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    /**
     * GET /cuentas -> todos
     * GET /cuentas?numeroCuenta=xxx -> filtra por número de cuenta
     *
     * @RequestParam es lo que va despues del "?" en la URL.
     */
    @Operation(summary = "Lista todos las cuentas",
            description = "Devuelve la lista completa. Con el parametro 'numeroCuenta' filtra por "
                    + "coincidencia parcial, sin distinguir mayusculas.")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas (puede venir vacia)")
    @GetMapping
    public List<Cuenta> listar(@Parameter(description = "Lista por el número de cuenta a buscar", example = "123456789012")
                                    @RequestParam(required = false) String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return cuentaService.listarTodos();
        }
        return cuentaService.buscarPorNumeroCuenta(numeroCuenta);
    }

    /**
     * GET /cuentas/1
     *
     * @PathVariable toma el "1" de la URL.
     */
    @Operation(summary = "Obtener una cuenta por su id",
            description = "Devuelve los datos de la cuenta que coincide con el id indicado. "
                    + "Si no existe una cuenta con ese id, responde 404.")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @ApiResponse(responseCode = "404", description = "La cuenta no existe")
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cuentaService.obtenerOFallar(id)); //200
        } catch (CuentaNoEncontradaException e){
            System.out.println("Get/cuentas/"+ id + " " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /cuentas
     *
     * @RequestBody convierte el JSON del cuerpo en un objeto Cuenta.
     */
    @Operation(summary = "Crear una cuenta",
            description = "Abre una Caja de Ahorro o una Cuenta Corriente para un usuario existente. "
                    + "El número de cuenta lo genera el sistema.")
    @ApiResponse(responseCode = "201", description = "Cuenta creada.")
    @ApiResponse(responseCode = "400", description = "Tipo de cuenta inválido o campos que no corresponden al tipo.")
    @ApiResponse(responseCode = "404", description = "El usuario indicado no existe.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@RequestBody CuentaRequest request) {
        try {
            CuentaResponse creada = cuentaService.crear(request);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(creada.id()).toUri();
            return ResponseEntity.created(location).body(creada);
        } catch (TipoCuentaInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (CamposInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** PUT /cuentas/1 */
    @Operation(summary = "Actualiza una cuenta existente",
            description = "Actualiza el porcentajeTasaInteresAnual de una Caja de Ahorro o el limiteDescuerto de una Cuenta Corriente.")
    @ApiResponse(responseCode = "200", description = "Cuenta actualizada.")
    @ApiResponse(responseCode = "404", description = "Cuenta indicada no encontrada.")
    @ApiResponse(responseCode = "400", description = "Campos que no corresponden al tipo de cuenta indicado.")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CuentaUpdateRequest datosNuevos) {
        try {
            return ResponseEntity.ok(cuentaService.actualizar(id, datosNuevos));
        } catch (CuentaNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (CamposInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** DELETE /cuentas/12 */
    @Operation(summary = "Borra logica o fisicamente una cuenta existente",
            description = "Deshabilita una cuenta existente en modo logico. En modo fisico, borra fisicamente una cuenta existente.")
    @ApiResponse(responseCode = "204", description = "Cuenta deshabilitada.")
    @ApiResponse(responseCode = "404", description = "Cuenta indicada no encontrada, ya se borró.")
    @ApiResponse(responseCode = "400", description = "El modo debe ser 'logico' o 'fisico' en los parametros.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam(defaultValue = "logico")String modo) {

        if (!modo.equals("fisico") && !modo.equals("logico")){
            return ResponseEntity.badRequest().build(); // 400
        }

        boolean seDioDeBaja = modo.equals("logico") ? cuentaService.desactivar(id) : cuentaService.eliminar(id);

        if (!seDioDeBaja){
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.noContent().build(); // 204
    }

//    /** POST /cuentas/1/acreditaciones?saldo=33000 */
//    @PostMapping("/{id}/acreditaciones")
//    public Cuenta acreditar(@PathVariable Long id, @RequestParam BigDecimal importe) {
//        return cuentaService.acreditarSaldo(id, importe);
//    }
}
