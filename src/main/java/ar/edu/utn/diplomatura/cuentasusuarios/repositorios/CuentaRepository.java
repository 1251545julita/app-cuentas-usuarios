package ar.edu.utn.diplomatura.cuentasusuarios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Cuenta;

import java.util.List;

/**
 * Acceso a la base de datos.
 *
 * Es una interfaz vacia a proposito: al extender JpaRepository, Spring genera
 * la implementacion solo y ya vienen hechos findAll(), findById(), save(),
 * deleteById(), existsById() y count().
 *
 * Los genericos son <Entidad, TipoDelId>.
 */
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    boolean existsByNumeroCuenta(String numeroCuenta);

    // SELECT * FROM cuentas WHERE UPPER(numeroCuenta) LIKE UPPER('%?%')
    List<Cuenta> findByNumeroCuentaContainingIgnoreCase(String numeroCuenta);

    List<Cuenta> findByUsuarioId(Long usuarioId);
}
