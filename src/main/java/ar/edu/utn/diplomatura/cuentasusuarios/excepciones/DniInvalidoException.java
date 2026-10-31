package ar.edu.utn.diplomatura.cuentasusuarios.excepciones;

public class DniInvalidoException extends RuntimeException{

    public DniInvalidoException(String dni){
        super("El dni " + dni + " es inválido, revisar que contenga 8 dígitos.");
    }
}
