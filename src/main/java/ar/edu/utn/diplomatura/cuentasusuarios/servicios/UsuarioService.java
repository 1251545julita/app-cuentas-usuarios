package ar.edu.utn.diplomatura.cuentasusuarios.servicios;

import ar.edu.utn.diplomatura.cuentasusuarios.componentes.ValidadorDeDni;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Cuenta;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Usuario;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.CamposInvalidosException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.DniInvalidoException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.EmailDuplicadoException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.UsuarioNoEncontradoException;
import ar.edu.utn.diplomatura.cuentasusuarios.repositorios.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static ar.edu.utn.diplomatura.cuentasusuarios.Constants.CAJA_AHORRO_CAMPO_EXCLUSIVO;
import static ar.edu.utn.diplomatura.cuentasusuarios.Constants.TIPO_CUENTA_CAJA_AHORRO;

/**
 * Reglas del negocio. Está entre el controlador y el repositorio.
 *
 * Cuando algo no se puede hacer (el usuario no existe)
 * estos metodos devuelven null.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ValidadorDeDni validadorDeDni;

    // Inyeccion por constructor: Spring busca esos dos beans en el contenedor y los pasa como argumentos.
    public UsuarioService(UsuarioRepository usuarioRepository, ValidadorDeDni validadorDeDni) {
        this.usuarioRepository = usuarioRepository;
        this.validadorDeDni = validadorDeDni;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /** Devuelve el usuario encontrado, o null si ese id no existe. */
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario obtenerOFallar(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    public List<Usuario> buscarPorApellido(String apellido) {
        return usuarioRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    /** Crea un usuario. El CUIT se guarda siempre sin guiones. */
    public Usuario crear(Usuario unUsuario) {
        if (!validadorDeDni.esValido(unUsuario.getDni())) {
            throw new DniInvalidoException(unUsuario.getDni());
        }

        if (usuarioRepository.existsByEmail(unUsuario.getEmail())) {
            throw new EmailDuplicadoException(unUsuario.getEmail());
        }

        unUsuario.setDni(validadorDeDni.normalizar(unUsuario.getDni()));
        unUsuario.setId(null); // El id lo pone la base: si viene uno en el JSON, lo ignoramos.

        return usuarioRepository.save(unUsuario);
    }


    /** Actualiza nombre, apellido, email y cuentas asociadas. Devuelve null si el id no existe. */
    public Usuario actualizar(Long id, Usuario datosNuevos) {
        Usuario usuarioExistente = buscarPorId(id).orElse(null);
        if (usuarioExistente == null) {
            return null;
        }

        usuarioExistente.setNombre(datosNuevos.getNombre());
        usuarioExistente.setApellido(datosNuevos.getApellido());
        usuarioExistente.setEmail(datosNuevos.getEmail());
        usuarioExistente.setCuentas(datosNuevos.getCuentas());

        return usuarioRepository.save(usuarioExistente);
    }

    /** Elimina un usuario. Si el id no existe, no hace nada. */
    public boolean eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById((id));
        return true;
    }

    public boolean desactivar(Long id){
        Usuario unUsuario = buscarPorId(id).orElse(null);
        if (unUsuario == null) {
            return false;
        }
        unUsuario.setHabilitado(false);
        usuarioRepository.save(unUsuario);
        return true;
    }

}
