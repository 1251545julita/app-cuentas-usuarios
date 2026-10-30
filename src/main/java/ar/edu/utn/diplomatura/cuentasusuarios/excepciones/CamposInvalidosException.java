package ar.edu.utn.diplomatura.cuentasusuarios.excepciones;

public class CamposInvalidosException extends RuntimeException{

    public CamposInvalidosException(String campo, String tipoCuenta){
        super("El campo " + campo + " sólo aplica a una " + tipoCuenta);
    }
}
