package com.edutech.microservicio_evaluacion.controller;

import com.edutech.microservicio_evaluacion.model.Calificacion;
import com.edutech.microservicio_evaluacion.service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {
    
    private final CalificacionService calificacionService;
    
    @Autowired
    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }
    
    @GetMapping
    public ResponseEntity<List<Calificacion>> getAllCalificaciones() {
        return ResponseEntity.ok(calificacionService.getAllCalificaciones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> getCalificacionById(@PathVariable Integer id) {
        Calificacion calificacion = calificacionService.getCalificacionById(id);
        if (calificacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(calificacion);
    }
    
    @GetMapping("/evaluacion/{idEvaluacion}")
    public ResponseEntity<List<Calificacion>> getCalificacionesByEvaluacion(@PathVariable Integer idEvaluacion) {
        return ResponseEntity.ok(calificacionService.getCalificacionesByEvaluacion(idEvaluacion));
    }
    
    @GetMapping("/estudiante/{rutEstudiante}")
    public ResponseEntity<List<Calificacion>> getCalificacionesByEstudiante(@PathVariable String rutEstudiante) {
        return ResponseEntity.ok(calificacionService.getCalificacionesByEstudiante(rutEstudiante));
    }
    
    @GetMapping("/evaluacion/{idEvaluacion}/estudiante/{rutEstudiante}")
    public ResponseEntity<Calificacion> getCalificacionByEvaluacionAndEstudiante(
            @PathVariable Integer idEvaluacion, @PathVariable String rutEstudiante) {
        Calificacion calificacion = calificacionService.getCalificacionByEvaluacionAndEstudiante(idEvaluacion, rutEstudiante);
        if (calificacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(calificacion);
    }
    
    @GetMapping("/evaluacion/{idEvaluacion}/promedio")
    public ResponseEntity<Double> getPromedioPorEvaluacion(@PathVariable Integer idEvaluacion) {
        return ResponseEntity.ok(calificacionService.calcularPromedioPorEvaluacion(idEvaluacion));
    }
    
    @GetMapping("/estudiante/{rutEstudiante}/promedio")
    public ResponseEntity<Double> getPromedioPorEstudiante(@PathVariable String rutEstudiante) {
        return ResponseEntity.ok(calificacionService.calcularPromedioPorEstudiante(rutEstudiante));
    }
    
    @PostMapping
    public ResponseEntity<Calificacion> registrarCalificacion(@RequestBody Calificacion calificacion) {
        Calificacion nuevaCalificacion = calificacionService.registrarCalificacion(calificacion);
        if (nuevaCalificacion == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizarCalificacion(@PathVariable Integer id, @RequestBody Calificacion calificacion) {
        Calificacion calificacionActualizada = calificacionService.actualizarCalificacion(id, calificacion);
        if (calificacionActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(calificacionActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCalificacion(@PathVariable Integer id) {
        if (calificacionService.eliminarCalificacion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
