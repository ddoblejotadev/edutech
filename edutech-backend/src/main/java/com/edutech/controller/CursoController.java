package com.edutech.controller;

import com.edutech.model.Curso;
import com.edutech.service.CursoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CursoController {
    
    private final CursoService cursoService;
    
    /**
     * Obtener todos los cursos con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Curso>> obtenerTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/cursos - Obteniendo cursos con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Curso> cursos = cursoService.obtenerTodos(pageable);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener curso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/cursos/{} - Obteniendo curso por ID", id);
        return cursoService.obtenerPorId(id)
                .map(curso -> ResponseEntity.ok(curso))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener cursos por código
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<List<Curso>> obtenerPorCodigo(@PathVariable String codigo) {
        log.debug("GET /api/cursos/codigo/{} - Obteniendo cursos por código", codigo);
        List<Curso> cursos = cursoService.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Crear nuevo curso
     */
    @PostMapping
    public ResponseEntity<Curso> crear(@Valid @RequestBody Curso curso) {
        log.debug("POST /api/cursos - Creando nuevo curso: {}", curso.getNombre());
        try {
            Curso cursoCreado = cursoService.crear(curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoCreado);
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
        log.debug("PUT /api/cursos/{} - Actualizando curso", id);
        try {
            Curso cursoActualizado = cursoService.actualizar(id, curso);
            return ResponseEntity.ok(cursoActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar curso: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar curso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/cursos/{} - Eliminando curso", id);
        try {
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar curso: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Buscar cursos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Curso>> buscarPorNombre(@RequestParam String nombre) {
        log.debug("GET /api/cursos/buscar?nombre={} - Buscando cursos por nombre", nombre);
        List<Curso> cursos = cursoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos por nivel
     */
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<Curso>> obtenerPorNivel(@PathVariable String nivel) {
        log.debug("GET /api/cursos/nivel/{} - Obteniendo cursos por nivel", nivel);
        List<Curso> cursos = cursoService.obtenerPorNivel(nivel);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos por rango de créditos
     */
    @GetMapping("/creditos")
    public ResponseEntity<List<Curso>> obtenerPorRangoCreditos(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        log.debug("GET /api/cursos/creditos?min={}&max={} - Obteniendo cursos por rango de créditos", min, max);
        List<Curso> cursos = cursoService.obtenerPorRangoCreditos(min, max);
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener cursos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Curso>> obtenerActivos() {
        log.debug("GET /api/cursos/activos - Obteniendo cursos activos");
        List<Curso> cursos = cursoService.obtenerActivos();
        return ResponseEntity.ok(cursos);
    }
    
    /**
     * Obtener prerrequisitos de un curso
     */
    @GetMapping("/{id}/prerrequisitos")
    public ResponseEntity<List<Curso>> obtenerPrerrequisitos(@PathVariable Long id) {
        log.debug("GET /api/cursos/{}/prerrequisitos - Obteniendo prerrequisitos del curso", id);
        List<Curso> prerrequisitos = cursoService.obtenerPrerrequisitos(id);
        return ResponseEntity.ok(prerrequisitos);
    }
    
    /**
     * Obtener cursos que tienen como prerrequisito el curso especificado
     */
    @GetMapping("/{id}/dependientes")
    public ResponseEntity<List<Curso>> obtenerCursosDependientes(@PathVariable Long id) {
        log.debug("GET /api/cursos/{}/dependientes - Obteniendo cursos dependientes", id);
        List<Curso> dependientes = cursoService.obtenerCursosDependientes(id);
        return ResponseEntity.ok(dependientes);
    }
    
    /**
     * Agregar prerrequisito a un curso
     */
    @PostMapping("/{cursoId}/prerrequisitos/{prerrequisito}")
    public ResponseEntity<Curso> agregarPrerrequisito(@PathVariable Long cursoId, @PathVariable Long prerrequisito) {
        log.debug("POST /api/cursos/{}/prerrequisitos/{} - Agregando prerrequisito", cursoId, prerrequisito);
        try {
            Curso curso = cursoService.agregarPrerrequisito(cursoId, prerrequisito);
            return ResponseEntity.ok(curso);
        } catch (IllegalArgumentException e) {
            log.error("Error al agregar prerrequisito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar prerrequisito de un curso
     */
    @DeleteMapping("/{cursoId}/prerrequisitos/{prerrequisito}")
    public ResponseEntity<Curso> eliminarPrerrequisito(@PathVariable Long cursoId, @PathVariable Long prerrequisito) {
        log.debug("DELETE /api/cursos/{}/prerrequisitos/{} - Eliminando prerrequisito", cursoId, prerrequisito);
        try {
            Curso curso = cursoService.eliminarPrerrequisito(cursoId, prerrequisito);
            return ResponseEntity.ok(curso);
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar prerrequisito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Verificar si un curso puede ser prerrequisito de otro
     */
    @GetMapping("/{cursoId}/puede-ser-prerrequisito/{prerrequisito}")
    public ResponseEntity<Boolean> puedeSerPrerrequisito(@PathVariable Long cursoId, @PathVariable Long prerrequisito) {
        log.debug("GET /api/cursos/{}/puede-ser-prerrequisito/{} - Verificando si puede ser prerrequisito", cursoId, prerrequisito);
        boolean puede = cursoService.puedeSerPrerrequisito(cursoId, prerrequisito);
        return ResponseEntity.ok(puede);
    }
    
    /**
     * Contar total de cursos
     */
    @GetMapping("/count")
    public ResponseEntity<Long> contar() {
        log.debug("GET /api/cursos/count - Contando cursos");
        Long count = cursoService.contar();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Obtener cursos ordenados por nombre
     */
    @GetMapping("/ordenados")
    public ResponseEntity<List<Curso>> obtenerOrdenadosPorNombre() {
        log.debug("GET /api/cursos/ordenados - Obteniendo cursos ordenados por nombre");
        List<Curso> cursos = cursoService.obtenerOrdenadosPorNombre();
        return ResponseEntity.ok(cursos);
    }
}
