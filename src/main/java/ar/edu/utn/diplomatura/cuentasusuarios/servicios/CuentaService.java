package ar.edu.utn.diplomatura.cuentasusuarios.servicios;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import ar.edu.utn.diplomatura.cuentasusuarios.dto.CuentaRequest;
import ar.edu.utn.diplomatura.cuentasusuarios.dto.CuentaResponse;
import ar.edu.utn.diplomatura.cuentasusuarios.dto.CuentaUpdateRequest;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.CajaAhorro;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.CuentaCorriente;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Usuario;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.*;
import org.springframework.stereotype.Service;

import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Cuenta;
import ar.edu.utn.diplomatura.cuentasusuarios.repositorios.CuentaRepository;

import static ar.edu.utn.diplomatura.cuentasusuarios.Constants.*;

/**
 * Reglas del negocio. Está entre el controlador y el repositorio.
 *
 * Cuando algo no se puede hacer (el cliente no existe, el importe es invalido)
 * estos metodos devuelven null.
 */
@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioService usuarioService;

    /** Inyeccion por constructor: Spring busca esos dos beans en el contenedor y los pasa como argumentos. **/
    public CuentaService(CuentaRepository cuentaRepository, UsuarioService usuarioService) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioService = usuarioService;
    }

    public List<Cuenta> listarTodos() {
        return cuentaRepository.findAll();
    }

    /** Devuelve la cuenta, o null si ese id no existe. */
    public Optional<Cuenta> buscarPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    public Cuenta obtenerOFallar(Long id){
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException(id));
    }

    public List<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuentaContainingIgnoreCase(numeroCuenta);
    }

    /** Crea una cuenta. */
    public CuentaResponse crear(CuentaRequest request) {

        Usuario unUsuario = usuarioService.obtenerOFallar(request.usuarioId());
        String tipoCuenta = request.tipo();
        BigDecimal limiteDescubierto = request.limiteDescubierto(); //Objects.requireNonNullElse(request.limiteDescubierto(), BigDecimal.ZERO);
        BigDecimal porcentajeTasaInteresAnual = request.porcentajeTasaInteresAnual(); //Objects.requireNonNullElse(request.porcentajeTasaInteresAnual(), BigDecimal.ZERO);

        validarCamposSegunTipoCuenta(tipoCuenta, limiteDescubierto, porcentajeTasaInteresAnual);

        String numeroCuenta = String.format("%012d", ThreadLocalRandom.current().nextLong(1_000_000_000_000L));
        BigDecimal saldoInicial = BigDecimal.valueOf(0.0);
        String moneda = request.moneda() != null ? request.moneda() : TIPO_MONEDA_PESOS;

        Cuenta unaCuenta = switch (tipoCuenta) {
            case TIPO_CUENTA_CAJA_AHORRO ->
                    new CajaAhorro(numeroCuenta, saldoInicial, moneda, unUsuario, porcentajeTasaInteresAnual);
            case TIPO_CUENTA_CUENTA_CORRIENTE ->
                    new CuentaCorriente(numeroCuenta, saldoInicial, moneda, unUsuario, limiteDescubierto);
            default -> throw new TipoCuentaInvalidosException(tipoCuenta);
        };

        return CuentaResponse.desdeEntidad(cuentaRepository.save(unaCuenta));
    }

    private void validarCamposSegunTipoCuenta(String tipo, BigDecimal limiteDescubierto, BigDecimal porcentajeTasaInteresAnual) {
        if (TIPO_CUENTA_CAJA_AHORRO.equals(tipo) && limiteDescubierto != null) {
            throw new CamposInvalidosException(CUENTA_CORRIENTE_CAMPO_EXCLUSIVO, TIPO_CUENTA_CUENTA_CORRIENTE);
        }
        if (TIPO_CUENTA_CUENTA_CORRIENTE.equals(tipo) && porcentajeTasaInteresAnual != null) {
            throw new CamposInvalidosException(CAJA_AHORRO_CAMPO_EXCLUSIVO, TIPO_CUENTA_CAJA_AHORRO);
        }
    }

    /** Actualiza porcentajeTasaInteresAnual(Solo para CA) o limiteDescubierto(Solo para CC). Devuelve null si el id no existe. */
    public CuentaResponse actualizar(Long id, CuentaUpdateRequest datosNuevos) {
        Cuenta cuentaExistente = buscarPorId(id)
                .orElseThrow(() -> new CuentaNoEncontradaException(id));

        if (cuentaExistente instanceof CuentaCorriente corriente) {
            if (datosNuevos.porcentajeTasaInteresAnual() != null) {
                throw new CamposInvalidosException(CAJA_AHORRO_CAMPO_EXCLUSIVO, TIPO_CUENTA_CAJA_AHORRO);
            }
            if (datosNuevos.limiteDescubierto() != null) {
                corriente.setLimiteDescubierto(datosNuevos.limiteDescubierto());
            }
        } else if (cuentaExistente instanceof CajaAhorro caja) {
            if (datosNuevos.limiteDescubierto() != null) {
                throw new CamposInvalidosException(CUENTA_CORRIENTE_CAMPO_EXCLUSIVO, TIPO_CUENTA_CUENTA_CORRIENTE);
            }
            if (datosNuevos.porcentajeTasaInteresAnual() != null) {
                caja.setPorcentajeTasaInteresAnual(datosNuevos.porcentajeTasaInteresAnual());
            }
        }

        return CuentaResponse.desdeEntidad(cuentaRepository.save(cuentaExistente));
    }

    /** Elimina una cuenta. Si el id no existe, no hace nada. */
    public boolean eliminar(Long id) {
        if (!cuentaRepository.existsById(id)) {
            return false;
        }
        cuentaRepository.deleteById((id));
        return true;
    }

    public boolean desactivar(Long id){
        Cuenta unaCuenta = buscarPorId(id).orElse(null);
        if (unaCuenta == null) {
            return false;
        }
        unaCuenta.setHabilitada(false);
        cuentaRepository.save(unaCuenta);
        return true;
    }

    /** Suma un monto al saldo de una cuenta existente.
     * Devuelve null si la cuenta no existe o si el monto no es positivo. */
    public Cuenta acreditarSaldo (Long id, BigDecimal unSaldo) {
        Cuenta cuentaExistente = buscarPorId(id)
                .orElseThrow(() -> new CuentaNoEncontradaException(id));

        if (unSaldo == null || unSaldo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SaldoInvalidoException(unSaldo);
        }

        cuentaExistente.setSaldo(cuentaExistente.getSaldo().add(unSaldo));
        return cuentaRepository.save(cuentaExistente);
    }
}
