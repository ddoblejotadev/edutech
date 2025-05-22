package com.edutech.microservicio_comunicacion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ClienteService {

    private final RestTemplate restTemplate;
    
    @Value("${microservicio.persona.url}")
    private String personaServiceUrl;
    
    public ClienteService() {
        this.restTemplate = new RestTemplate();
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPersonaByRut(String rut) {
        try {
            String url = personaServiceUrl + "/api/personas/" + rut;
            System.out.println("Intentando conectar a: " + url);
            Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);
            System.out.println("Respuesta recibida: " + respuesta);
            return respuesta;
        } catch (Exception e) {
            System.err.println("Error conectando al microservicio de personas: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean existePersona(String rut) {
        try {
            Map<String, Object> persona = getPersonaByRut(rut);
            return persona != null;
        } catch (Exception e) {
            return false;
        }
    }
}
