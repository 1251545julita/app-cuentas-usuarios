package ar.edu.utn.diplomatura.cuentasusuarios.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Respuesta de la API externa de cotizaciones (DolarAPI). Sólo se mapean los campos que usamos.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CotizacionDolar(
        String nombre,
        BigDecimal compra,
        BigDecimal venta,
        String fechaActualizacion) {
}
