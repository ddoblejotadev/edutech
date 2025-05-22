package com.edutech.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Registro de servicios para centralizar las URLs de los microservicios
 */
@Configuration
@PropertySource(value = "classpath:microservicios.yml", factory = YamlPropertySourceFactory.class)
public class ServiceRegistry {

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
