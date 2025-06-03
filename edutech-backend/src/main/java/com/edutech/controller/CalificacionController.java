package com.edutech.controller;

import com.edutech.model.Calificacion;
import com.edutech.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CalificacionController {
    
    private final CalificacionService calificacionService;
    
    /**
     * Obtener todas las calificaciones con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Calificacion>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/calificaciones - Obteniendo calificaciones con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Calificacion> calificaciones = calificacionService.obtenerTodas(pageable);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/calificaciones/{} - Obteniendo calificación por ID", id);
        return calificacionService.obtenerPorId(id)
                .map(calificacion -> ResponseEntity.ok(calificacion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva calificación
     */
    @PostMapping
    public ResponseEntity<Calificacion> crear(@Valid @RequestBody Calificacion calificacion) {
        log.debug("POST /api/calificaciones - Creando nueva calificación");
        try {
            Calificacion calificacionCreada = calificacionService.crear(calificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(calificacionCreada);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear calificación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar calificación existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizar(@PathVariable Long id, @Valid @RequestBody Calificacion calificacion) {
        log.debug("PUT /api/calificaciones/{} - Actualizando calificación", id);
        try {
            Calificacion calificacionActualizada = calificacionService.actualizar(id, calificacion);
            return ResponseEntity.ok(calificacionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar calificación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar calificación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/calificaciones/{} - Eliminando calificación", id);
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
        log.debug("GET /api/calificaciones/estudiante/{} - Obteniendo calificaciones por estudiante", estudianteId);
        List<Calificacion> calificaciones = calificacionService.obtenerPorEstudiante(estudianteId);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones por evaluación
     */
    @GetMapping("/evaluacion/{evaluacionId}")
    public ResponseEntity<List<Calificacion>> obtenerPorEvaluacion(@PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/evaluacion/{} - Obteniendo calificaciones por evaluación", evaluacionId);
        List<Calificacion> calificaciones = calificacionService.obtenerPorEvaluacion(evaluacionId);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones por ejecución
     */
    @GetMapping("/ejecucion/{ejecucionId}")
    public ResponseEntity<List<Calificacion>> obtenerPorEjecucion(@PathVariable Long ejecucionId) {
        log.debug("GET /api/calificaciones/ejecucion/{} - Obteniendo calificaciones por ejecución", ejecucionId);
        List<Calificacion> calificaciones = calificacionService.obtenerPorEjecucion(ejecucionId);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones aprobadas
     */
    @GetMapping("/aprobadas")
    public ResponseEntity<List<Calificacion>> obtenerAprobadas() {
        log.debug("GET /api/calificaciones/aprobadas - Obteniendo calificaciones aprobadas");
        List<Calificacion> calificaciones = calificacionService.obtenerAprobadas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones reprobadas
     */
    @GetMapping("/reprobadas")
    public ResponseEntity<List<Calificacion>> obtenerReprobadas() {
        log.debug("GET /api/calificaciones/reprobadas - Obteniendo calificaciones reprobadas");
        List<Calificacion> calificaciones = calificacionService.obtenerReprobadas();
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones por rango de nota
     */
    @GetMapping("/rango-nota")
    public ResponseEntity<List<Calificacion>> obtenerPorRangoNota(
            @RequestParam Double notaMinima,
            @RequestParam Double notaMaxima) {
        log.debug("GET /api/calificaciones/rango-nota?notaMinima={}&notaMaxima={} - Obteniendo calificaciones por rango de nota", notaMinima, notaMaxima);
        List<Calificacion> calificaciones = calificacionService.obtenerPorRangoNota(notaMinima, notaMaxima);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Obtener calificaciones por rango de fechas
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<Calificacion>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        log.debug("GET /api/calificaciones/fechas?fechaInicio={}&fechaFin={} - Obteniendo calificaciones por rango de fechas", fechaInicio, fechaFin);
        List<Calificacion> calificaciones = calificacionService.obtenerPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(calificaciones);
    }
    
    /**
     * Verificar si un estudiante tiene calificación para una evaluación
     */
    @GetMapping("/existe/estudiante/{estudianteId}/evaluacion/{evaluacionId}")
    public ResponseEntity<Boolean> tieneCalificacion(@PathVariable Long estudianteId, @PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/existe/estudiante/{}/evaluacion/{} - Verificando calificación", estudianteId, evaluacionId);
        boolean tiene = calificacionService.tieneCalificacion(estudianteId, evaluacionId);
        return ResponseEntity.ok(tiene);
    }
    
    /**
     * Obtener calificación específica de estudiante en evaluación
     */
    @GetMapping("/estudiante/{estudianteId}/evaluacion/{evaluacionId}")
    public ResponseEntity<Calificacion> obtenerCalificacion(@PathVariable Long estudianteId, @PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/estudiante/{}/evaluacion/{} - Obteniendo calificación específica", estudianteId, evaluacionId);
        return calificacionService.obtenerCalificacion(estudianteId, evaluacionId)
                .map(calificacion -> ResponseEntity.ok(calificacion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Calcular promedio de un estudiante
     */
    @GetMapping("/promedio/estudiante/{estudianteId}")
    public ResponseEntity<Double> calcularPromedioEstudiante(@PathVariable Long estudianteId) {
        log.debug("GET /api/calificaciones/promedio/estudiante/{} - Calculando promedio del estudiante", estudianteId);
        Double promedio = calificacionService.calcularPromedioEstudiante(estudianteId);
        return ResponseEntity.ok(promedio);
    }
    
    /**
     * Calcular promedio de una evaluación
     */
    @GetMapping("/promedio/evaluacion/{evaluacionId}")
    public ResponseEntity<Double> calcularPromedioEvaluacion(@PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/promedio/evaluacion/{} - Calculando promedio de la evaluación", evaluacionId);
        Double promedio = calificacionService.calcularPromedioEvaluacion(evaluacionId);
        return ResponseEntity.ok(promedio);
    }
    
    /**
     * Calcular promedio de una ejecución
     */
    @GetMapping("/promedio/ejecucion/{ejecucionId}")
    public ResponseEntity<Double> calcularPromedioEjecucion(@PathVariable Long ejecucionId) {
        log.debug("GET /api/calificaciones/promedio/ejecucion/{} - Calculando promedio de la ejecución", ejecucionId);
        Double promedio = calificacionService.calcularPromedioEjecucion(ejecucionId);
        return ResponseEntity.ok(promedio);
    }
    
    /**
     * Contar calificaciones por estudiante
     */
    @GetMapping("/count/estudiante/{estudianteId}")
    public ResponseEntity<Long> contarPorEstudiante(@PathVariable Long estudianteId) {
        log.debug("GET /api/calificaciones/count/estudiante/{} - Contando calificaciones por estudiante", estudianteId);
        Long count = calificacionService.contarPorEstudiante(estudianteId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar calificaciones por evaluación
     */
    @GetMapping("/count/evaluacion/{evaluacionId}")
    public ResponseEntity<Long> contarPorEvaluacion(@PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/count/evaluacion/{} - Contando calificaciones por evaluación", evaluacionId);
        Long count = calificacionService.contarPorEvaluacion(evaluacionId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar calificaciones aprobadas
     */
    @GetMapping("/count/aprobadas")
    public ResponseEntity<Long> contarAprobadas() {
        log.debug("GET /api/calificaciones/count/aprobadas - Contando calificaciones aprobadas");
        Long count = calificacionService.contarAprobadas();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar calificaciones reprobadas
     */
    @GetMapping("/count/reprobadas")
    public ResponseEntity<Long> contarReprobadas() {
        log.debug("GET /api/calificaciones/count/reprobadas - Contando calificaciones reprobadas");
        Long count = calificacionService.contarReprobadas();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Calcular porcentaje de aprobación de una evaluación
     */
    @GetMapping("/aprobacion/evaluacion/{evaluacionId}")
    public ResponseEntity<Double> calcularPorcentajeAprobacion(@PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/aprobacion/evaluacion/{} - Calculando porcentaje de aprobación", evaluacionId);
        Double porcentaje = calificacionService.calcularPorcentajeAprobacion(evaluacionId);
        return ResponseEntity.ok(porcentaje);
    }
    
    /**
     * Obtener estadísticas de calificaciones por evaluación
     */
    @GetMapping("/estadisticas/evaluacion/{evaluacionId}")
    public ResponseEntity<Object> obtenerEstadisticasEvaluacion(@PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/estadisticas/evaluacion/{} - Obteniendo estadísticas de evaluación", evaluacionId);
        try {
            Object estadisticas = calificacionService.obtenerEstadisticasEvaluacion(evaluacionId);
            return ResponseEntity.ok(estadisticas);
        } catch (IllegalArgumentException e) {
            log.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener estadísticas de calificaciones por estudiante
     */
    @GetMapping("/estadisticas/estudiante/{estudianteId}")
    public ResponseEntity<Object> obtenerEstadisticasEstudiante(@PathVariable Long estudianteId) {
        log.debug("GET /api/calificaciones/estadisticas/estudiante/{} - Obteniendo estadísticas de estudiante", estudianteId);
        try {
            Object estadisticas = calificacionService.obtenerEstadisticasEstudiante(estudianteId);
            return ResponseEntity.ok(estadisticas);
        } catch (IllegalArgumentException e) {
            log.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verificar si un estudiante aprobó una evaluación
     */
    @GetMapping("/aprobo/estudiante/{estudianteId}/evaluacion/{evaluacionId}")
    public ResponseEntity<Boolean> aprobo(@PathVariable Long estudianteId, @PathVariable Long evaluacionId) {
        log.debug("GET /api/calificaciones/aprobo/estudiante/{}/evaluacion/{} - Verificando aprobación", estudianteId, evaluacionId);
        boolean aprobo = calificacionService.aprobo(estudianteId, evaluacionId);
        return ResponseEntity.ok(aprobo);
    }
    
    /**
     * Obtener ranking de estudiantes por promedio
     */
    @GetMapping("/ranking/estudiantes")
    public ResponseEntity<List<Object>> obtenerRankingEstudiantes() {
        log.debug("GET /api/calificaciones/ranking/estudiantes - Obteniendo ranking de estudiantes");
        List<Object> ranking = calificacionService.obtenerRankingEstudiantes();
        return ResponseEntity.ok(ranking);
    }
    
    /**
     * Obtener mejores calificaciones (top N)
     */
    @GetMapping("/mejores")
    public ResponseEntity<List<Calificacion>> obtenerMejoresCalificaciones(
            @RequestParam(defaultValue = "10") int limite) {
        log.debug("GET /api/calificaciones/mejores?limite={} - Obteniendo mejores calificaciones", limite);
        List<Calificacion> mejores = calificacionService.obtenerMejoresCalificaciones(limite);
        return ResponseEntity.ok(mejores);
    }
}
