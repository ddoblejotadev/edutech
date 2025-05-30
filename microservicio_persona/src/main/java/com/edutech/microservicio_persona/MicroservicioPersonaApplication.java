package com.edutech.microservicio_persona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class MicroservicioPersonaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioPersonaApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Microservicio Persona está funcionando!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
