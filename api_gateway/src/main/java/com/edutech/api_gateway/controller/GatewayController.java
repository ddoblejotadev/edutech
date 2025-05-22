package com.edutech.api_gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;

@RestController
public class GatewayController {

    @Autowired
    private RestTemplate restTemplate;

    // Método genérico para manejar todas las solicitudes GET
    @GetMapping({"/api/personas/**", "/api/cursos/**", "/api/ejecuciones/**", 
                "/api/inscripciones/**", "/api/mensajes/**", "/api/notificaciones/**",
                "/api/evaluaciones/**", "/api/preguntas/**", "/api/opciones/**", 
                "/api/calificaciones/**", "/api/respuestas/**"})
    public ResponseEntity<Object> handleGetRequest(HttpServletRequest request) {
        return forwardRequest(request, HttpMethod.GET, null);
    }

    // Método genérico para manejar todas las solicitudes POST
    @PostMapping({"/api/personas/**", "/api/cursos/**", "/api/ejecuciones/**", 
                 "/api/inscripciones/**", "/api/mensajes/**", "/api/notificaciones/**",
                 "/api/evaluaciones/**", "/api/preguntas/**", "/api/opciones/**", 
                 "/api/calificaciones/**", "/api/respuestas/**"})
    public ResponseEntity<Object> handlePostRequest(HttpServletRequest request, @RequestBody(required = false) Object body) {
        return forwardRequest(request, HttpMethod.POST, body);
    }

    // Método genérico para manejar todas las solicitudes PUT
    @PutMapping({"/api/personas/**", "/api/cursos/**", "/api/ejecuciones/**", 
                "/api/inscripciones/**", "/api/mensajes/**", "/api/notificaciones/**",
                "/api/evaluaciones/**", "/api/preguntas/**", "/api/opciones/**", 
                "/api/calificaciones/**", "/api/respuestas/**"})
    public ResponseEntity<Object> handlePutRequest(HttpServletRequest request, @RequestBody(required = false) Object body) {
        return forwardRequest(request, HttpMethod.PUT, body);
    }

    // Método genérico para manejar todas las solicitudes DELETE
    @DeleteMapping({"/api/personas/**", "/api/cursos/**", "/api/ejecuciones/**", 
                   "/api/inscripciones/**", "/api/mensajes/**", "/api/notificaciones/**",
                   "/api/evaluaciones/**", "/api/preguntas/**", "/api/opciones/**", 
                   "/api/calificaciones/**", "/api/respuestas/**"})
    public ResponseEntity<Object> handleDeleteRequest(HttpServletRequest request) {
        return forwardRequest(request, HttpMethod.DELETE, null);
    }

    // Método central para reenviar solicitudes
    private ResponseEntity<Object> forwardRequest(HttpServletRequest request, HttpMethod method, Object body) {
        try {
            // Determinar el host destino según la ruta
            String targetHost = getTargetHost(request.getRequestURI());
            
            // Construir la URL completa
            String url = targetHost + request.getRequestURI();
            
            // Copiar los headers
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.set(headerName, request.getHeader(headerName));
            }
            
            // Crear la entidad con el cuerpo si existe
            HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);
            
            // Ejecutar la solicitud y retornar la respuesta
            return restTemplate.exchange(url, method, httpEntity, Object.class);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al reenviar la solicitud: " + e.getMessage());
        }
    }
    
    // Determinar el host destino según la ruta
    private String getTargetHost(String uri) {
        if (uri.startsWith("/api/personas")) {
            return "http://localhost:8081";
        } else if (uri.startsWith("/api/cursos")) {
            return "http://localhost:8082";
        } else if (uri.startsWith("/api/ejecuciones") || uri.startsWith("/api/inscripciones")) {
            return "http://localhost:8084";
        } else if (uri.startsWith("/api/mensajes") || uri.startsWith("/api/notificaciones")) {
            return "http://localhost:8085";
        } else if (uri.startsWith("/api/evaluaciones") || uri.startsWith("/api/preguntas") || 
                  uri.startsWith("/api/opciones") || uri.startsWith("/api/calificaciones") || 
                  uri.startsWith("/api/respuestas")) {
            return "http://localhost:8086";
        }
        return "http://localhost:8080"; // fallback
    }
}