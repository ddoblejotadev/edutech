package com.edutech.controller;

import com.edutech.model.Calificacion;
import com.edutech.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CalificacionController {
    
    private final CalificacionService calificacionService;
    
    /**
     * Obtener todas las calificaciones
     */
    @GetMapping
    public ResponseEntity<List<Calificacion>> obtenerTodas() {
        log.info("GET /api/calificaciones - Obteniendo todas las calificaciones");
        List<Calificacion> calificaciones = calificacionService.obtenerTodas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/calificaciones/{} - Obteniendo calificación por ID", id);
        return calificacionService.obtenerPorId(id)
                .map(calificacion -> ResponseEntity.ok(calificacion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Registrar nueva calificación
     */
    @PostMapping
    public ResponseEntity<Calificacion> registrar(@Valid @RequestBody Calificacion calificacion) {
        log.info("POST /api/calificaciones - Registrando nueva calificación");
        try {
            Calificacion nuevaCalificacion = calificacionService.registrar(calificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCalificacion);
        } catch (IllegalArgumentException e) {
            log.error("Error al registrar calificación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar calificación existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizar(@PathVariable Long id, 
                                                  @Valid @RequestBody Calificacion calificacion) {
        log.info("PUT /api/calificaciones/{} - Actualizando calificación", id);
        try {
            Calificacion calificacionActualizada = calificacionService.actualizar(id, calificacion);
            return ResponseEntity.ok(calificacionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar calificación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar calificación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/calificaciones/{} - Eliminando calificación", id);
        try {
            calificacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar calificación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener calificaciones por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Calificacion>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/calificaciones/estudiante/{} - Obteniendo calificaciones por estudiante", estudianteId);
        List<Calificacion> calificaciones = calificacionService.obtenerPorEstudiante(estudianteId);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones por evaluación
     */
    @GetMapping("/evaluacion/{evaluacionId}")
    public ResponseEntity<List<Calificacion>> obtenerPorEvaluacion(@PathVariable Long evaluacionId) {
        log.info("GET /api/calificaciones/evaluacion/{} - Obteniendo calificaciones por evaluación", evaluacionId);
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
        log.info("GET /api/calificaciones/estudiante/{}/evaluacion/{} - Obteniendo calificación específica", 
                estudianteId, evaluacionId);
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
        log.info("GET /api/calificaciones/puntaje - Obteniendo calificaciones por rango de puntaje: {} - {}", 
                puntajeMin, puntajeMax);
        List<Calificacion> calificaciones = calificacionService.obtenerPorRangoPuntaje(puntajeMin, puntajeMax);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones aprobadas
     */
    @GetMapping("/aprobadas")
    public ResponseEntity<List<Calificacion>> obtenerAprobadas() {
        log.info("GET /api/calificaciones/aprobadas - Obteniendo calificaciones aprobadas");
        List<Calificacion> calificaciones = calificacionService.obtenerAprobadas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones reprobadas
     */
    @GetMapping("/reprobadas")
    public ResponseEntity<List<Calificacion>> obtenerReprobadas() {
        log.info("GET /api/calificaciones/reprobadas - Obteniendo calificaciones reprobadas");
        List<Calificacion> calificaciones = calificacionService.obtenerReprobadas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Calcular promedio por evaluación
     */
    @GetMapping("/evaluacion/{evaluacionId}/promedio")
    public ResponseEntity<Double> calcularPromedioEvaluacion(@PathVariable Long evaluacionId) {
        log.info("GET /api/calificaciones/evaluacion/{}/promedio - Calculando promedio de evaluación", evaluacionId);
        Double promedio = calificacionService.calcularPromedioEvaluacion(evaluacionId);
        return ResponseEntity.ok(promedio != null ? promedio : 0.0);
    }
    
    /**
     * Calcular promedio por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}/promedio")
    public ResponseEntity<Double> calcularPromedioEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/calificaciones/estudiante/{}/promedio - Calculando promedio de estudiante", estudianteId);
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
        log.info("GET /api/calificaciones/existe/estudiante/{}/evaluacion/{} - Verificando existencia de calificación", 
                estudianteId, evaluacionId);
        boolean existe = calificacionService.existeCalificacion(estudianteId, evaluacionId);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Calcular porcentaje obtenido
     */
    @GetMapping("/{id}/porcentaje")
    public ResponseEntity<Double> calcularPorcentaje(@PathVariable Long id) {
        log.info("GET /api/calificaciones/{}/porcentaje - Calculando porcentaje obtenido", id);
        try {
            Double porcentaje = calificacionService.calcularPorcentaje(id);
            return ResponseEntity.ok(porcentaje);
        } catch (Exception e) {
            log.error("Error al calcular porcentaje: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verificar si calificación está aprobada
     */
    @GetMapping("/{id}/aprobada")
    public ResponseEntity<Boolean> estaAprobada(@PathVariable Long id) {
        log.info("GET /api/calificaciones/{}/aprobada - Verificando si calificación está aprobada", id);
        try {
            boolean aprobada = calificacionService.estaAprobada(id);
            return ResponseEntity.ok(aprobada);
        } catch (Exception e) {
            log.error("Error al verificar aprobación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}