package ar.edu.utn.diplomatura.cuentasusuarios.config;

import java.math.BigDecimal;
import java.util.List;

import ar.edu.utn.diplomatura.cuentasusuarios.entidades.CajaAhorro;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.CuentaCorriente;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Usuario;
import ar.edu.utn.diplomatura.cuentasusuarios.repositorios.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Cuenta;
import ar.edu.utn.diplomatura.cuentasusuarios.repositorios.CuentaRepository;

import static ar.edu.utn.diplomatura.cuentasusuarios.Constants.*;

/**
 * Carga cuentas y usuarios de ejemplo al arrancar.
 *
 * Hace falta porque la base H2 vive en memoria: al apagar la aplicacion se
 * pierde todo, asi que cada arranque empieza con estos cinco.
 */
@Component
@Order(2)
public class CargaDatosIniciales implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;

    public CargaDatosIniciales(UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        if (cuentaRepository.count() > 0) {
            return;
        }

        // saveAll hace un INSERT por cada elemento de la lista.
        Usuario usuario01 = new Usuario("Ana", "Queen", "27301234567", "ana.queen@mail.com");
        Usuario usuario02 = new Usuario("Bruno", "Diaz", "20289876543", "bruno.diaz@mail.com");
        Usuario usuario03 = new Usuario("Carla", "Conte", "27354112233", "carla.conte@mail.com");
        Usuario usuario04 = new Usuario("Diego", "Gomez", "20221098765", "diego.gomez@mail.com");
        Usuario usuario05 = new Usuario("Elena", "Suarez", "23412233445", "elena.suarez@mail.com");
        Usuario usuario06 = new Usuario("Federico", "Luna", "2033445566", "federico.luna@mail.com");

        usuarioRepository.saveAll(List.of(usuario01, usuario02, usuario03, usuario04, usuario05, usuario06));

        //Cuenta(String numeroCuenta, BigDecimal saldo, String moneda, Usuario usuario), BigDecimal limiteDescubierto, String tipoCuenta)
        cuentaRepository.saveAll(List.of(
                new CuentaCorriente("000000000042", BigDecimal.valueOf(152300.50), TIPO_MONEDA_PESOS, usuario01,BigDecimal.valueOf(200000.00)),
                new CuentaCorriente("000000078521", BigDecimal.valueOf(8400.00), TIPO_MONEDA_PESOS, usuario02,BigDecimal.valueOf(50000.00)),
                new CajaAhorro("000006543210", BigDecimal.valueOf(0.00), TIPO_MONEDA_PESOS, usuario03, BigDecimal.valueOf(15.00)),
                new CajaAhorro("045678912345", BigDecimal.valueOf(1250000.00), TIPO_MONEDA_PESOS, usuario04, BigDecimal.valueOf(19)),
                new CajaAhorro("789456123078", BigDecimal.valueOf(76500.75), TIPO_MONEDA_PESOS, usuario05, BigDecimal.valueOf(15))
        ));

        Cuenta cajaAhorroInactiva = new CajaAhorro("123456789012",  BigDecimal.valueOf(500.00), TIPO_MONEDA_DOLARES, usuario06,  BigDecimal.valueOf(19.00));
        cajaAhorroInactiva.setHabilitada(false);
        cuentaRepository.save(cajaAhorroInactiva);

        System.out.println("=== " + cuentaRepository.count() + " cuentas cargadas en MySql ===");
    }
}
