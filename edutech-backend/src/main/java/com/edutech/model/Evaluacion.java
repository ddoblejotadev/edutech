package com.edutech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ejecucion", nullable = false)
    @NotNull(message = "La ejecución es obligatoria")
    private Ejecucion ejecucion;
    
    @Column(name = "titulo", nullable = false, length = 200)
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 200, message = "El título debe tener entre 5 y 200 caracteres")
    private String titulo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @Column(name = "tipo", nullable = false, length = 50)
    @NotBlank(message = "El tipo de evaluación es obligatorio")
    @Pattern(regexp = "^(PARCIAL|FINAL|QUIZ|TAREA|PROYECTO|PRESENTACION)$", 
             message = "El tipo debe ser PARCIAL, FINAL, QUIZ, TAREA, PROYECTO o PRESENTACION")
    private String tipo;
    
    @Column(name = "fecha_disponible", nullable = false)
    @NotNull(message = "La fecha disponible es obligatoria")
    private LocalDateTime fechaDisponible;
    
    @Column(name = "fecha_limite", nullable = false)
    @NotNull(message = "La fecha límite es obligatoria")
    private LocalDateTime fechaLimite;
    
    @Column(name = "duracion_minutos")
    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    @Max(value = 480, message = "La duración no puede exceder 480 minutos (8 horas)")
    private Integer duracionMinutos;
    
    @Column(name = "puntaje_total", nullable = false)
    @NotNull(message = "El puntaje total es obligatorio")
    @DecimalMin(value = "1.0", message = "El puntaje total debe ser al menos 1.0")
    @DecimalMax(value = "100.0", message = "El puntaje total no puede exceder 100.0")
    private Double puntajeTotal;
    
    @Column(name = "ponderacion", nullable = false)
    @NotNull(message = "La ponderación es obligatoria")
    @DecimalMin(value = "0.01", message = "La ponderación debe ser al menos 0.01")
    @DecimalMax(value = "1.0", message = "La ponderación no puede exceder 1.0")
    private Double ponderacion;
    
    @Column(name = "intentos_permitidos", nullable = false)
    @NotNull(message = "Los intentos permitidos son obligatorios")
    @Min(value = 1, message = "Debe permitir al menos 1 intento")
    @Max(value = 10, message = "No puede permitir más de 10 intentos")
    private Integer intentosPermitidos = 1;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @Column(name = "publicada", nullable = false)
    private Boolean publicada = false;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pregunta> preguntas = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (publicada == null) {
            publicada = false;
        }
        if (intentosPermitidos == null) {
            intentosPermitidos = 1;
        }
        if (tipo != null) {
            tipo = tipo.toUpperCase();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        if (tipo != null) {
            tipo = tipo.toUpperCase();
        }
    }
    
    // Métodos auxiliares
    public Boolean isDisponible() {
        LocalDateTime now = LocalDateTime.now();
        return publicada && activo && 
               now.isAfter(fechaDisponible) && 
               now.isBefore(fechaLimite);
    }
    
    public Boolean isVencida() {
        return LocalDateTime.now().isAfter(fechaLimite);
    }
    
    @AssertTrue(message = "La fecha límite debe ser posterior a la fecha disponible")
    public boolean isFechaLimiteValida() {
        return fechaLimite == null || fechaDisponible == null || fechaLimite.isAfter(fechaDisponible);
    }
}
