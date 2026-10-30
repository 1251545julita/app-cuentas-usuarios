package ar.edu.utn.diplomatura.cuentasusuarios.componentes;

import org.springframework.stereotype.Component;

// Herramienta para trabajar con DNI.
@Component
public class ValidadorDeDni {

    /** Saca puntos y espacios. */
    public String normalizar(String unDni) {
        if (unDni == null) {
            return null;
        }
        return unDni.replace(".", "").trim();
    }

    /** Es verdadero cuando son 8 digitos. */
    public boolean esValido(String unDni) {
        String dniLimpio = normalizar(unDni);
        if (dniLimpio == null) {
            return false;
        }
        return dniLimpio.matches("[0-9]{8}");
    }

    /** 35789963 -> 35.789.963 */
    public String formatear(String unDni) {
        String dniLimpio = normalizar(unDni);
        if (!esValido(dniLimpio)) {
            return unDni;
        }
        return dniLimpio.substring(0, 2) + "." + dniLimpio.substring(2, 5) + "." + dniLimpio.substring(5);
    }
}
