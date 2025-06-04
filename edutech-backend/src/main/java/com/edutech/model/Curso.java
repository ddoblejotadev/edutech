package com.edutech.model;

//Importaciones de Anotaciones JPA
import jakarta.persistence.*;

//Importaciones para Lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Importaciones Java
import java.time.LocalDateTime;
import java.util.List;

//Importaciones de Jackson
import com.fasterxml.jackson.annotation.JsonIgnore;

//Entidad JPA
@Entity  // Marca esta clase como una entidad JPA.
@Table(name = "cursos")  // Especifica el nombre de la tabla en la base de datos.
@Data  // Genera automáticamente getters, setters, equals, hashCode y toString.
@NoArgsConstructor  // Genera un constructor sin argumentos.
@AllArgsConstructor  // Genera un constructor con un argumento por cada campo en la clase.
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 10)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "creditos", nullable = false)
    private Integer creditos;

    @Column(name = "horas_teoricas", nullable = false)
    private Integer horasTeoricas;

    @Column(name = "horas_practicas", nullable = false)
    private Integer horasPracticas;

    @Column(name = "total_horas", nullable = false)
    private Integer totalHoras;

    @Column(name = "ciclo", nullable = false, length = 10)
    private String ciclo;

    @Column(name = "modalidad", nullable = false, length = 20)
    private String modalidad;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo")
    private Boolean activo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ejecucion> ejecuciones;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
