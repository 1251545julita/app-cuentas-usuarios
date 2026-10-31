package ar.edu.utn.diplomatura.cuentasusuarios.entidades;

import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.SaldoInsuficienteException;
import ar.edu.utn.diplomatura.cuentasusuarios.excepciones.SaldoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static ar.edu.utn.diplomatura.cuentasusuarios.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Lógica de negocio que vive en las entidades (no necesita Spring ni Mockito).
 */
class CuentaTest {

    private Usuario titular;
    private CajaAhorro caja;
    private CuentaCorriente corriente;

    @BeforeEach
    void setUp() {
        titular = new Usuario("Ana", "Gomez", "20123456", "ana@mail.com");
        caja = new CajaAhorro("000000000001", new BigDecimal("100.00"), TIPO_MONEDA_PESOS, titular, new BigDecimal("12.00"));
        corriente = new CuentaCorriente("000000000002", BigDecimal.ZERO, TIPO_MONEDA_PESOS, titular, new BigDecimal("1000.00"));
    }

    private static void assertMonto(String esperado, BigDecimal real) {
        assertEquals(0, new BigDecimal(esperado).compareTo(real),
                "Esperado " + esperado + " pero fue " + real);
    }

    // ---------- Constructor ----------

    @Test
    @DisplayName("Una cuenta nueva queda habilitada, con fecha de apertura y con su titular")
    void cuentaNuevaQuedaHabilitada() {
        assertTrue(caja.isHabilitada());
        assertNotNull(caja.getFechaDeApertura());
        assertSame(titular, caja.getUsuario());
    }

    @Test
    void cadaSubclaseDevuelveSuTipo() {
        assertEquals(TIPO_CUENTA_CAJA_AHORRO, caja.getTipoCuenta());
        assertEquals(TIPO_CUENTA_CUENTA_CORRIENTE, corriente.getTipoCuenta());
    }

    // ---------- ingresarSaldo ----------

    @Test
    void ingresarSaldoSumaElMonto() {
        caja.ingresarSaldo(new BigDecimal("50.00"));
        assertMonto("150.00", caja.getSaldo());
    }

    @Test
    void ingresarSaldoCeroLanzaSaldoInvalido() {
        assertThrows(SaldoInvalidoException.class, () -> caja.ingresarSaldo(BigDecimal.ZERO));
        assertMonto("100.00", caja.getSaldo());
    }

    @Test
    void ingresarSaldoNegativoLanzaSaldoInvalido() {
        assertThrows(SaldoInvalidoException.class, () -> caja.ingresarSaldo(new BigDecimal("-5")));
    }

    @Test
    void ingresarSaldoNullLanzaSaldoInvalido() {
        assertThrows(SaldoInvalidoException.class, () -> caja.ingresarSaldo(null));
    }

    // ---------- retirarSaldo ----------

    @Test
    void retirarSaldoRestaElMonto() {
        caja.retirarSaldo(new BigDecimal("40.00"));
        assertMonto("60.00", caja.getSaldo());
    }

    @Test
    @DisplayName("Caja de ahorro: puede retirar exactamente todo su saldo")
    void cajaPuedeRetirarTodoElSaldo() {
        caja.retirarSaldo(new BigDecimal("100.00"));
        assertMonto("0", caja.getSaldo());
    }

    @Test
    @DisplayName("Caja de ahorro: no puede quedar en descubierto")
    void cajaNoPuedeQuedarEnDescubierto() {
        SaldoInsuficienteException ex = assertThrows(SaldoInsuficienteException.class,
                () -> caja.retirarSaldo(new BigDecimal("100.01")));
        assertTrue(ex.getMessage().contains("000000000001"));
        assertMonto("100.00", caja.getSaldo()); // el saldo no cambió
    }

    @Test
    void retirarSaldoInvalidoLanzaSaldoInvalido() {
        assertThrows(SaldoInvalidoException.class, () -> caja.retirarSaldo(BigDecimal.ZERO));
        assertThrows(SaldoInvalidoException.class, () -> caja.retirarSaldo(new BigDecimal("-1")));
        assertThrows(SaldoInvalidoException.class, () -> caja.retirarSaldo(null));
    }

    // ---------- Caja de ahorro ----------

    @Test
    void saldoDisponibleDeCajaEsSuSaldo() {
        assertMonto("100.00", caja.obtenerSaldoDisponible());
    }

    @Test
    @DisplayName("Interés mensual = saldo * tasa anual / 12 / 100 (12000 al 12% -> 120.00)")
    void calcularInteresMensual() {
        caja.setSaldo(new BigDecimal("12000.00"));
        assertMonto("120.00", caja.calcularInteresMensual());
    }

    @Test
    void interesMensualConSaldoCeroEsCero() {
        caja.setSaldo(BigDecimal.ZERO);
        assertMonto("0.00", caja.calcularInteresMensual());
    }

    @Test
    void interesMensualSeRedondeaADosDecimales() {
        caja.setSaldo(new BigDecimal("100.00"));
        caja.setPorcentajeTasaInteresAnual(new BigDecimal("10.00")); // 100*10/1200 = 0.8333...
        assertMonto("0.83", caja.calcularInteresMensual());
    }

    // ---------- Cuenta corriente ----------

    @Test
    void saldoDisponibleDeCorrienteSumaElDescubierto() {
        assertMonto("1000.00", corriente.obtenerSaldoDisponible());
    }

    @Test
    @DisplayName("Cuenta corriente: puede retirar hasta el límite de descubierto")
    void corrientePuedeRetirarHastaElLimite() {
        corriente.retirarSaldo(new BigDecimal("1000.00"));
        assertMonto("-1000.00", corriente.getSaldo());
        assertMonto("0.00", corriente.obtenerSaldoDisponible());
    }

    @Test
    @DisplayName("Cuenta corriente: no puede superar el límite de descubierto")
    void corrienteNoPuedeSuperarElLimite() {
        assertThrows(SaldoInsuficienteException.class,
                () -> corriente.retirarSaldo(new BigDecimal("1000.01")));
        assertMonto("0", corriente.getSaldo());
    }

    @Test
    void depositarEnDescubiertoReduceLaDeuda() {
        corriente.retirarSaldo(new BigDecimal("300.00"));  // saldo -300
        corriente.ingresarSaldo(new BigDecimal("100.00")); // saldo -200
        assertMonto("-200.00", corriente.getSaldo());
    }
}
