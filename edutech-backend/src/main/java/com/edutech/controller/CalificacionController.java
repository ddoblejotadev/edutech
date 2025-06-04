package com.edutech.controller;

//Importaciones Modelo y Service
import com.edutech.model.Calificacion;
import com.edutech.service.CalificacionService;

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
@RequestMapping("/api/calificaciones")
@CrossOrigin(origins = "*")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;
    
    /**
     * Obtener todas las calificaciones
     */
    @GetMapping
    public ResponseEntity<List<Calificacion>> obtenerTodas() {
        List<Calificacion> calificaciones = calificacionService.obtenerTodas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerPorId(@PathVariable Long id) {
        return calificacionService.obtenerPorId(id)
                .map(calificacion -> ResponseEntity.ok(calificacion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Registrar nueva calificación
     */
    @PostMapping
    public ResponseEntity<Calificacion> registrar(@RequestBody Calificacion calificacion) {
        try {
            Calificacion nuevaCalificacion = calificacionService.registrar(calificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCalificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar calificación existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizar(@PathVariable Long id, 
                                                  @RequestBody Calificacion calificacion) {
        try {
            Calificacion calificacionActualizada = calificacionService.actualizar(id, calificacion);
            return ResponseEntity.ok(calificacionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar calificación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            calificacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener calificaciones por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Calificacion>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        List<Calificacion> calificaciones = calificacionService.obtenerPorEstudiante(estudianteId);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones por evaluación
     */
    @GetMapping("/evaluacion/{evaluacionId}")
    public ResponseEntity<List<Calificacion>> obtenerPorEvaluacion(@PathVariable Long evaluacionId) {
        List<Calificacion> calificaciones = calificacionService.obtenerPorEvaluacion(evaluacionId);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificación específica de un estudiante en una evaluación
     */
    @GetMapping("/estudiante/{estudianteId}/evaluacion/{evaluacionId}")
    public ResponseEntity<Calificacion> obtenerCalificacionEspecifica(
            @PathVariable Long estudianteId, 
            @PathVariable Long evaluacionId) {
        return calificacionService.obtenerCalificacionEspecifica(estudianteId, evaluacionId)
                .map(calificacion -> ResponseEntity.ok(calificacion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener calificaciones por rango de puntaje
     */
    @GetMapping("/puntaje")
    public ResponseEntity<List<Calificacion>> obtenerPorRangoPuntaje(
            @RequestParam Double puntajeMin,
            @RequestParam Double puntajeMax) {
        List<Calificacion> calificaciones = calificacionService.obtenerPorRangoPuntaje(puntajeMin, puntajeMax);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones aprobadas
     */
    @GetMapping("/aprobadas")
    public ResponseEntity<List<Calificacion>> obtenerAprobadas() {
        List<Calificacion> calificaciones = calificacionService.obtenerAprobadas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones reprobadas
     */
    @GetMapping("/reprobadas")
    public ResponseEntity<List<Calificacion>> obtenerReprobadas() {
        List<Calificacion> calificaciones = calificacionService.obtenerReprobadas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Calcular promedio por evaluación
     */
    @GetMapping("/evaluacion/{evaluacionId}/promedio")
    public ResponseEntity<Double> calcularPromedioEvaluacion(@PathVariable Long evaluacionId) {
        Double promedio = calificacionService.calcularPromedioEvaluacion(evaluacionId);
        return ResponseEntity.ok(promedio != null ? promedio : 0.0);
    }
    
    /**
     * Calcular promedio por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}/promedio")
    public ResponseEntity<Double> calcularPromedioEstudiante(@PathVariable Long estudianteId) {
        Double promedio = calificacionService.calcularPromedioEstudiante(estudianteId);
        return ResponseEntity.ok(promedio != null ? promedio : 0.0);
    }
    
    /**
     * Verificar si estudiante ya tiene calificación en evaluación
     */
    @GetMapping("/existe/estudiante/{estudianteId}/evaluacion/{evaluacionId}")
    public ResponseEntity<Boolean> existeCalificacion(
            @PathVariable Long estudianteId, 
            @PathVariable Long evaluacionId) {
        boolean existe = calificacionService.existeCalificacion(estudianteId, evaluacionId);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Calcular porcentaje obtenido
     */
    @GetMapping("/{id}/porcentaje")
    public ResponseEntity<Double> calcularPorcentaje(@PathVariable Long id) {
        try {
            Double porcentaje = calificacionService.calcularPorcentaje(id);
            return ResponseEntity.ok(porcentaje);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verificar si calificación está aprobada
     */
    @GetMapping("/{id}/aprobada")
    public ResponseEntity<Boolean> estaAprobada(@PathVariable Long id) {
        try {
            boolean aprobada = calificacionService.estaAprobada(id);
            return ResponseEntity.ok(aprobada);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}