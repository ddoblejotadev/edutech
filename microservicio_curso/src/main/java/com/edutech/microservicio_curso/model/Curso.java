package com.edutech.microservicio_curso.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso") // Asegúrate que coincida con el nombre real de la columna
    private Integer idCurso;
    
    // Si usas control de versiones, asegúrate de que la columna exista en la base de datos
    @Version
    @Column(name = "version")
    private Long version = 0L; // inicializar en 0
    
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(length = 100)
    private String descripcion;
    
    @Column(name = "fec_creacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now(); // Valor predeterminado
    
    @Column(name = "fec_publicacion")
    private LocalDate fechaPublicacion;
    
    @Column(length = 1)
    private String sence;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;
    
    @ManyToOne
    @JoinColumn(name = "id_tipocurso")
    @JsonBackReference
    private TipoCurso tipoCurso;
    
    // Estos campos no están en el modelo SQL pero los mantendremos 
    // para compatibilidad con el código existente
    private String categoria;
    private Integer duracionHoras;
    private String nivel;
}
