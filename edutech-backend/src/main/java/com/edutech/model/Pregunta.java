package com.edutech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "preguntas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evaluacion", nullable = false)
    @NotNull(message = "La evaluación es obligatoria")
    private Evaluacion evaluacion;
    
    @Column(name = "numero_pregunta", nullable = false)
    @NotNull(message = "El número de pregunta es obligatorio")
    @Min(value = 1, message = "El número de pregunta debe ser al menos 1")
    private Integer numeroPregunta;
    
    @Column(name = "enunciado", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "El enunciado es obligatorio")
    @Size(min = 10, max = 2000, message = "El enunciado debe tener entre 10 y 2000 caracteres")
    private String enunciado;
    
    @Column(name = "tipo", nullable = false, length = 50)
    @NotBlank(message = "El tipo de pregunta es obligatorio")
    @Pattern(regexp = "^(MULTIPLE_CHOICE|TRUE_FALSE|OPEN_TEXT|NUMERIC)$", 
             message = "El tipo debe ser MULTIPLE_CHOICE, TRUE_FALSE, OPEN_TEXT o NUMERIC")
    private String tipo;
    
    @Column(name = "puntaje", nullable = false)
    @NotNull(message = "El puntaje es obligatorio")
    @DecimalMin(value = "0.1", message = "El puntaje debe ser al menos 0.1")
    @DecimalMax(value = "50.0", message = "El puntaje no puede exceder 50.0")
    private Double puntaje;
    
    @Column(name = "explicacion", columnDefinition = "TEXT")
    @Size(max = 1000, message = "La explicación no puede exceder 1000 caracteres")
    private String explicacion;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orden ASC")
    private List<Opcion> opciones = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        if (activo == null) {
            activo = true;
        }
        if (tipo != null) {
            tipo = tipo.toUpperCase();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        if (tipo != null) {
            tipo = tipo.toUpperCase();
        }
    }
    
    // Método auxiliar para obtener la opción correcta
    public Opcion getOpcionCorrecta() {
        return opciones.stream()
                .filter(Opcion::getEsCorrecta)
                .findFirst()
                .orElse(null);
    }
    
    // Método auxiliar para verificar si es pregunta de opción múltiple
    public Boolean isMultipleChoice() {
        return "MULTIPLE_CHOICE".equals(tipo);
    }
    
    // Método auxiliar para verificar si es pregunta verdadero/falso
    public Boolean isTrueFalse() {
        return "TRUE_FALSE".equals(tipo);
    }
}
