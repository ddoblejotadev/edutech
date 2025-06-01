package com.edutech.microservicio_evaluacion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "microservicio")
public class MicroserviciosConfig {
    
    private String personaUrl = "http://localhost:8081";
    private String cursoUrl = "http://localhost:8082";
    private String ejecucionUrl = "http://localhost:8083";
    
    // Getters and Setters
    public String getPersonaUrl() {
        return personaUrl;
    }
    
    public void setPersonaUrl(String personaUrl) {
        this.personaUrl = personaUrl;
    }
    
    public String getCursoUrl() {
        return cursoUrl;
    }
    
    public void setCursoUrl(String cursoUrl) {
        this.cursoUrl = cursoUrl;
    }
    
    public String getEjecucionUrl() {
        return ejecucionUrl;
    }
    
    public void setEjecucionUrl(String ejecucionUrl) {
        this.ejecucionUrl = ejecucionUrl;
    }
}
