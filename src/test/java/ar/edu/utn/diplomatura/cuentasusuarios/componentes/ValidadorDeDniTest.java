package ar.edu.utn.diplomatura.cuentasusuarios.componentes;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorDeDniTest {

    private ValidadorDeDni validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorDeDni();
    }

    @Test
    void dniConPuntosEsValido(){
        boolean resultado = validador.esValido("40.123.456");
        assertTrue(resultado);
    }

    @Test
    void dniConLetrasNoEsValido(){
        boolean resultado = validador.esValido("36.547.88B");
        assertFalse(resultado);
    }

    @Test
    void normalizarDeNullDevuelveNull(){
        assertNull(validador.normalizar(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"20123456", "  20123456  ", "20.123.456"})
    void variosFormatosDeDni(String unDni){
        assertTrue(validador.esValido(unDni),"Debería ser válido"+ unDni);
    }

    @ParameterizedTest
    @ValueSource(strings = {"M8345679", "36.547.88B"})
    void formatosNoValidosDeDni(String unDni){
        assertFalse(validador.esValido(unDni),"Debería ser no válido"+ unDni);
    }

}