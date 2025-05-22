package com.edutech.microservicio_curso.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCurso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipocurso")
    private Integer id; // Cambiado de idTipoCurso a id para coincidir con los métodos del controlador
    
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}
