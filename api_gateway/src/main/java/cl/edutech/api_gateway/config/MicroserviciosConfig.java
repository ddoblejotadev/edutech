package cl.edutech.api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource(value = "classpath:microservicios.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "microservicios")
public class MicroserviciosConfig {
    
    private Servicio persona;
    private Servicio curso;
    private Servicio ejecucion;
    private Servicio comunicacion;
    private Servicio evaluacion;
    
    public static class Servicio {
        private String url;
        private Map<String, String> endpoints;
        
        // Getters y setters
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public Map<String, String> getEndpoints() {
            return endpoints;
        }
        
        public void setEndpoints(Map<String, String> endpoints) {
            this.endpoints = endpoints;
        }
    }
    
    // Getters y setters
    public Servicio getPersona() {
        return persona;
    }
    
    public void setPersona(Servicio persona) {
        this.persona = persona;
    }
      public Servicio getCurso() {
        return curso;
    }
    
    public void setCurso(Servicio curso) {
        this.curso = curso;
    }
    
    public Servicio getEjecucion() {
        return ejecucion;
    }
    
    public void setEjecucion(Servicio ejecucion) {
        this.ejecucion = ejecucion;
    }
    
    public Servicio getComunicacion() {
        return comunicacion;
    }
    
    public void setComunicacion(Servicio comunicacion) {
        this.comunicacion = comunicacion;
    }
    
    public Servicio getEvaluacion() {
        return evaluacion;
    }
    
    public void setEvaluacion(Servicio evaluacion) {
        this.evaluacion = evaluacion;
    }
}
