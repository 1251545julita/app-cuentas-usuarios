package ar.edu.utn.diplomatura.cuentasusuarios.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        LocalDateTime fechaAlta) {
}
