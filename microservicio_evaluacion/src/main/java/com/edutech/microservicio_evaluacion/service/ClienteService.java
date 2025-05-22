package com.edutech.microservicio_evaluacion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ClienteService {

    private final RestTemplate restTemplate;
    
    @Value("${microservicio.persona.url}")
    private String personaServiceUrl;
    
    @Value("${microservicio.curso.url}")
    private String cursoServiceUrl;
    
    @Value("${microservicio.ejecucion.url}")
    private String ejecucionServiceUrl;
    
    public ClienteService() {
        this.restTemplate = new RestTemplate();
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPersonaByRut(String rut) {
        try {
            return restTemplate.getForObject(personaServiceUrl + "/api/personas/" + rut, Map.class);
        } catch (Exception e) {
            System.err.println("Error al obtener persona: " + e.getMessage());
            return null;
        }
    }
    
    public boolean existePersona(String rut) {
        try {
            Map<String, Object> persona = getPersonaByRut(rut);
            return persona != null;
        } catch (Exception e) {
            System.err.println("Error al verificar persona: " + e.getMessage());
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCursoById(Integer idCurso) {
        try {
            return restTemplate.getForObject(cursoServiceUrl + "/api/cursos/" + idCurso, Map.class);
        } catch (Exception e) {
            System.err.println("Error al obtener curso: " + e.getMessage());
            return null;
        }
    }
    
    public boolean existeCurso(Integer idCurso) {
        try {
            Map<String, Object> curso = getCursoById(idCurso);
            return curso != null;
        } catch (Exception e) {
            System.err.println("Error al verificar curso: " + e.getMessage());
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getEjecucionById(Integer idEjecucion) {
        try {
            return restTemplate.getForObject(ejecucionServiceUrl + "/api/ejecuciones/" + idEjecucion, Map.class);
        } catch (Exception e) {
            System.err.println("Error al obtener ejecución: " + e.getMessage());
            return null;
        }
    }
    
    public boolean existeEjecucion(Integer idEjecucion) {
        try {
            Map<String, Object> ejecucion = getEjecucionById(idEjecucion);
            return ejecucion != null;
        } catch (Exception e) {
            System.err.println("Error al verificar ejecución: " + e.getMessage());
            return false;
        }
    }
    
    public boolean estudianteInscrito(String rutEstudiante, Integer idEjecucion) {
        try {
            Boolean inscrito = restTemplate.getForObject(
                ejecucionServiceUrl + "/api/inscripciones/verificar/" + rutEstudiante + "/" + idEjecucion, 
                Boolean.class);
            return inscrito != null && inscrito;
        } catch (Exception e) {
            System.err.println("Error al verificar inscripción: " + e.getMessage());
            return false;
        }
    }
}
