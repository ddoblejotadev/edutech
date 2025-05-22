package com.edutech.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Simulación de autenticación (solo para pruebas)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        // Validación básica (en un entorno real esto verificaría con la base de datos)
        if (username == null || password == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // En un entorno real verificarías con la base de datos
        // Esto es solo para pruebas con la app móvil
        if (username.equals("admin") && password.equals("admin123")) {
            Map<String, Object> response = new HashMap<>();
            
            // Generar token simulado (en un entorno real usarías JWT)
            String token = UUID.randomUUID().toString();
            
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("rut", "12345678-9");
            usuario.put("nombres", "Administrador");
            usuario.put("apellidos", "Sistema");
            usuario.put("email", "admin@edutech.com");
            usuario.put("roles", Arrays.asList("ADMIN"));
            
            response.put("token", token);
            response.put("usuario", usuario);
            
            return ResponseEntity.ok(response);
        } else if (username.equals("estudiante") && password.equals("estudiante123")) {
            Map<String, Object> response = new HashMap<>();
            
            String token = UUID.randomUUID().toString();
            
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("rut", "98765432-1");
            usuario.put("nombres", "Estudiante");
            usuario.put("apellidos", "Demo");
            usuario.put("email", "estudiante@example.com");
            usuario.put("roles", Arrays.asList("ESTUDIANTE"));
            
            response.put("token", token);
            response.put("usuario", usuario);
            
            return ResponseEntity.ok(response);
        }
        
        // Credenciales inválidas
        return ResponseEntity.status(401).build();
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> userData) {
        // Validación básica
        if (!userData.containsKey("rut") || !userData.containsKey("nombres") || 
            !userData.containsKey("apellidos") || !userData.containsKey("email") || 
            !userData.containsKey("password")) {
            return ResponseEntity.badRequest().build();
        }
        
        // En un entorno real esto guardaría el usuario en la base de datos
        // y ejecutaría validaciones adicionales
        
        Map<String, Object> response = new HashMap<>();
        
        // Generar token simulado
        String token = UUID.randomUUID().toString();
        
        // Devolver los datos del usuario sin la contraseña
        Map<String, Object> usuarioCreado = new HashMap<>(userData);
        usuarioCreado.remove("password");
        usuarioCreado.put("id", UUID.randomUUID().toString());
        
        response.put("token", token);
        response.put("usuario", usuarioCreado);
        
        return ResponseEntity.status(201).body(response);
    }
}
