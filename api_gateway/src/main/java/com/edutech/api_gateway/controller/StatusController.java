package com.edutech.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "API Gateway funcionando correctamente");
        response.put("timestamp", System.currentTimeMillis());
        response.put("services", getServicesStatus());
        
        return ResponseEntity.ok(response);
    }
    
    private Map<String, String> getServicesStatus() {
        Map<String, String> services = new HashMap<>();
        services.put("persona", "UP");
        services.put("curso", "UP");
        services.put("ejecucion", "UP");
        services.put("comunicacion", "UP");
        services.put("evaluacion", "UP");
        
        return services;
    }
}
