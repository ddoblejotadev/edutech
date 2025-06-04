package com.edutech.controller;

import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;
import com.edutech.service.InscripcionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
     * Obtener todas las inscripciones
     */
    @GetMapping
    public ResponseEntity<List<Inscripcion>> obtenerTodas() {
        log.info("GET /api/inscripciones - Obteniendo todas las inscripciones");
        List<Inscripcion> inscripciones = inscripcionService.obtenerTodas();
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/inscripciones/{} - Obteniendo inscripción por ID", id);
        return inscripcionService.obtenerPorId(id)
                .map(inscripcion -> ResponseEntity.ok(inscripcion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Inscribir estudiante en una ejecución
     */
    @PostMapping("/inscribir")
    public ResponseEntity<Inscripcion> inscribir(
            @RequestParam Long estudianteId,
            @RequestParam Long ejecucionId) {
        log.info("POST /api/inscripciones/inscribir - Inscribiendo estudiante {} en ejecución {}", 
                estudianteId, ejecucionId);
        try {
            Inscripcion inscripcion = inscripcionService.inscribir(estudianteId, ejecucionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al inscribir estudiante: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Crear inscripción con fecha específica
     */
    @PostMapping
    public ResponseEntity<Inscripcion> crear(@Valid @RequestBody Inscripcion inscripcion) {
        log.info("POST /api/inscripciones - Creando nueva inscripción");
        try {
            Inscripcion nuevaInscripcion = inscripcionService.crear(inscripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al crear inscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancelar inscripción por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarInscripcion(@PathVariable Long id) {
        log.info("DELETE /api/inscripciones/{} - Cancelando inscripción", id);
        try {
            inscripcionService.cancelarInscripcion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al cancelar inscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancelar inscripción por estudiante y ejecución
     */
    @DeleteMapping("/estudiante/{estudianteId}/ejecucion/{ejecucionId}")
    public ResponseEntity<Void> cancelarInscripcionPorEstudianteYEjecucion(
            @PathVariable Long estudianteId,
            @PathVariable Long ejecucionId) {
        log.info("DELETE /api/inscripciones/estudiante/{}/ejecucion/{} - Cancelando inscripción específica", 
                estudianteId, ejecucionId);
        try {
            inscripcionService.cancelarInscripcion(estudianteId, ejecucionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al cancelar inscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener inscripciones por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Inscripcion>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/inscripciones/estudiante/{} - Obteniendo inscripciones por estudiante", estudianteId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones por ejecución
     */
    @GetMapping("/ejecucion/{ejecucionId}")
    public ResponseEntity<List<Inscripcion>> obtenerPorEjecucion(@PathVariable Long ejecucionId) {
        log.info("GET /api/inscripciones/ejecucion/{} - Obteniendo inscripciones por ejecución", ejecucionId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorEjecucion(ejecucionId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones activas de un estudiante
     */
    @GetMapping("/estudiante/{estudianteId}/activas")
    public ResponseEntity<List<Inscripcion>> obtenerActivasDeEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/inscripciones/estudiante/{}/activas - Obteniendo inscripciones activas", estudianteId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerActivasDeEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones futuras de un estudiante
     */
    @GetMapping("/estudiante/{estudianteId}/futuras")
    public ResponseEntity<List<Inscripcion>> obtenerFuturasDeEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/inscripciones/estudiante/{}/futuras - Obteniendo inscripciones futuras", estudianteId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerFuturasDeEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener inscripciones pasadas de un estudiante
     */
    @GetMapping("/estudiante/{estudianteId}/pasadas")
    public ResponseEntity<List<Inscripcion>> obtenerPasadasDeEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/inscripciones/estudiante/{}/pasadas - Obteniendo inscripciones pasadas", estudianteId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPasadasDeEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Obtener estudiantes inscritos en una ejecución
     */
    @GetMapping("/ejecucion/{ejecucionId}/estudiantes")
    public ResponseEntity<List<Persona>> obtenerEstudiantesInscritos(@PathVariable Long ejecucionId) {
        log.info("GET /api/inscripciones/ejecucion/{}/estudiantes - Obteniendo estudiantes inscritos", ejecucionId);
        List<Persona> estudiantes = inscripcionService.obtenerEstudiantesInscritos(ejecucionId);
        return ResponseEntity.ok(estudiantes);
    }
    
    /**
     * Obtener inscripciones por curso
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> obtenerPorCurso(@PathVariable Long cursoId) {
        log.info("GET /api/inscripciones/curso/{} - Obteniendo inscripciones por curso", cursoId);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorCurso(cursoId);
        return ResponseEntity.ok(inscripciones);
    }
    
    /**
     * Contar inscripciones por ejecución
     */
    @GetMapping("/ejecucion/{ejecucionId}/contar")
    public ResponseEntity<Integer> contarPorEjecucion(@PathVariable Long ejecucionId) {
        log.info("GET /api/inscripciones/ejecucion/{}/contar - Contando inscripciones por ejecución", ejecucionId);
        Integer count = inscripcionService.contarPorEjecucion(ejecucionId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar inscripciones por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}/contar")
    public ResponseEntity<Integer> contarPorEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/inscripciones/estudiante/{}/contar - Contando inscripciones por estudiante", estudianteId);
        Integer count = inscripcionService.contarPorEstudiante(estudianteId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Verificar si estudiante está inscrito en ejecución
     */
    @GetMapping("/existe/estudiante/{estudianteId}/ejecucion/{ejecucionId}")
    public ResponseEntity<Boolean> estaInscrito(@PathVariable Long estudianteId, @PathVariable Long ejecucionId) {
        log.info("GET /api/inscripciones/existe/estudiante/{}/ejecucion/{} - Verificando inscripción", 
                estudianteId, ejecucionId);
        boolean inscrito = inscripcionService.estaInscrito(estudianteId, ejecucionId);
        return ResponseEntity.ok(inscrito);
    }
    
    /**
     * Verificar si estudiante está inscrito en alguna ejecución de un curso
     */
    @GetMapping("/existe/estudiante/{estudianteId}/curso/{cursoId}")
    public ResponseEntity<Boolean> estaInscritoEnCurso(@PathVariable Long estudianteId, @PathVariable Long cursoId) {
        log.info("GET /api/inscripciones/existe/estudiante/{}/curso/{} - Verificando inscripción en curso", 
                estudianteId, cursoId);
        boolean inscrito = inscripcionService.estaInscritoEnCurso(estudianteId, cursoId);
        return ResponseEntity.ok(inscrito);
    }
    
    /**
     * Obtener últimas inscripciones
     */
    @GetMapping("/ultimas")
    public ResponseEntity<List<Inscripcion>> obtenerUltimas() {
        log.info("GET /api/inscripciones/ultimas - Obteniendo últimas inscripciones");
        List<Inscripcion> inscripciones = inscripcionService.obtenerUltimas();
        return ResponseEntity.ok(inscripciones);
    }
}