package ar.edu.utn.diplomatura.cuentasusuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar un usuario")
public record UsuarioRequest(
        @Schema(example = "Lucía") @NotBlank @Size(max = 80) String nombre,
        @Schema(example = "Fernández") @NotBlank @Size(max = 80) String apellido,
        @Schema(example = "lucia.fernandez@mail.com") @NotBlank @Email @Size(max = 120) String email) {
}
