package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "respuesta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Respuesta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Integer idRespuesta;
    
    @Column(name = "rut_estudiante", nullable = false)
    private String rutEstudiante;
    
    @Column(name = "id_pregunta", nullable = false)
    private Integer idPregunta;
    
    @Column(name = "id_opcion")
    private Integer idOpcion;
    
    @Column(name = "texto_respuesta", columnDefinition = "TEXT")
    private String textoRespuesta;  // Para preguntas de desarrollo
    
    @Column(nullable = false)
    private Boolean correcta;
    
    private Double puntajeObtenido;
}
