package com.edutech.controller;

import com.edutech.model.Ejecucion;
import com.edutech.service.EjecucionService;
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
@RequestMapping("/api/ejecuciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EjecucionController {
    
    private final EjecucionService ejecucionService;
    
    /**
     * Obtener todas las ejecuciones con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Ejecucion>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/ejecuciones - Obteniendo ejecuciones con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Ejecucion> ejecuciones = ejecucionService.obtenerTodas(pageable);
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Obtener ejecución por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ejecucion> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/ejecuciones/{} - Obteniendo ejecución por ID", id);
        return ejecucionService.obtenerPorId(id)
                .map(ejecucion -> ResponseEntity.ok(ejecucion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva ejecución
     */
    @PostMapping
    public ResponseEntity<Ejecucion> crear(@Valid @RequestBody Ejecucion ejecucion) {
        log.debug("POST /api/ejecuciones - Creando nueva ejecución para curso: {}", ejecucion.getCurso().getId());
        try {
            Ejecucion ejecucionCreada = ejecucionService.crear(ejecucion);
            return ResponseEntity.status(HttpStatus.CREATED).body(ejecucionCreada);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear ejecución: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar ejecución existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ejecucion> actualizar(@PathVariable Long id, @Valid @RequestBody Ejecucion ejecucion) {
        log.debug("PUT /api/ejecuciones/{} - Actualizando ejecución", id);
        try {
            Ejecucion ejecucionActualizada = ejecucionService.actualizar(id, ejecucion);
            return ResponseEntity.ok(ejecucionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar ejecución: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar ejecución
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/ejecuciones/{} - Eliminando ejecución", id);
        try {
            ejecucionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar ejecución: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener ejecuciones por curso
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Ejecucion>> obtenerPorCurso(@PathVariable Long cursoId) {
        log.debug("GET /api/ejecuciones/curso/{} - Obteniendo ejecuciones por curso", cursoId);
        List<Ejecucion> ejecuciones = ejecucionService.obtenerPorCurso(cursoId);
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Obtener ejecuciones por profesor
     */
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Ejecucion>> obtenerPorProfesor(@PathVariable Long profesorId) {
        log.debug("GET /api/ejecuciones/profesor/{} - Obteniendo ejecuciones por profesor", profesorId);
        List<Ejecucion> ejecuciones = ejecucionService.obtenerPorProfesor(profesorId);
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Obtener ejecuciones por periodo
     */
    @GetMapping("/periodo/{periodo}")
    public ResponseEntity<List<Ejecucion>> obtenerPorPeriodo(@PathVariable String periodo) {
        log.debug("GET /api/ejecuciones/periodo/{} - Obteniendo ejecuciones por periodo", periodo);
        List<Ejecucion> ejecuciones = ejecucionService.obtenerPorPeriodo(periodo);
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Obtener ejecuciones activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<Ejecucion>> obtenerActivas() {
        log.debug("GET /api/ejecuciones/activas - Obteniendo ejecuciones activas");
        List<Ejecucion> ejecuciones = ejecucionService.obtenerActivas();
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Obtener ejecuciones por rango de fechas
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<Ejecucion>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        log.debug("GET /api/ejecuciones/fechas?fechaInicio={}&fechaFin={} - Obteniendo ejecuciones por rango de fechas", fechaInicio, fechaFin);
        List<Ejecucion> ejecuciones = ejecucionService.obtenerPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Obtener ejecuciones con inscripciones abiertas
     */
    @GetMapping("/inscripciones-abiertas")
    public ResponseEntity<List<Ejecucion>> obtenerConInscripcionesAbiertas() {
        log.debug("GET /api/ejecuciones/inscripciones-abiertas - Obteniendo ejecuciones con inscripciones abiertas");
        List<Ejecucion> ejecuciones = ejecucionService.obtenerConInscripcionesAbiertas();
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Obtener ejecuciones con cupos disponibles
     */
    @GetMapping("/con-cupos")
    public ResponseEntity<List<Ejecucion>> obtenerConCuposDisponibles() {
        log.debug("GET /api/ejecuciones/con-cupos - Obteniendo ejecuciones con cupos disponibles");
        List<Ejecucion> ejecuciones = ejecucionService.obtenerConCuposDisponibles();
        return ResponseEntity.ok(ejecuciones);
    }
    
    /**
     * Contar ejecuciones por curso
     */
    @GetMapping("/count/curso/{cursoId}")
    public ResponseEntity<Long> contarPorCurso(@PathVariable Long cursoId) {
        log.debug("GET /api/ejecuciones/count/curso/{} - Contando ejecuciones por curso", cursoId);
        Long count = ejecucionService.contarPorCurso(cursoId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar ejecuciones por profesor
     */
    @GetMapping("/count/profesor/{profesorId}")
    public ResponseEntity<Long> contarPorProfesor(@PathVariable Long profesorId) {
        log.debug("GET /api/ejecuciones/count/profesor/{} - Contando ejecuciones por profesor", profesorId);
        Long count = ejecucionService.contarPorProfesor(profesorId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Verificar si hay conflicto de horario para un profesor
     */
    @GetMapping("/conflicto-horario/profesor/{profesorId}")
    public ResponseEntity<Boolean> verificarConflictoHorario(
            @PathVariable Long profesorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        log.debug("GET /api/ejecuciones/conflicto-horario/profesor/{} - Verificando conflicto de horario", profesorId);
        boolean hayConflicto = ejecucionService.verificarConflictoHorario(profesorId, fechaInicio, fechaFin);
        return ResponseEntity.ok(hayConflicto);
    }
    
    /**
     * Obtener número de inscritos en una ejecución
     */
    @GetMapping("/{id}/inscritos")
    public ResponseEntity<Integer> obtenerNumeroInscritos(@PathVariable Long id) {
        log.debug("GET /api/ejecuciones/{}/inscritos - Obteniendo número de inscritos", id);
        Integer inscritos = ejecucionService.obtenerNumeroInscritos(id);
        return ResponseEntity.ok(inscritos);
    }
    
    /**
     * Verificar si hay cupos disponibles
     */
    @GetMapping("/{id}/cupos-disponibles")
    public ResponseEntity<Boolean> tieneCuposDisponibles(@PathVariable Long id) {
        log.debug("GET /api/ejecuciones/{}/cupos-disponibles - Verificando cupos disponibles", id);
        boolean tieneCupos = ejecucionService.tieneCuposDisponibles(id);
        return ResponseEntity.ok(tieneCupos);
    }
}
