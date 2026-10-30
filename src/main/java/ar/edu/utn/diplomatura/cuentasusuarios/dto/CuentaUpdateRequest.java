package ar.edu.utn.diplomatura.cuentasusuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Campos modificables de una cuenta.")
public record CuentaUpdateRequest(
        @Schema(example = "40.00", description = "Solamente CA") BigDecimal porcentajeTasaInteresAnual,
        @Schema(example = "80000.00", description = "Solamente CC") BigDecimal limiteDescubierto) {
}
