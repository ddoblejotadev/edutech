package com.edutech.microservicio_comunicacion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "remitente_id", nullable = false)
    private Long remitenteId;
    
    // Necesitamos estos campos adicionales para los métodos usados en MensajeService
    @Column(name = "remitente")
    private String remitente;
    
    @Column(name = "destinatario")
    private String destinatario;
    
    @Column(name = "destinatario_id", nullable = false)
    private Long destinatarioId;
    
    @Column(nullable = false, length = 500)
    private String contenido;
    
    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
    
    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;
    
    @Column(name = "leido")
    private boolean leido;
    
    // Constructor vacío
    public Mensaje() {
    }
    
    // Constructor con todos los argumentos
    public Mensaje(Long id, Long remitenteId, String remitente, Long destinatarioId, 
                  String destinatario, String contenido, LocalDateTime fechaEnvio, 
                  LocalDateTime fechaLectura, boolean leido) {
        this.id = id;
        this.remitenteId = remitenteId;
        this.remitente = remitente;
        this.destinatarioId = destinatarioId;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
        this.fechaLectura = fechaLectura;
        this.leido = leido;
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRemitenteId() {
        return remitenteId;
    }
    
    public void setRemitenteId(Long remitenteId) {
        this.remitenteId = remitenteId;
    }
    
    public String getRemitente() {
        return remitente;
    }
    
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }
    
    public Long getDestinatarioId() {
        return destinatarioId;
    }
    
    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    public LocalDateTime getFechaLectura() {
        return fechaLectura;
    }
    
    public void setFechaLectura(LocalDateTime fechaLectura) {
        this.fechaLectura = fechaLectura;
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void setLeido(boolean leido) {
        this.leido = leido;
    }
    
    // equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Mensaje mensaje = (Mensaje) o;
        
        return id != null ? id.equals(mensaje.id) : mensaje.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Mensaje{" +
                "id=" + id +
                ", remitenteId=" + remitenteId +
                ", remitente='" + remitente + '\'' +
                ", destinatarioId=" + destinatarioId +
                ", destinatario='" + destinatario + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", fechaLectura=" + fechaLectura +
                ", leido=" + leido +
                '}';
    }
}
