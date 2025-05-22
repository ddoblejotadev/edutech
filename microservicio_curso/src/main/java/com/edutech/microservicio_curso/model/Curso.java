package com.edutech.microservicio_curso.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCurso;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Column(name = "duracion_horas")
    private int duracionHoras;
    
    @Column(name = "nivel", length = 50)
    private String nivel;
    
    @Column(name = "categoria", length = 100)
    private String categoria;
    
    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Column(name = "sence", length = 20)
    private String sence;
    
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
    
    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;
    
    @ManyToOne
    @JoinColumn(name = "tipo_curso_id", nullable = false)
    private TipoCurso tipoCurso;
    
    // Constructores
    public Curso() {
    }
    
    public Curso(Integer idCurso, String nombre, String descripcion, int duracionHoras, 
                String nivel, String categoria, BigDecimal precio, String sence,
                LocalDate fechaCreacion, LocalDate fechaPublicacion, TipoCurso tipoCurso) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionHoras = duracionHoras;
        this.nivel = nivel;
        this.categoria = categoria;
        this.precio = precio;
        this.sence = sence;
        this.fechaCreacion = fechaCreacion;
        this.fechaPublicacion = fechaPublicacion;
        this.tipoCurso = tipoCurso;
    }
    
    // Getters y Setters
    public Integer getIdCurso() {
        return idCurso;
    }
    
    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getDuracionHoras() {
        return duracionHoras;
    }
    
    public void setDuracionHoras(int duracionHoras) {
        this.duracionHoras = duracionHoras;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public BigDecimal getPrecio() {
        return precio;
    }
    
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    
    public String getSence() {
        return sence;
    }
    
    public void setSence(String sence) {
        this.sence = sence;
    }
    
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }
    
    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
    
    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }
    
    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }
}
