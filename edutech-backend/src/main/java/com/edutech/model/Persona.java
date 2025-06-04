package com.edutech.model;

//Importaciones de Anotaciones JPA
import jakarta.persistence.*;

//Importaciones para Lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Importaciones Java
import java.time.LocalDate;
import java.util.List;

//Importaciones de Jackson
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "personas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni", unique = true, nullable = false, length = 8)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_persona_id", nullable = false)
    private TipoPersona tipoPersona;

    @Column(name = "activo")
    private Boolean activo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;

    @JsonIgnore
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Calificacion> calificaciones;

    // Alias methods for compatibility
    public String getApellidoPaterno() {
        if (apellidos != null && apellidos.contains(" ")) {
            return apellidos.split(" ")[0];
        }
        return apellidos;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        String materno = getApellidoMaterno();
        if (materno != null && !materno.isEmpty()) {
            this.apellidos = apellidoPaterno + " " + materno;
        } else {
            this.apellidos = apellidoPaterno;
        }
    }

    public String getApellidoMaterno() {
        if (apellidos != null && apellidos.contains(" ")) {
            String[] parts = apellidos.split(" ");
            return parts.length > 1 ? parts[1] : "";
        }
        return "";
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        String paterno = getApellidoPaterno();
        if (paterno != null && !paterno.isEmpty()) {
            this.apellidos = paterno + " " + apellidoMaterno;
        }
    }

    public String getCorreo() {
        return email;
    }

    public void setCorreo(String correo) {
        this.email = correo;
    }
}
