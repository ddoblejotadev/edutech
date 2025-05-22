package com.edutech.microservicio_evaluacion.controller;

import com.edutech.microservicio_evaluacion.model.Pregunta;
import com.edutech.microservicio_evaluacion.service.PreguntaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
public class PreguntaController {
    
    private final PreguntaService preguntaService;
    
    public PreguntaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }
    
    @GetMapping
    public ResponseEntity<List<Pregunta>> getAllPreguntas() {
        return ResponseEntity.ok(preguntaService.getAllPreguntas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pregunta> getPreguntaById(@PathVariable Integer id) {
        Pregunta pregunta = preguntaService.getPreguntaById(id);
        if (pregunta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pregunta);
    }
    
    @GetMapping("/evaluacion/{idEvaluacion}")
    public ResponseEntity<List<Pregunta>> getPreguntasByEvaluacion(@PathVariable Integer idEvaluacion) {
        return ResponseEntity.ok(preguntaService.getPreguntasByEvaluacion(idEvaluacion));
    }
    
    @PostMapping
    public ResponseEntity<Pregunta> crearPregunta(@RequestBody Pregunta pregunta) {
        Pregunta nuevaPregunta = preguntaService.crearPregunta(pregunta);
        if (nuevaPregunta == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(nuevaPregunta, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pregunta> actualizarPregunta(@PathVariable Integer id, @RequestBody Pregunta pregunta) {
        Pregunta preguntaActualizada = preguntaService.actualizarPregunta(id, pregunta);
        if (preguntaActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(preguntaActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPregunta(@PathVariable Integer id) {
        if (preguntaService.eliminarPregunta(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
