package ar.edu.utn.diplomatura.cuentasusuarios.excepciones;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException(String numeroCuenta, BigDecimal saldoDisponible, BigDecimal saldoSolicitado) {
        super("Saldo insuficiente en la cuenta " + numeroCuenta + ": disponible " + saldoDisponible + ", solicitado " + saldoSolicitado);
    }
}
