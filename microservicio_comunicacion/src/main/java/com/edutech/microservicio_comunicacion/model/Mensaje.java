package com.edutech.microservicio_comunicacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;
    
    @Column(name = "remitente", nullable = false)
    private String remitente;
    
    @Column(name = "destinatario", nullable = false)
    private String destinatario;
    
    @Column(nullable = false)
    private String asunto;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
    
    @Column(nullable = false)
    private Boolean leido;
    
    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;
}
