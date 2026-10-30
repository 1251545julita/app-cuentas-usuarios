package ar.edu.utn.diplomatura.cuentasusuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

@Schema(description = "Datos para abrir una cuenta en el banco.")
public record CuentaRequest(
        @Schema(example = "Caja de Ahorro") @NotNull String tipo,
        @Schema(example = "ARS", description = "Moneda de la cuenta de ahorro.") String moneda,
        @Schema(example = "1") @NotNull Long usuarioId,
        @Schema(example = "10000.00") @DecimalMin("0.00") @Digits(integer = 17, fraction = 2) BigDecimal saldoInicial,
        @Schema(example = "50000.00", description = "Para Cuenta Corriente") @DecimalMin("0.00") @Digits(integer = 17, fraction = 2) BigDecimal limiteDescubierto,
        @Schema(example = "40.00", description = "Para Caja de Ahorro") @DecimalMin("0.00") @Digits(integer = 5, fraction = 2) BigDecimal porcentajeTasaInteresAnual) {
}
