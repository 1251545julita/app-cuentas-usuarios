package ar.edu.utn.diplomatura.cuentasusuarios.entidades;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static ar.edu.utn.diplomatura.cuentasusuarios.Constants.TIPO_CUENTA_CAJA_AHORRO;

/* Una caja de ahorro es un producto operable que
   devenga intereses.
 */
@Entity
public class CajaAhorro extends Cuenta {
    // @Column(name = "tasa_interes_anual", nullable = false, precision = 7, scale = 2)
    private BigDecimal porcentajeTasaInteresAnual;

    /** Constructor vacio: JPA lo necesita para reconstruir el objeto. */
    public CajaAhorro() {
    }

    public CajaAhorro(String numeroCuenta, BigDecimal saldo, String moneda, Usuario usuario, BigDecimal porcentajeTasaInteresAnual) {
        super(numeroCuenta, saldo, moneda, usuario);
        this.porcentajeTasaInteresAnual = porcentajeTasaInteresAnual;
    }

    // Getters y setters: se usan para armar el JSON de las respuestas.
    public BigDecimal getPorcentajeTasaInteresAnual() { return porcentajeTasaInteresAnual; }

    public void setPorcentajeTasaInteresAnual(BigDecimal porcentajeTasaInteresAnual) { this.porcentajeTasaInteresAnual = porcentajeTasaInteresAnual; }

    // Implementación obligatoria de métodos (Sobreescritura)
    @Override
    public String getTipoCuenta() { return TIPO_CUENTA_CAJA_AHORRO; }

    @Override
    public BigDecimal obtenerSaldoDisponible() {
        return getSaldo();
    }

    /** Interés mensual simple: saldo * (tasa anual / 12 / 100). */
    public BigDecimal calcularInteresMensual() {
        return getSaldo()
                .multiply(porcentajeTasaInteresAnual)
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);
    }

}