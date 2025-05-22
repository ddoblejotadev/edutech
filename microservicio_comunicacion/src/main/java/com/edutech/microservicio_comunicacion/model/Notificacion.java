package com.edutech.microservicio_comunicacion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
public class Notificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    // Añadimos este campo para el método getIdPersona() usado en NotificacionService
    @Column(name = "id_persona")
    private Long idPersona;
    
    @Column(nullable = false, length = 500)
    private String mensaje;
    
    @Column(nullable = false)
    private String tipo;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column
    private boolean leida;
    
    // Constructor vacío
    public Notificacion() {
    }
    
    // Constructor con todos los argumentos
    public Notificacion(Long id, Long usuarioId, Long idPersona, String mensaje, String tipo, 
                       LocalDateTime fechaCreacion, boolean leida) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.idPersona = idPersona;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
        this.leida = leida;
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Long getIdPersona() {
        return idPersona;
    }
    
    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public boolean isLeida() {
        return leida;
    }
    
    public void setLeida(boolean leida) {
        this.leida = leida;
    }
    
    // equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Notificacion notificacion = (Notificacion) o;
        
        return id != null ? id.equals(notificacion.id) : notificacion.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", idPersona=" + idPersona +
                ", mensaje='" + mensaje + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", leida=" + leida +
                '}';
    }
}
