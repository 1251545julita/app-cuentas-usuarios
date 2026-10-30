package ar.edu.utn.diplomatura.cuentasusuarios.repositorios;

import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // SELECT * FROM usuarios WHERE UPPER(apellido) LIKE UPPER('%?%')
    List<Usuario> findByApellidoContainingIgnoreCase(String apellido);

    boolean existsByEmail(String email);
}
