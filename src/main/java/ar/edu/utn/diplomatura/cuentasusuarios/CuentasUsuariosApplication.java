package ar.edu.utn.diplomatura.cuentasusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada. Ejecutar el main de esta clase levanta la aplicacion.
 *
 * @SpringBootApplication hace tres cosas:
 *   - marca esta clase como configuracion,
 *   - configura la aplicación sola según las librerias del pom.xml
 *     (por eso arranca Tomcat en el 8080 y se conecta a H2),
 *   - recorre este paquete y los de abajo buscando clases anotadas
 *     (@Component, @Service, @Repository, @RestController) para crearlas.
 *
 * Por eso toda clase que Spring deba gestionar tiene que estar dentro de
 * ar.edu.utn.diplomatura.cuentasusuarios o en un subpaquete.
 */
@SpringBootApplication
public class CuentasUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuentasUsuariosApplication.class, args);

        System.out.println();
        System.out.println("===========================================================");
        System.out.println("  App-cuentas-usuarios - Spring REST API y base de datos");
        System.out.println("  App:         http://localhost:8080/cuentas");
        System.out.println("               JDBC URL: jdbc:mysql://localhost:3306/app_cuentas_usuarios   user: root");
        System.out.println("===========================================================");
        System.out.println();

    }
}
