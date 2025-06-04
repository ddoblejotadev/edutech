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

    @Column(name = "puntaje_total", nullable = false, precision = 5)
    private Double puntajeTotal;

    @Column(name = "nota_minima_aprobacion", nullable = false, precision = 3, scale = 1)
    private Double notaMinimaAprobacion = 4.0;

    @Column(name = "nota_maxima", nullable = false, precision = 3, scale = 1)
    private Double notaMaxima = 7.0;

    @Column(name = "exigencia_porcentual", nullable = false, precision = 5, scale = 2)
    private Double exigenciaPorcentual = 60.0;

    @Column(name = "intentos_permitidos", nullable = false)
    private Integer intentosPermitidos;

    @Column(name = "ponderacion", precision = 5)
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
        if (notaMinimaAprobacion == null) {
            notaMinimaAprobacion = 4.0;
        }
        if (notaMaxima == null) {
            notaMaxima = 7.0;
        }
        if (exigenciaPorcentual == null) {
            exigenciaPorcentual = 60.0;
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

    // Alias methods for compatibility
    public LocalDateTime getFechaDisponible() { return fechaInicio; }
    public void setFechaDisponible(LocalDateTime fechaDisponible) { this.fechaInicio = fechaDisponible; }

    public LocalDateTime getFechaLimite() { return fechaFin; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaFin = fechaLimite; }

    public Double getNotaMinima() {
        return notaMinimaAprobacion;
    }
    public void setNotaMinima(Double notaMinima) {
        this.notaMinimaAprobacion = notaMinima;
    }

    // Método para calcular nota chilena basada en puntaje
    public Double calcularNotaChilena(Double puntajeObtenido) {
        if (puntajeObtenido == null || puntajeTotal == null || puntajeTotal == 0) {
            return 1.0;
        }
        
        Double porcentajeObtenido = (puntajeObtenido / puntajeTotal) * 100;
        
        if (porcentajeObtenido < exigenciaPorcentual) {
            // Escala de 1.0 a 3.9 para puntajes bajo la exigencia
            return 1.0 + (porcentajeObtenido / exigenciaPorcentual) * 2.9;
        } else {
            // Escala de 4.0 a 7.0 para puntajes sobre la exigencia
            Double porcentajeSobreExigencia = (porcentajeObtenido - exigenciaPorcentual) / (100 - exigenciaPorcentual);
            return 4.0 + porcentajeSobreExigencia * 3.0;
        }
    }
}
