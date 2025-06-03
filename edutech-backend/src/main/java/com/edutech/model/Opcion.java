package com.edutech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "opciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Opcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    @NotNull(message = "La pregunta es obligatoria")
    private Pregunta pregunta;
    
    @Column(name = "orden", nullable = false)
    @NotNull(message = "El orden es obligatorio")
    @Min(value = 1, message = "El orden debe ser al menos 1")
    @Max(value = 10, message = "El orden no puede exceder 10")
    private Integer orden;
    
    @Column(name = "texto", nullable = false, length = 500)
    @NotBlank(message = "El texto de la opción es obligatorio")
    @Size(min = 1, max = 500, message = "El texto debe tener entre 1 y 500 caracteres")
    private String texto;
    
    @Column(name = "es_correcta", nullable = false)
    @NotNull(message = "Debe especificar si es correcta")
    private Boolean esCorrecta = false;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @PrePersist
    protected void onCreate() {
        if (activo == null) {
            activo = true;
        }
        if (esCorrecta == null) {
            esCorrecta = false;
        }
    }
}
