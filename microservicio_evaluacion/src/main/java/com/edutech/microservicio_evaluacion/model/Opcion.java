package com.edutech.microservicio_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "opcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Opcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Integer idOpcion;
    
    @Column(nullable = false)
    private String texto;
    
    @Column(nullable = false)
    private Boolean correcta;
    
    @Column(name = "id_pregunta", nullable = false)
    private Integer idPregunta;
}
