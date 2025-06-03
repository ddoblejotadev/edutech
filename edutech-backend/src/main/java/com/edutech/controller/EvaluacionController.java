package com.edutech.controller;

import com.edutech.model.Evaluacion;
import com.edutech.service.EvaluacionService;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EvaluacionController {
    
    private final EvaluacionService evaluacionService;
    
    /**
     * Obtener todas las evaluaciones con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Evaluacion>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/evaluaciones - Obteniendo evaluaciones con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Evaluacion> evaluaciones = evaluacionService.obtenerTodas(pageable);
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/evaluaciones/{} - Obteniendo evaluación por ID", id);
        return evaluacionService.obtenerPorId(id)
                .map(evaluacion -> ResponseEntity.ok(evaluacion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva evaluación
     */
    @PostMapping
    public ResponseEntity<Evaluacion> crear(@Valid @RequestBody Evaluacion evaluacion) {
        log.debug("POST /api/evaluaciones - Creando nueva evaluación: {}", evaluacion.getTitulo());
        try {
            Evaluacion evaluacionCreada = evaluacionService.crear(evaluacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionCreada);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear evaluación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar evaluación existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizar(@PathVariable Long id, @Valid @RequestBody Evaluacion evaluacion) {
        log.debug("PUT /api/evaluaciones/{} - Actualizando evaluación", id);
        try {
            Evaluacion evaluacionActualizada = evaluacionService.actualizar(id, evaluacion);
            return ResponseEntity.ok(evaluacionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar evaluación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar evaluación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/evaluaciones/{} - Eliminando evaluación", id);
        try {
            evaluacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar evaluación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener evaluaciones por ejecución
     */
    @GetMapping("/ejecucion/{ejecucionId}")
    public ResponseEntity<List<Evaluacion>> obtenerPorEjecucion(@PathVariable Long ejecucionId) {
        log.debug("GET /api/evaluaciones/ejecucion/{} - Obteniendo evaluaciones por ejecución", ejecucionId);
        List<Evaluacion> evaluaciones = evaluacionService.obtenerPorEjecucion(ejecucionId);
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluaciones por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Evaluacion>> obtenerPorTipo(@PathVariable String tipo) {
        log.debug("GET /api/evaluaciones/tipo/{} - Obteniendo evaluaciones por tipo", tipo);
        List<Evaluacion> evaluaciones = evaluacionService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Buscar evaluaciones por título
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Evaluacion>> buscarPorTitulo(@RequestParam String titulo) {
        log.debug("GET /api/evaluaciones/buscar?titulo={} - Buscando evaluaciones por título", titulo);
        List<Evaluacion> evaluaciones = evaluacionService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluaciones activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<Evaluacion>> obtenerActivas() {
        log.debug("GET /api/evaluaciones/activas - Obteniendo evaluaciones activas");
        List<Evaluacion> evaluaciones = evaluacionService.obtenerActivas();
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluaciones por rango de fechas
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<Evaluacion>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.debug("GET /api/evaluaciones/fechas?fechaInicio={}&fechaFin={} - Obteniendo evaluaciones por rango de fechas", fechaInicio, fechaFin);
        List<Evaluacion> evaluaciones = evaluacionService.obtenerPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluaciones programadas
     */
    @GetMapping("/programadas")
    public ResponseEntity<List<Evaluacion>> obtenerProgramadas() {
        log.debug("GET /api/evaluaciones/programadas - Obteniendo evaluaciones programadas");
        List<Evaluacion> evaluaciones = evaluacionService.obtenerProgramadas();
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluaciones en progreso
     */
    @GetMapping("/en-progreso")
    public ResponseEntity<List<Evaluacion>> obtenerEnProgreso() {
        log.debug("GET /api/evaluaciones/en-progreso - Obteniendo evaluaciones en progreso");
        List<Evaluacion> evaluaciones = evaluacionService.obtenerEnProgreso();
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluaciones finalizadas
     */
    @GetMapping("/finalizadas")
    public ResponseEntity<List<Evaluacion>> obtenerFinalizadas() {
        log.debug("GET /api/evaluaciones/finalizadas - Obteniendo evaluaciones finalizadas");
        List<Evaluacion> evaluaciones = evaluacionService.obtenerFinalizadas();
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Obtener evaluaciones con tiempo límite
     */
    @GetMapping("/con-tiempo-limite")
    public ResponseEntity<List<Evaluacion>> obtenerConTiempoLimite() {
        log.debug("GET /api/evaluaciones/con-tiempo-limite - Obteniendo evaluaciones con tiempo límite");
        List<Evaluacion> evaluaciones = evaluacionService.obtenerConTiempoLimite();
        return ResponseEntity.ok(evaluaciones);
    }
    
    /**
     * Contar evaluaciones por ejecución
     */
    @GetMapping("/count/ejecucion/{ejecucionId}")
    public ResponseEntity<Long> contarPorEjecucion(@PathVariable Long ejecucionId) {
        log.debug("GET /api/evaluaciones/count/ejecucion/{} - Contando evaluaciones por ejecución", ejecucionId);
        Long count = evaluacionService.contarPorEjecucion(ejecucionId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar evaluaciones por tipo
     */
    @GetMapping("/count/tipo/{tipo}")
    public ResponseEntity<Long> contarPorTipo(@PathVariable String tipo) {
        log.debug("GET /api/evaluaciones/count/tipo/{} - Contando evaluaciones por tipo", tipo);
        Long count = evaluacionService.contarPorTipo(tipo);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Verificar si una evaluación está disponible
     */
    @GetMapping("/{id}/disponible")
    public ResponseEntity<Boolean> estaDisponible(@PathVariable Long id) {
        log.debug("GET /api/evaluaciones/{}/disponible - Verificando si evaluación está disponible", id);
        boolean disponible = evaluacionService.estaDisponible(id);
        return ResponseEntity.ok(disponible);
    }
    
    /**
     * Verificar si una evaluación está en progreso
     */
    @GetMapping("/{id}/en-progreso")
    public ResponseEntity<Boolean> estaEnProgreso(@PathVariable Long id) {
        log.debug("GET /api/evaluaciones/{}/en-progreso - Verificando si evaluación está en progreso", id);
        boolean enProgreso = evaluacionService.estaEnProgreso(id);
        return ResponseEntity.ok(enProgreso);
    }
    
    /**
     * Verificar si una evaluación ha finalizado
     */
    @GetMapping("/{id}/finalizada")
    public ResponseEntity<Boolean> haFinalizado(@PathVariable Long id) {
        log.debug("GET /api/evaluaciones/{}/finalizada - Verificando si evaluación ha finalizado", id);
        boolean finalizada = evaluacionService.haFinalizado(id);
        return ResponseEntity.ok(finalizada);
    }
    
    /**
     * Calcular tiempo restante de una evaluación
     */
    @GetMapping("/{id}/tiempo-restante")
    public ResponseEntity<Long> calcularTiempoRestante(@PathVariable Long id) {
        log.debug("GET /api/evaluaciones/{}/tiempo-restante - Calculando tiempo restante", id);
        try {
            Long tiempoRestante = evaluacionService.calcularTiempoRestante(id);
            return ResponseEntity.ok(tiempoRestante);
        } catch (IllegalArgumentException e) {
            log.error("Error al calcular tiempo restante: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Activar evaluación
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<Evaluacion> activar(@PathVariable Long id) {
        log.debug("PUT /api/evaluaciones/{}/activar - Activando evaluación", id);
        try {
            Evaluacion evaluacionActivada = evaluacionService.activar(id);
            return ResponseEntity.ok(evaluacionActivada);
        } catch (IllegalArgumentException e) {
            log.error("Error al activar evaluación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Desactivar evaluación
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Evaluacion> desactivar(@PathVariable Long id) {
        log.debug("PUT /api/evaluaciones/{}/desactivar - Desactivando evaluación", id);
        try {
            Evaluacion evaluacionDesactivada = evaluacionService.desactivar(id);
            return ResponseEntity.ok(evaluacionDesactivada);
        } catch (IllegalArgumentException e) {
            log.error("Error al desactivar evaluación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
