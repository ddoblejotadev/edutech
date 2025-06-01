package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "respuesta")
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
    private String textoRespuesta;
    
    @Column(nullable = false)
    private Boolean correcta;
    
    private Double puntajeObtenido;

    // Constructores
    public Respuesta() {}

    public Respuesta(String rutEstudiante, Integer idPregunta, Integer idOpcion, 
                    String textoRespuesta, Boolean correcta, Double puntajeObtenido) {
        this.rutEstudiante = rutEstudiante;
        this.idPregunta = idPregunta;
        this.idOpcion = idOpcion;
        this.textoRespuesta = textoRespuesta;
        this.correcta = correcta;
        this.puntajeObtenido = puntajeObtenido;
    }

    // Getters y Setters
    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getRutEstudiante() {
        return rutEstudiante;
    }

    public void setRutEstudiante(String rutEstudiante) {
        this.rutEstudiante = rutEstudiante;
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public Integer getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Integer idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getTextoRespuesta() {
        return textoRespuesta;
    }

    public void setTextoRespuesta(String textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }

    public Boolean getCorrecta() {
        return correcta;
    }

    public void setCorrecta(Boolean correcta) {
        this.correcta = correcta;
    }

    public Double getPuntajeObtenido() {
        return puntajeObtenido;
    }

    public void setPuntajeObtenido(Double puntajeObtenido) {
        this.puntajeObtenido = puntajeObtenido;
    }
}
