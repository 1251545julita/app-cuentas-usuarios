package ar.edu.utn.diplomatura.cuentasusuarios.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SaldoUsdResponse(
        Long cuentaId,
        String monedaOriginal,
        BigDecimal saldoOriginal,
        BigDecimal cotizacionVenta,
        BigDecimal saldoUsd,
        String fechaCotizacion) {
}
