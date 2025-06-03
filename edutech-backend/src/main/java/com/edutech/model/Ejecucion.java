package com.edutech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ejecuciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ejecucion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_curso", nullable = false)
    @NotNull(message = "El curso es obligatorio")
    private Curso curso;
    
    @Column(name = "periodo", nullable = false, length = 20)
    @NotBlank(message = "El período es obligatorio")
    @Pattern(regexp = "^[0-9]{4}-[12]$", message = "El período debe tener formato YYYY-1 o YYYY-2")
    private String periodo;
    
    @Column(name = "seccion", nullable = false, length = 5)
    @NotBlank(message = "La sección es obligatoria")
    @Pattern(regexp = "^[A-Z0-9]{1,5}$", message = "La sección debe tener entre 1-5 caracteres alfanuméricos")
    private String seccion;
    
    @Column(name = "fecha_inicio", nullable = false)
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser futura")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rut_profesor")
    private Persona profesor;
    
    @Column(name = "sala", length = 50)
    @Size(max = 50, message = "La sala no puede exceder 50 caracteres")
    private String sala;
    
    @Column(name = "horario", length = 200)
    @Size(max = 200, message = "El horario no puede exceder 200 caracteres")
    private String horario;
    
    @Column(name = "cupos_disponibles", nullable = false)
    @NotNull(message = "Los cupos disponibles son obligatorios")
    @Min(value = 1, message = "Debe haber al menos 1 cupo disponible")
    @Max(value = 50, message = "No puede haber más de 50 cupos")
    private Integer cuposDisponibles;
    
    @Column(name = "cupos_ocupados", nullable = false)
    @Min(value = 0, message = "Los cupos ocupados no pueden ser negativos")
    private Integer cuposOcupados = 0;
    
    @Column(name = "estado", nullable = false, length = 20)
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(PROGRAMADA|EN_CURSO|FINALIZADA|CANCELADA)$", 
             message = "El estado debe ser PROGRAMADA, EN_CURSO, FINALIZADA o CANCELADA")
    private String estado = "PROGRAMADA";
    
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
        if (cuposOcupados == null) {
            cuposOcupados = 0;
        }
        if (estado == null) {
            estado = "PROGRAMADA";
        }
        if (seccion != null) {
            seccion = seccion.toUpperCase();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        if (seccion != null) {
            seccion = seccion.toUpperCase();
        }
    }
    
    // Métodos auxiliares
    public Integer getCuposLibres() {
        return cuposDisponibles - cuposOcupados;
    }
    
    public Boolean isCuposAgotados() {
        return cuposOcupados >= cuposDisponibles;
    }
    
    public String getCodigoCompleto() {
        return curso.getCodigo() + "-" + seccion + "-" + periodo;
    }
    
    @AssertTrue(message = "La fecha de fin debe ser posterior a la fecha de inicio")
    public boolean isFechaFinPosterior() {
        return fechaFin == null || fechaInicio == null || fechaFin.isAfter(fechaInicio);
    }
    
    @AssertTrue(message = "Los cupos ocupados no pueden exceder los cupos disponibles")
    public boolean isCuposValidos() {
        return cuposOcupados == null || cuposDisponibles == null || cuposOcupados <= cuposDisponibles;
    }
}
