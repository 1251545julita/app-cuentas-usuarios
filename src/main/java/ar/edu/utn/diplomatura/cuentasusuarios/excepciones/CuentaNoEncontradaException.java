package ar.edu.utn.diplomatura.cuentasusuarios.excepciones;

public class CuentaNoEncontradaException extends RuntimeException{

    public CuentaNoEncontradaException(Long id){
        super("No existe la cuenta con el id " + id);
    }
}
