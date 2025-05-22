package com.edutech.microservicio_ejecucion.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ejecucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ejecucion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejecucion")
    private Integer idEjecucion;
    
    @Column(name = "fec_inicio", nullable = false)
    private LocalDate fecInicio;
    
    @Column(name = "fec_fin")
    private LocalDate fecFin;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(name = "id_curso", nullable = false)
    private Integer idCurso;
    
    @OneToMany(mappedBy = "ejecucion", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EjecucionPersona> inscripciones;
}
