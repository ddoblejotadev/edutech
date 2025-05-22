package com.edutech.api_gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ForwardController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public String test() {
        return "API Gateway está funcionando correctamente";
    }

    @GetMapping("/personas-test")
    public ResponseEntity<Object> testPersonasService() {
        try {
            // Intenta conectar directamente al servicio de personas
            return restTemplate.getForEntity("http://localhost:8081/api/personas", Object.class);
        } catch (Exception e) {
            // Maneja excepciones de conexión o respuesta
            return ResponseEntity.status(500).body("Error al conectar con microservicio_persona: " + e.getMessage());
        }
    }
}
