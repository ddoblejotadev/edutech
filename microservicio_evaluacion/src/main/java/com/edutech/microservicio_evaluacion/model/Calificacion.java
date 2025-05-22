package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion")
    private Integer idCalificacion;
    
    @Column(name = "id_evaluacion", nullable = false)
    private Integer idEvaluacion;
    
    @Column(name = "rut_estudiante", nullable = false)
    private String rutEstudiante;
    
    @Column(nullable = false)
    private Double puntajeObtenido;
    
    private String comentario;
    
    @Column(name = "fecha_calificacion", nullable = false)
    private LocalDateTime fechaCalificacion;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
}
