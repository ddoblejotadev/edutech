package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calificacion")
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

    // Constructores
    public Calificacion() {}

    public Calificacion(Integer idEvaluacion, String rutEstudiante, Double puntajeObtenido, 
                       String comentario, LocalDateTime fechaCalificacion, LocalDateTime fechaEntrega) {
        this.idEvaluacion = idEvaluacion;
        this.rutEstudiante = rutEstudiante;
        this.puntajeObtenido = puntajeObtenido;
        this.comentario = comentario;
        this.fechaCalificacion = fechaCalificacion;
        this.fechaEntrega = fechaEntrega;
    }

    // Getters y Setters
    public Integer getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(Integer idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public Integer getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Integer idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public String getRutEstudiante() {
        return rutEstudiante;
    }

    public void setRutEstudiante(String rutEstudiante) {
        this.rutEstudiante = rutEstudiante;
    }

    public Double getPuntajeObtenido() {
        return puntajeObtenido;
    }

    public void setPuntajeObtenido(Double puntajeObtenido) {
        this.puntajeObtenido = puntajeObtenido;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDateTime fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}
