package com.edutech.model;

//Importaciones de Anotaciones JPA
import jakarta.persistence.*;

//Importaciones para Lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Importaciones Java
import java.util.List;

//Importaciones de Jackson
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tipos_persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPersona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoPersona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Persona> personas;
}
