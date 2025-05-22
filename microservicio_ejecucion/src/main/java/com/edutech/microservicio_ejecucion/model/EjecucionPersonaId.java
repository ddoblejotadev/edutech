package com.edutech.microservicio_ejecucion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjecucionPersonaId implements Serializable {
    
    @Column(name = "rut_persona")
    private String rutPersona;
    
    @Column(name = "id_ejecucion")
    private Integer idEjecucion;
}
