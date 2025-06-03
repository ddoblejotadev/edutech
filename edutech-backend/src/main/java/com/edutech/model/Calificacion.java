package com.edutech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_evaluacion", nullable = false)
    @NotNull(message = "La evaluación es obligatoria")
    private Evaluacion evaluacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rut_estudiante", nullable = false)
    @NotNull(message = "El estudiante es obligatorio")
    private Persona estudiante;
    
    @Column(name = "intento", nullable = false)
    @NotNull(message = "El número de intento es obligatorio")
    @Min(value = 1, message = "El intento debe ser al menos 1")
    private Integer intento = 1;
    
    @Column(name = "puntaje_obtenido", nullable = false)
    @NotNull(message = "El puntaje obtenido es obligatorio")
    @DecimalMin(value = "0.0", message = "El puntaje no puede ser negativo")
    private Double puntajeObtenido;
    
    @Column(name = "nota", nullable = false)
    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "1.0", message = "La nota mínima es 1.0")
    @DecimalMax(value = "7.0", message = "La nota máxima es 7.0")
    private Double nota;
    
    @Column(name = "fecha_inicio", nullable = false)
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    @Column(name = "tiempo_utilizado_minutos")
    @Min(value = 0, message = "El tiempo utilizado no puede ser negativo")
    private Integer tiempoUtilizadoMinutos;
    
    @Column(name = "estado", nullable = false, length = 20)
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(EN_PROGRESO|ENTREGADA|CALIFICADA|VENCIDA)$", 
             message = "El estado debe ser EN_PROGRESO, ENTREGADA, CALIFICADA o VENCIDA")
    private String estado = "EN_PROGRESO";
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;
    
    @Column(name = "fecha_calificacion")
    private LocalDateTime fechaCalificacion;
    
    @PrePersist
    protected void onCreate() {
        if (fechaInicio == null) {
            fechaInicio = LocalDateTime.now();
        }
        if (intento == null) {
            intento = 1;
        }
        if (estado == null) {
            estado = "EN_PROGRESO";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        if ("CALIFICADA".equals(estado) && fechaCalificacion == null) {
            fechaCalificacion = LocalDateTime.now();
        }
    }
    
    // Métodos auxiliares
    public Boolean isAprobada() {
        return nota != null && nota >= 4.0;
    }
    
    public Boolean isEntregada() {
        return "ENTREGADA".equals(estado) || "CALIFICADA".equals(estado);
    }
    
    public Boolean isVencida() {
        return "VENCIDA".equals(estado);
    }
    
    public Double getPorcentajeObtenido() {
        if (puntajeObtenido == null || evaluacion == null || evaluacion.getPuntajeTotal() == null) {
            return 0.0;
        }
        return (puntajeObtenido / evaluacion.getPuntajeTotal()) * 100.0;
    }
}
