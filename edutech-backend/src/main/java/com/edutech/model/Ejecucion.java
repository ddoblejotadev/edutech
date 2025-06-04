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

//Entidad JPA
@Entity  // Marca esta clase como una entidad JPA.
@Table(name = "ejecuciones")  // Especifica el nombre de la tabla en la base de datos.
@Data  // Genera automáticamente getters, setters, equals, hashCode y toString.
@NoArgsConstructor  // Genera un constructor sin argumentos.
@AllArgsConstructor  // Genera un constructor con un argumento por cada campo en la clase.
public class Ejecucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "periodo", nullable = false, length = 20)
    private String periodo;

    @Column(name = "seccion", nullable = false, length = 10)
    private String seccion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "aula", length = 50)
    private String aula;

    @Column(name = "horario", length = 100)
    private String horario;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Column(name = "inscritos_actuales")
    private Integer inscritosActuales = 0;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Persona profesor;

    @Column(name = "sala", length = 50)
    private String sala;

    @JsonIgnore
    @OneToMany(mappedBy = "ejecucion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;

    @JsonIgnore
    @OneToMany(mappedBy = "ejecucion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Evaluacion> evaluaciones;

    // Business method
    public Integer getCuposDisponibles() {
        return capacidadMaxima - (inscritosActuales != null ? inscritosActuales : 0);
    }

    // No setter needed - this is calculated
    public void setCuposDisponibles(Integer cupos) {
        // This method exists for compatibility but doesn't set anything
        // as cuposDisponibles is calculated
    }
}
