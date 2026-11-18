package ar.edu.utn.diplomatura.cuentasusuarios.excepciones;

public class ModoEliminacionInvalidoException extends RuntimeException {

    public ModoEliminacionInvalidoException(String modo) {
        super("Modo de eliminación inválido: " + modo + ". Los valores permitidos son 'logico' o 'fisico'.");
    }
}
