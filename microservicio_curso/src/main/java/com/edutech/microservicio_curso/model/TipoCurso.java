package com.edutech.microservicio_curso.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_curso")
public class TipoCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    private String nombre;
    
    @Column(name = "descripcion", length = 200)
    private String descripcion;
    
    // Constructores
    public TipoCurso() {
    }
    
    public TipoCurso(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
}
