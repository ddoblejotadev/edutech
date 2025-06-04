package com.edutech.model;

//Importaciones de Anotaciones JPA
import jakarta.persistence.*;

//Importaciones para Lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Importaciones Java
import java.time.LocalDateTime;

@Entity
@Table(name = "calificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @Column(name = "numero_intento", nullable = false)
    private Integer numeroIntento;

    @Column(name = "puntaje_obtenido", nullable = false, precision = 5, scale = 2)
    private Double puntajeObtenido;

    @Column(name = "puntaje_maximo", nullable = false, precision = 5, scale = 2)
    private Double puntajeMaximo;

    @Column(name = "fecha_realizacion", nullable = false)
    private LocalDateTime fechaRealizacion;

    @Column(name = "tiempo_empleado")
    private Integer tiempoEmpleado;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "nota", precision = 4)
    private Double nota;

    @Column(name = "nota_chilena", precision = 3, scale = 1)
    private Double notaChilena;

    // Alias methods for compatibility
    public Persona getEstudiante() { return persona; }
    public void setEstudiante(Persona estudiante) { this.persona = estudiante; }
    
    public Integer getIntento() { return numeroIntento; }
    public void setIntento(Integer intento) { this.numeroIntento = intento; }
    
    public LocalDateTime getFechaEntrega() { return fechaRealizacion; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaRealizacion = fechaEntrega; }
    
    public Integer getTiempoUtilizadoMinutos() { return tiempoEmpleado; }
    public void setTiempoUtilizadoMinutos(Integer tiempo) { this.tiempoEmpleado = tiempo; }

    // Business methods
    public Double getPorcentajeObtenido() {
        if (puntajeMaximo != null && puntajeMaximo > 0) {
            return (puntajeObtenido / puntajeMaximo) * 100;
        }
        return 0.0;
    }

    public boolean isAprobada() {
        if (notaChilena != null) {
            return notaChilena >= 4.0;
        }
        if (evaluacion != null && evaluacion.getNotaMinima() != null && nota != null) {
            return nota >= evaluacion.getNotaMinima();
        }
        return false;
    }

    // Método para calcular nota chilena automáticamente
    public void calcularNotaChilena() {
        if (evaluacion != null && puntajeObtenido != null) {
            this.notaChilena = evaluacion.calcularNotaChilena(puntajeObtenido);
            this.nota = this.notaChilena; // Para compatibilidad
        }
    }

    public String getCalificacionCualitativa() {
        if (notaChilena == null) return "Sin calificar";
        
        if (notaChilena >= 6.0) return "Muy Bueno";
        if (notaChilena >= 5.0) return "Bueno";
        if (notaChilena >= 4.0) return "Suficiente";
        return "Insuficiente";
    }
}
