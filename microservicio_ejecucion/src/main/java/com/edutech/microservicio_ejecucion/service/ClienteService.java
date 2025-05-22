package com.edutech.microservicio_ejecucion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ClienteService {

    private final RestTemplate restTemplate;
    
    @Value("${microservicio.persona.url:http://localhost:8081}")
    private String personaServiceUrl;
    
    @Value("${microservicio.curso.url:http://localhost:8082}")
    private String cursoServiceUrl;
    
    public ClienteService() {
        this.restTemplate = new RestTemplate();
    }
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPersonaByRut(String rut) {
        try {
            return restTemplate.getForObject(personaServiceUrl + "/api/personas/" + rut, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCursoById(Integer idCurso) {
        try {
            return restTemplate.getForObject(cursoServiceUrl + "/api/cursos/" + idCurso, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
}
