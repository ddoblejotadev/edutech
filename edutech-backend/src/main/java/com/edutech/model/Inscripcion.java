package com.edutech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_ejecucion", "rut_estudiante"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ejecucion", nullable = false)
    @NotNull(message = "La ejecución es obligatoria")
    private Ejecucion ejecucion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rut_estudiante", nullable = false)
    @NotNull(message = "El estudiante es obligatorio")
    private Persona estudiante;
    
    @Column(name = "fecha_inscripcion", nullable = false, updatable = false)
    private LocalDateTime fechaInscripcion;
    
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "INSCRITO"; // INSCRITO, RETIRADO, REPROBADO, APROBADO
    
    @Column(name = "nota_final")
    private Double notaFinal;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @PrePersist
    protected void onCreate() {
        fechaInscripcion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (estado == null) {
            estado = "INSCRITO";
        }
    }
}
