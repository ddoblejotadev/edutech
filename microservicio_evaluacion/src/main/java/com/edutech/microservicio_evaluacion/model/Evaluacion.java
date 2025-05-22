package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private Integer idEvaluacion;
    
    @Column(nullable = false)
    private String titulo;
    
    private String descripcion;
    
    @Column(name = "id_curso", nullable = false)
    private Integer idCurso;
    
    @Column(name = "id_ejecucion", nullable = false)
    private Integer idEjecucion;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_vencimiento")
    private LocalDateTime fechaVencimiento;
    
    @Column(nullable = false)
    private Integer puntajeMaximo;
    
    @Column(nullable = false)
    private String tipo;  // PRUEBA, TAREA, PROYECTO, EXAMEN, etc.
}
