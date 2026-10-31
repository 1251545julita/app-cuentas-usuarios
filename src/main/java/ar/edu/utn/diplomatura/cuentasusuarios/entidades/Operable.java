package ar.edu.utn.diplomatura.cuentasusuarios.entidades;

import java.math.BigDecimal;

// Producto financiero sobre el que se puede operar.
public interface Operable {

    void ingresarSaldo(BigDecimal unSaldo);

    void retirarSaldo(BigDecimal unSaldo);

    BigDecimal obtenerSaldoDisponible();
}
