package com.edutech.controller;

import com.edutech.model.Curso;
import com.edutech.service.CursoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CursoController {
    
    private final CursoService cursoService;
    
    /**
     * Obtener todos los cursos
     */
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerTodos() {
        log.info("GET /api/cursos - Obteniendo todos los cursos");
        List<Curso> cursos = cursoService.obtenerTodos();
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener curso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/cursos/{} - Obteniendo curso por ID", id);
        return cursoService.obtenerPorId(id)
                .map(curso -> ResponseEntity.ok(curso))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nuevo curso
     */
    @PostMapping
    public ResponseEntity<Curso> crear(@Valid @RequestBody Curso curso) {
        log.info("POST /api/cursos - Creando nuevo curso: {}", curso.getNombre());
        try {
            Curso nuevoCurso = cursoService.crear(curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCurso);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear curso: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar curso existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @Valid @RequestBody Curso curso) {
        log.info("PUT /api/cursos/{} - Actualizando curso", id);
        try {
            Curso cursoActualizado = cursoService.actualizar(id, curso);
            return ResponseEntity.ok(cursoActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar curso: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar curso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/cursos/{} - Eliminando curso", id);
        try {
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al eliminar curso: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Buscar cursos por nombre
     */
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Curso>> buscarPorNombre(@PathVariable String nombre) {
        log.info("GET /api/cursos/buscar/nombre/{} - Buscando cursos por nombre", nombre);
        List<Curso> cursos = cursoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Buscar cursos por descripción
     */
    @GetMapping("/buscar/descripcion/{descripcion}")
    public ResponseEntity<List<Curso>> buscarPorDescripcion(@PathVariable String descripcion) {
        log.info("GET /api/cursos/buscar/descripcion/{} - Buscando cursos por descripción", descripcion);
        List<Curso> cursos = cursoService.buscarPorDescripcion(descripcion);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos por rango de duración
     */
    @GetMapping("/duracion")
    public ResponseEntity<List<Curso>> obtenerPorRangoDuracion(
            @RequestParam Integer duracionMin,
            @RequestParam Integer duracionMax) {
        log.info("GET /api/cursos/duracion - Obteniendo cursos con duración entre {} y {} horas", 
                duracionMin, duracionMax);
        List<Curso> cursos = cursoService.obtenerPorRangoDuracion(duracionMin, duracionMax);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos sin prerequisitos
     */
    @GetMapping("/sin-prerequisitos")
    public ResponseEntity<List<Curso>> obtenerSinPrerequisitos() {
        log.info("GET /api/cursos/sin-prerequisitos - Obteniendo cursos sin prerequisitos");
        List<Curso> cursos = cursoService.obtenerSinPrerequisitos();
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos que requieren un prerequisito específico
     */
    @GetMapping("/prerequisito/{prerequisitoId}")
    public ResponseEntity<List<Curso>> obtenerConPrerequisito(@PathVariable Long prerequisitoId) {
        log.info("GET /api/cursos/prerequisito/{} - Obteniendo cursos con prerequisito específico", prerequisitoId);
        List<Curso> cursos = cursoService.obtenerConPrerequisito(prerequisitoId);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos ordenados por nombre
     */
    @GetMapping("/ordenados/nombre")
    public ResponseEntity<List<Curso>> obtenerOrdenadosPorNombre() {
        log.info("GET /api/cursos/ordenados/nombre - Obteniendo cursos ordenados por nombre");
        List<Curso> cursos = cursoService.obtenerOrdenadosPorNombre();
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos ordenados por duración
     */
    @GetMapping("/ordenados/duracion")
    public ResponseEntity<List<Curso>> obtenerOrdenadosPorDuracion(
            @RequestParam(defaultValue = "true") boolean ascendente) {
        log.info("GET /api/cursos/ordenados/duracion - Obteniendo cursos ordenados por duración: {}", 
                ascendente ? "ascendente" : "descendente");
        List<Curso> cursos = cursoService.obtenerOrdenadosPorDuracion(ascendente);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Verificar si existe curso por nombre
     */
    @GetMapping("/existe/nombre/{nombre}")
    public ResponseEntity<Boolean> existePorNombre(@PathVariable String nombre) {
        log.info("GET /api/cursos/existe/nombre/{} - Verificando existencia por nombre", nombre);
        boolean existe = cursoService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }
}