package ar.edu.utn.diplomatura.cuentasusuarios.controladores;

import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Usuario;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.CamposInvalidosException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.DniInvalidoException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.EmailDuplicadoException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.UsuarioNoEncontradoException;
import ar.edu.utn.diplomatura.cuentasusuarios.servicios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Alta y consulta de usuarios titulares de cuentas")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /usuarios              -> todos
     * GET /usuarios?apellido=xxx -> filtra por apellido
     *
     * @RequestParam es lo que va despues del "?" en la URL.
     */
    @Operation(summary = "Lista todos los usuarios",
            description = "Devuelve la lista completa. Con el parametro 'apellido' filtra por "
                    + "coincidencia parcial, sin distinguir mayusculas.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios (puede venir vacia)")
    @GetMapping
    public List<Usuario> listar(@Parameter(description = "Parte del apellido a buscar", example = "Queen")
                                @RequestParam(required = false) String apellido) {
        if (apellido == null || apellido.isBlank()) {
            return usuarioService.listarTodos();
        }
        return usuarioService.buscarPorApellido(apellido);
    }

    /**
     * GET /usuarios/1
     *
     * @PathVariable toma el "1" de la URL.
     */
    @Operation(summary = "Obtener un usuario por id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado.")
    @ApiResponse(responseCode = "404", description = "El usuario no encontrado.")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.obtenerOFallar(id)); //200
        } catch (UsuarioNoEncontradoException e){
            System.out.println("Get/usuarios/"+ id + " " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * POST /usuarios
     *
     * @RequestBody convierte el JSON del cuerpo en un objeto Usuario.
     */
    @Operation(summary = "Registrar un usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos al intentar crear un usuario, por ejemplo, dni invalido")
    @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@RequestBody Usuario unUsuario) {
        try {
            Usuario usuarioCreado = usuarioService.crear(unUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (DniInvalidoException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (EmailDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    /** PUT /usuarios/1
     *
     * @RequestBody convierte el JSON del cuerpo en un objeto Usuario con datos actualizados.
     */
    @Operation(summary = "Actualiza un usuario existente",
            description = "Actualiza nombre, apellido, email y cuentas asociadas. Devuelve null si el id no existe.")
    @ApiResponse(responseCode = "200", description = "Cuenta actualizada.")
    @ApiResponse(responseCode = "404", description = "Cuenta indicada no encontrada.")
    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario datosNuevos) {
        return usuarioService.actualizar(id, datosNuevos);
    }

    /** DELETE /usuarios/6
     *
     * @PathVariable envia por path param el id del usuario existente.
     * @RequestParam envia por path param el modo de cómo eliminar ese usuario existente. Puede ser lógico o físico.
     */
    @Operation(summary = "Borra logica o fisicamente un usuario existente",
            description = "Deshabilita un usuario existente en modo logico. En modo fisico, borra fisicamente de la Base de Datos.")
    @ApiResponse(responseCode = "204", description = "Usuario deshabilitado.")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado o ya se borró.")
    @ApiResponse(responseCode = "400", description = "El modo debe ser 'logico' o 'fisico' en los parametros.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam(defaultValue = "logico")String modo) {

        if (!modo.equals("fisico") && !modo.equals("logico")){
            return ResponseEntity.badRequest().build(); // 400
        }

        boolean seDioDeBaja = modo.equals("logico") ? usuarioService.desactivar(id) : usuarioService.eliminar(id);

        if (!seDioDeBaja){
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.noContent().build(); // 204
    }
}
