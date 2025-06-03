package com.edutech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", unique = true, nullable = false, length = 10)
    @NotBlank(message = "El código del curso es obligatorio")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "El código debe tener entre 3-10 caracteres alfanuméricos en mayúsculas")
    private String codigo;
    
    @Column(name = "nombre", nullable = false, length = 150)
    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(min = 5, max = 150, message = "El nombre debe tener entre 5 y 150 caracteres")
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @Column(name = "creditos", nullable = false)
    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "El curso debe tener al menos 1 crédito")
    @Max(value = 15, message = "El curso no puede tener más de 15 créditos")
    private Integer creditos;
    
    @Column(name = "horas_teoricas", nullable = false)
    @NotNull(message = "Las horas teóricas son obligatorias")
    @Min(value = 0, message = "Las horas teóricas no pueden ser negativas")
    @Max(value = 100, message = "Las horas teóricas no pueden exceder 100")
    private Integer horasTeoricas;
    
    @Column(name = "horas_practicas", nullable = false)
    @NotNull(message = "Las horas prácticas son obligatorias")
    @Min(value = 0, message = "Las horas prácticas no pueden ser negativas")
    @Max(value = 100, message = "Las horas prácticas no pueden exceder 100")
    private Integer horasPracticas;
    
    @Column(name = "nivel", nullable = false)
    @NotNull(message = "El nivel es obligatorio")
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 10, message = "El nivel máximo es 10")
    private Integer nivel;
    
    @Column(name = "prereq_curso_codigo", length = 10)
    private String prerequisitoCursoCodigo;
    
    @Column(name = "area", nullable = false, length = 100)
    @NotBlank(message = "El área es obligatoria")
    @Size(min = 3, max = 100, message = "El área debe tener entre 3 y 100 caracteres")
    private String area;
    
    @Column(name = "modalidad", nullable = false, length = 50)
    @NotBlank(message = "La modalidad es obligatoria")
    @Pattern(regexp = "^(PRESENCIAL|ONLINE|HIBRIDA)$", message = "La modalidad debe ser PRESENCIAL, ONLINE o HIBRIDA")
    private String modalidad;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (codigo != null) {
            codigo = codigo.toUpperCase();
        }
        if (modalidad != null) {
            modalidad = modalidad.toUpperCase();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        if (codigo != null) {
            codigo = codigo.toUpperCase();
        }
        if (modalidad != null) {
            modalidad = modalidad.toUpperCase();
        }
    }
    
    // Método auxiliar para obtener total de horas
    public Integer getTotalHoras() {
        return (horasTeoricas != null ? horasTeoricas : 0) + (horasPracticas != null ? horasPracticas : 0);
    }
}
