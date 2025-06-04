package com.edutech.model;

//Importaciones de Anotaciones JPA
import jakarta.persistence.*;

//Importaciones para Lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Importaciones Java
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ejecucion_id", nullable = false)
    private Ejecucion ejecucion;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(name = "puntaje_total", nullable = false, precision = 5, scale = 2)
    private Double puntajeTotal;

    @Column(name = "nota_minima", nullable = false, precision = 5, scale = 2)
    private Double notaMinima;

    @Column(name = "intentos_permitidos", nullable = false)
    private Integer intentosPermitidos;

    @Column(name = "ponderacion", precision = 5, scale = 2)
    private Double ponderacion;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "publicada")
    private Boolean publicada = false;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pregunta> preguntas;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Calificacion> calificaciones;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (publicada == null) {
            publicada = false;
        }
        if (intentosPermitidos == null) {
            intentosPermitidos = 1;
        }
        if (tipo != null) {
            tipo = tipo.toUpperCase();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        if (tipo != null) {
            tipo = tipo.toUpperCase();
        }
    }

    // Métodos auxiliares
    public Boolean isDisponible() {
        LocalDateTime now = LocalDateTime.now();
        return publicada && activo &&
                now.isAfter(fechaInicio) &&
                now.isBefore(fechaFin);
    }

    public Boolean isVencida() {
        return LocalDateTime.now().isAfter(fechaFin);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ejecucion getEjecucion() { return ejecucion; }
    public void setEjecucion(Ejecucion ejecucion) { this.ejecucion = ejecucion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    // Alias methods for compatibility
    public LocalDateTime getFechaDisponible() { return fechaInicio; }
    public void setFechaDisponible(LocalDateTime fechaDisponible) { this.fechaInicio = fechaDisponible; }

    public LocalDateTime getFechaLimite() { return fechaFin; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaFin = fechaLimite; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public Double getPuntajeTotal() { return puntajeTotal; }
    public void setPuntajeTotal(Double puntajeTotal) { this.puntajeTotal = puntajeTotal; }

    public Double getNotaMinima() { return notaMinima; }
    public void setNotaMinima(Double notaMinima) { this.notaMinima = notaMinima; }

    public Integer getIntentosPermitidos() { return intentosPermitidos; }
    public void setIntentosPermitidos(Integer intentosPermitidos) { this.intentosPermitidos = intentosPermitidos; }

    public Double getPonderacion() { return ponderacion; }
    public void setPonderacion(Double ponderacion) { this.ponderacion = ponderacion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Boolean getPublicada() { return publicada; }
    public void setPublicada(Boolean publicada) { this.publicada = publicada; }
    public void setPublicada(boolean publicada) { this.publicada = publicada; }

    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas; }

    public List<Calificacion> getCalificaciones() { return calificaciones; }
    public void setCalificaciones(List<Calificacion> calificaciones) { this.calificaciones = calificaciones; }
}
