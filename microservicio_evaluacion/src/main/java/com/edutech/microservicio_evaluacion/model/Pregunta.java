package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pregunta")
public class Pregunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;
    
    @Column(nullable = false)
    private String enunciado;
    
    private String descripcion;
    
    @Column(nullable = false)
    private String tipo;
    
    private Double puntaje;
    
    @Column(name = "id_evaluacion", nullable = false)
    private Integer idEvaluacion;

    public Pregunta() {}

    public Pregunta(String enunciado, String descripcion, String tipo, Double puntaje, Integer idEvaluacion) {
        this.enunciado = enunciado;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.puntaje = puntaje;
        this.idEvaluacion = idEvaluacion;
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Double puntaje) {
        this.puntaje = puntaje;
    }

    public Integer getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Integer idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }
}
