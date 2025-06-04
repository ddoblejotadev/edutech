package com.edutech.controller;

//Importaciones Modelo y Service
import com.edutech.model.Evaluacion;
import com.edutech.service.EvaluacionService;

//Importacion dependencias
import org.springframework.beans.factory.annotation.Autowired;

//Importaciones respuestas HTTP
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//Importaciones Controladores REST
import org.springframework.web.bind.annotation.*;

//Importaciones Java
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@CrossOrigin(origins = "*")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;
    
    @GetMapping
    public ResponseEntity<List<Evaluacion>> obtenerTodas() {
        List<Evaluacion> evaluaciones = evaluacionService.obtenerTodas();
        return ResponseEntity.ok(evaluaciones);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> obtenerPorId(@PathVariable Long id) {
        return evaluacionService.obtenerPorId(id)
                .map(evaluacion -> ResponseEntity.ok(evaluacion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Evaluacion> crear(@RequestBody Evaluacion evaluacion) {
        try {
            Evaluacion nuevaEvaluacion = evaluacionService.crear(evaluacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEvaluacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizar(@PathVariable Long id, @RequestBody Evaluacion evaluacion) {
        try {
            Evaluacion evaluacionActualizada = evaluacionService.actualizar(id, evaluacion);
            if (evaluacionActualizada != null) {
                return ResponseEntity.ok(evaluacionActualizada);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            evaluacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
