package com.edutech.microservicio_persona.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    // El RUT no debe ser auto_increment, es un identificador natural
    @Column(name = "rut", length = 8, nullable = false)
    private String rut;

    @Column(name = "dv", length = 1, nullable = false)
    private String dv;

    @Column(name = "nombres", nullable = false, length = 50)
    private String nombres;

    @Column(name = "ape1", nullable = false, length = 50)
    private String ape1;

    @Column(name = "ape2", length = 50)
    private String ape2;

    @Temporal(TemporalType.DATE)
    @Column(name = "fec_nac")
    private Date fechaNacimiento;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_persona", nullable = false)
    @JsonBackReference
    private TipoPersona tipoPersona;


}
