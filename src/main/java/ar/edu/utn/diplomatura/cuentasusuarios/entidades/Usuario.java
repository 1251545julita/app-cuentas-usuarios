package ar.edu.utn.diplomatura.cuentasusuarios.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Un usuario en el sistema del banco.
 *
 * @Entity le dice a JPA que esta clase se guarda en una tabla:
 * la clase es la tabla, cada atributo es una columna y cada objeto es una fila.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private LocalDateTime fechaAlta;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Cuenta> cuentas = new ArrayList<>();

    /** Constructor vacio: JPA lo necesita para reconstruir el objeto. */
    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String dni, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaAlta = LocalDateTime.now();;
    }

    // Getters y setters: se usan para armar el JSON de las respuestas.

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }
    public void setFechaAlta(LocalDateTime fechaAlta) {}

    public List<Cuenta> getCuentas() {
        return cuentas;
    }
    public void setCuentas(List<Cuenta> cuentas) { this.cuentas = cuentas; }

}
