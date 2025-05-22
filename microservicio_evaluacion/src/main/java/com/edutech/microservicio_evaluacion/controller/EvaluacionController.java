package com.edutech.microservicio_evaluacion.controller;

import com.edutech.microservicio_evaluacion.model.Evaluacion;
import com.edutech.microservicio_evaluacion.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {
    
    private final EvaluacionService evaluacionService;
    
    @Autowired
    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }
    
    @GetMapping
    public ResponseEntity<List<Evaluacion>> getAllEvaluaciones() {
        return ResponseEntity.ok(evaluacionService.getAllEvaluaciones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> getEvaluacionById(@PathVariable Integer id) {
        Evaluacion evaluacion = evaluacionService.getEvaluacionById(id);
        if (evaluacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evaluacion);
    }
    
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByCurso(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(evaluacionService.getEvaluacionesByCurso(idCurso));
    }
    
    @GetMapping("/ejecucion/{idEjecucion}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByEjecucion(@PathVariable Integer idEjecucion) {
        return ResponseEntity.ok(evaluacionService.getEvaluacionesByEjecucion(idEjecucion));
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(evaluacionService.getEvaluacionesByTipo(tipo));
    }
    
    @PostMapping
    public ResponseEntity<Evaluacion> crearEvaluacion(@RequestBody Evaluacion evaluacion) {
        Evaluacion nuevaEvaluacion = evaluacionService.crearEvaluacion(evaluacion);
        if (nuevaEvaluacion == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(nuevaEvaluacion, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizarEvaluacion(@PathVariable Integer id, @RequestBody Evaluacion evaluacion) {
        Evaluacion evaluacionActualizada = evaluacionService.actualizarEvaluacion(id, evaluacion);
        if (evaluacionActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evaluacionActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Integer id) {
        if (evaluacionService.eliminarEvaluacion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
