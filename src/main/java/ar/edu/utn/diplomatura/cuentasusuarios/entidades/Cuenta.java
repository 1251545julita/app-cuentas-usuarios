package ar.edu.utn.diplomatura.cuentasusuarios.entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Una cuenta del banco.
 *
 * @Entity le dice a JPA que esta clase se guarda en una tabla:
 * la clase es la tabla, cada atributo es una columna y cada objeto es una fila.
 */
@Entity
@Table(name = "cuentas")
public abstract class Cuenta implements Operable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String numeroCuenta;
    private BigDecimal saldo = BigDecimal.ZERO; // Sino con el constructor vacío y saldo null, saldo.add(monto) en ingresarMonto fallaría con NullPointerException.
    private String moneda; // ARS, USD
    private LocalDateTime fechaDeApertura;
    private boolean habilitada; // Si alguna vez el valor es null, isHabilitada() no falla con NullPointerException al convertirlo.

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Constructor vacio: JPA lo necesita para reconstruir el objeto. */
    public Cuenta() {
    }

    public Cuenta(String numeroCuenta, BigDecimal saldo, String moneda, Usuario usuario) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.moneda = moneda;
        this.fechaDeApertura = LocalDateTime.now();
        this.habilitada = true;
        this.usuario = usuario;
    }

    // Getters y setters: se usan para armar el JSON de las respuestas.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCuenta() { return numeroCuenta;}

    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public LocalDateTime getFechaDeApertura() { return fechaDeApertura; }

    public void setFechaDeApertura(LocalDateTime fechaDeApertura) { this.fechaDeApertura = fechaDeApertura; }

    public BigDecimal getSaldo() { return saldo; }

    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public String getMoneda() { return moneda; }

    public void setMoneda(String moneda) { this.moneda = moneda; }

    public boolean isHabilitada() { return habilitada; }

    public void setHabilitada(boolean habilitada) { this.habilitada = habilitada; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    //Additional methods

    public abstract String getTipoCuenta();

    @Override
    public abstract BigDecimal obtenerSaldoDisponible();

    @Override
    public void ingresarMonto(BigDecimal monto) {
        validarMonto(monto);
        saldo = saldo.add(monto);
    }

    @Override
    public void retirarMonto(BigDecimal monto) {
        validarMonto(monto);
//        if (monto.compareTo(obtenerSaldoDisponible()) > 0) {
//            //throw new SaldoInsuficienteException(numeroCuenta, obtenerSaldoDisponible(), monto);
//        }
        saldo = saldo.subtract(monto);
    }

    private void validarMonto(BigDecimal monto) {
//        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
//            //throw new MontoInvalidoException("El monto debe ser mayor a cero");
//            yield;
//        }
    }
}
