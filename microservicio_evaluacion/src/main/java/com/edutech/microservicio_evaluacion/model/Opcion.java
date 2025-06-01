package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "opcion")
public class Opcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Integer idOpcion;
    
    @Column(name = "id_pregunta", nullable = false)
    private Integer idPregunta;
    
    @Column(nullable = false)
    private String texto;
    
    @Column(nullable = false)
    private Boolean correcta;

    public Opcion() {}

    public Opcion(Integer idPregunta, String texto, Boolean correcta) {
        this.idPregunta = idPregunta;
        this.texto = texto;
        this.correcta = correcta;
    }

    public Integer getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Integer idOpcion) {
        this.idOpcion = idOpcion;
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Boolean getCorrecta() {
        return correcta;
    }

    public void setCorrecta(Boolean correcta) {
        this.correcta = correcta;
    }
}
