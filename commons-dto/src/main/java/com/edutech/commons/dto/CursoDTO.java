package com.edutech.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Integer id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
    
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracionHoras;
    
    private String nivel;
    private String categoria;
    
    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;
    
    private String sence;
    private LocalDate fechaCreacion;
    private LocalDate fechaPublicacion;
    
    @NotNull(message = "El tipo de curso es obligatorio")
    private TipoCursoDTO tipoCurso;
}
