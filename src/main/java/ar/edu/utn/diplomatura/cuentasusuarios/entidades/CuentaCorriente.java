package ar.edu.utn.diplomatura.cuentasusuarios.entidades;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

import static ar.edu.utn.diplomatura.cuentasusuarios.Constants.TIPO_CUENTA_CUENTA_CORRIENTE;

/* Una cuenta corriente es una cuenta bancaria que
   permite operar en descubierto hasta un límite específico.
 */
@Entity
public class CuentaCorriente extends Cuenta {

    //  @Column(name = "limite_descubierto", nullable = false, precision = 19, scale = 2)
    private BigDecimal limiteDescubierto;

    /** Constructor vacio: JPA lo necesita para reconstruir el objeto. */
    public CuentaCorriente() {
    }

    public CuentaCorriente(String numeroCuenta, BigDecimal saldo, String moneda, Usuario usuario, BigDecimal limiteDescubierto) {
        super(numeroCuenta, saldo, moneda, usuario);
        this.limiteDescubierto = limiteDescubierto;
    }

    // Getters y setters: se usan para armar el JSON de las respuestas.
    public BigDecimal getLimiteDescubierto() {
        return limiteDescubierto;
    }

    public void setLimiteDescubierto(BigDecimal limiteDescubierto) {
        this.limiteDescubierto = limiteDescubierto;
    }

    // Implementación obligatoria de métodos (Sobreescritura)
    @Override
    public String getTipoCuenta() { return TIPO_CUENTA_CUENTA_CORRIENTE; }

    @Override
    public BigDecimal obtenerSaldoDisponible() {
        return getSaldo().add(limiteDescubierto);
    }

}
