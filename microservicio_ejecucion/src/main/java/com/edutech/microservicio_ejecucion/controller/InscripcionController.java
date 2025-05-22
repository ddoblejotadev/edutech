package com.edutech.microservicio_ejecucion.controller;

import com.edutech.microservicio_ejecucion.model.EjecucionPersona;
import com.edutech.microservicio_ejecucion.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {
    
    private final InscripcionService inscripcionService;
    
    @Autowired
    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }
    
    @PostMapping
    public ResponseEntity<String> inscribirEstudiante(@RequestBody Map<String, Object> datos) {
        String rutPersona = (String) datos.get("rutPersona");
        Integer idEjecucion = Integer.parseInt(datos.get("idEjecucion").toString());
        
        if (inscripcionService.inscribirEstudiante(rutPersona, idEjecucion)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Estudiante inscrito correctamente");
        } else {
            return ResponseEntity.badRequest().body("No se pudo realizar la inscripción");
        }
    }
    
    @DeleteMapping("/{rutPersona}/{idEjecucion}")
    public ResponseEntity<String> cancelarInscripcion(@PathVariable String rutPersona, @PathVariable Integer idEjecucion) {
        if (inscripcionService.cancelarInscripcion(rutPersona, idEjecucion)) {
            return ResponseEntity.ok("Inscripción cancelada correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/persona/{rutPersona}")
    public ResponseEntity<List<EjecucionPersona>> getInscripcionesByPersona(@PathVariable String rutPersona) {
        return ResponseEntity.ok(inscripcionService.getInscripcionesByPersona(rutPersona));
    }
    
    @GetMapping("/ejecucion/{idEjecucion}")
    public ResponseEntity<List<EjecucionPersona>> getInscripcionesByEjecucion(@PathVariable Integer idEjecucion) {
        return ResponseEntity.ok(inscripcionService.getInscripcionesByEjecucion(idEjecucion));
    }
}
