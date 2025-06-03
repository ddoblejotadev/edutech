package com.edutech.controller;

import com.edutech.model.Inscripcion;
import com.edutech.service.InscripcionService;
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
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InscripcionController {
    
    private final InscripcionService inscripcionService;
    
    /**
     * Obtener todas las inscripciones con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Inscripcion>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/inscripciones - Obteniendo inscripciones con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Inscripcion> inscripciones = inscripcionService.obtenerTodas(pageable);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/inscripciones/{} - Obteniendo inscripción por ID", id);
        return inscripcionService.obtenerPorId(id)
                .map(inscripcion -> ResponseEntity.ok(inscripcion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva inscripción
     */
    @PostMapping
    public ResponseEntity<Inscripcion> crear(@Valid @RequestBody Inscripcion inscripcion) {
        log.debug("POST /api/inscripciones - Creando nueva inscripción");
        try {
            Inscripcion inscripcionCreada = inscripcionService.crear(inscripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionCreada);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear inscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar inscripción existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Inscripcion> actualizar(@PathVariable Long id, @Valid @RequestBody Inscripcion inscripcion) {
        log.debug("PUT /api/inscripciones/{} - Actualizando inscripción", id);
        try {
            Inscripcion inscripcionActualizada = inscripcionService.actualizar(id, inscripcion);
            return ResponseEntity.ok(inscripcionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar inscripción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar inscripción
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/inscripciones/{} - Eliminando inscripción", id);
        try {
            inscripcionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar inscripción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener inscripciones por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Inscripcion>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        log.debug("GET /api/inscripciones/estudiante/{} - Obteniendo inscripciones por estudiante", estudianteId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones por ejecución
     */
    @GetMapping("/ejecucion/{ejecucionId}")
    public ResponseEntity<List<Inscripcion>> obtenerPorEjecucion(@PathVariable Long ejecucionId) {
        log.debug("GET /api/inscripciones/ejecucion/{} - Obteniendo inscripciones por ejecución", ejecucionId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorEjecucion(ejecucionId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Inscripcion>> obtenerPorEstado(@PathVariable String estado) {
        log.debug("GET /api/inscripciones/estado/{} - Obteniendo inscripciones por estado", estado);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorEstado(estado);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<Inscripcion>> obtenerActivas() {
        log.debug("GET /api/inscripciones/activas - Obteniendo inscripciones activas");
        List<Inscripcion> inscripciones = inscripcionService.obtenerActivas();
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones por rango de fechas
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<Inscripcion>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        log.debug("GET /api/inscripciones/fechas?fechaInicio={}&fechaFin={} - Obteniendo inscripciones por rango de fechas", fechaInicio, fechaFin);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones de un estudiante en un curso específico
     */
    @GetMapping("/estudiante/{estudianteId}/curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> obtenerPorEstudianteYCurso(
            @PathVariable Long estudianteId,
            @PathVariable Long cursoId) {
        log.debug("GET /api/inscripciones/estudiante/{}/curso/{} - Obteniendo inscripciones por estudiante y curso", estudianteId, cursoId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorEstudianteYCurso(estudianteId, cursoId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Verificar si un estudiante está inscrito en una ejecución
     */
    @GetMapping("/existe/estudiante/{estudianteId}/ejecucion/{ejecucionId}")
    public ResponseEntity<Boolean> estaInscrito(@PathVariable Long estudianteId, @PathVariable Long ejecucionId) {
        log.debug("GET /api/inscripciones/existe/estudiante/{}/ejecucion/{} - Verificando inscripción", estudianteId, ejecucionId);
        boolean inscrito = inscripcionService.estaInscrito(estudianteId, ejecucionId);
        return ResponseEntity.ok(inscrito);
    }
    
    /**
     * Contar inscripciones por ejecución
     */
    @GetMapping("/count/ejecucion/{ejecucionId}")
    public ResponseEntity<Long> contarPorEjecucion(@PathVariable Long ejecucionId) {
        log.debug("GET /api/inscripciones/count/ejecucion/{} - Contando inscripciones por ejecución", ejecucionId);
        Long count = inscripcionService.contarPorEjecucion(ejecucionId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar inscripciones por estudiante
     */
    @GetMapping("/count/estudiante/{estudianteId}")
    public ResponseEntity<Long> contarPorEstudiante(@PathVariable Long estudianteId) {
        log.debug("GET /api/inscripciones/count/estudiante/{} - Contando inscripciones por estudiante", estudianteId);
        Long count = inscripcionService.contarPorEstudiante(estudianteId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar inscripciones por estado
     */
    @GetMapping("/count/estado/{estado}")
    public ResponseEntity<Long> contarPorEstado(@PathVariable String estado) {
        log.debug("GET /api/inscripciones/count/estado/{} - Contando inscripciones por estado", estado);
        Long count = inscripcionService.contarPorEstado(estado);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Cambiar estado de inscripción
     */
    @PutMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<Inscripcion> cambiarEstado(@PathVariable Long id, @PathVariable String nuevoEstado) {
        log.debug("PUT /api/inscripciones/{}/estado/{} - Cambiando estado de inscripción", id, nuevoEstado);
        try {
            Inscripcion inscripcionActualizada = inscripcionService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(inscripcionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar estado de inscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Anular inscripción
     */
    @PutMapping("/{id}/anular")
    public ResponseEntity<Inscripcion> anular(@PathVariable Long id) {
        log.debug("PUT /api/inscripciones/{}/anular - Anulando inscripción", id);
        try {
            Inscripcion inscripcionAnulada = inscripcionService.anular(id);
            return ResponseEntity.ok(inscripcionAnulada);
        } catch (IllegalArgumentException e) {
            log.error("Error al anular inscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener historial académico de un estudiante
     */
    @GetMapping("/historial/estudiante/{estudianteId}")
    public ResponseEntity<List<Inscripcion>> obtenerHistorialAcademico(@PathVariable Long estudianteId) {
        log.debug("GET /api/inscripciones/historial/estudiante/{} - Obteniendo historial académico", estudianteId);
        List<Inscripcion> historial = inscripcionService.obtenerHistorialAcademico(estudianteId);
        return ResponseEntity.ok(historial);
    }
}
