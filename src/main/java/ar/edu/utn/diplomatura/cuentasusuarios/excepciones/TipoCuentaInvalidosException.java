package ar.edu.utn.diplomatura.cuentasusuarios.excepciones;

public class TipoCuentaInvalidosException extends RuntimeException{

    public TipoCuentaInvalidosException(String tipoCuenta){
        super("El valor " + tipoCuenta + " es inválido. Debe ser Caja de Ahorro o Cuenta Corriente.");
    }
}
