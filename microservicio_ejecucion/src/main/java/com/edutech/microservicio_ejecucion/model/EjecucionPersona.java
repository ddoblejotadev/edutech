package com.edutech.microservicio_ejecucion.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ejecucion_persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjecucionPersona {
    
    @EmbeddedId
    private EjecucionPersonaId id;
    
    @ManyToOne
    @MapsId("idEjecucion")
    @JoinColumn(name = "id_ejecucion")
    @JsonBackReference
    private Ejecucion ejecucion;
}
