package com.edutech.microservicio_evaluacion.config;

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
    
    @Value("${microservicio.curso.url}")
    private String cursoServiceUrl;
    
    @Value("${microservicio.ejecucion.url}")
    private String ejecucionServiceUrl;
    
    // Getters
    public String getPersonaServiceUrl() {
        return personaServiceUrl;
    }
    
    public String getCursoServiceUrl() {
        return cursoServiceUrl;
    }
    
    public String getEjecucionServiceUrl() {
        return ejecucionServiceUrl;
    }
}
