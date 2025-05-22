package com.edutech.microservicio_persona.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tipo_persona")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_persona")
    private Integer idTipoPersona;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @OneToMany(mappedBy = "tipoPersona", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Persona> personas;

}
