package com.edutech.microservicio_comunicacion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuración para conectar con otros microservicios
 */
@Configuration
@PropertySource("classpath:microservicios.yml")
public class MicroserviciosConfig {

    @Value("${microservicio.persona.url}")
    private String personaServiceUrl;
    
    // Getters
    public String getPersonaServiceUrl() {
        return personaServiceUrl;
    }
}
