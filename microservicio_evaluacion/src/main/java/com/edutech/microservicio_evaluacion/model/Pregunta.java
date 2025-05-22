package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pregunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;
    
    @Column(nullable = false)
    private String enunciado;
    
    private String descripcion;
    
    @Column(nullable = false)
    private String tipo;  // MULTIPLE_CHOICE, VERDADERO_FALSO, DESARROLLO, etc.
    
    private Double puntaje;
    
    @Column(name = "id_evaluacion", nullable = false)
    private Integer idEvaluacion;
}
