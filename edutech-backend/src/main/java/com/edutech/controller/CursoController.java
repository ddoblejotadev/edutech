package com.edutech.controller;

//Importaciones Modelo y Service
import com.edutech.model.Curso;
import com.edutech.service.CursoService;

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
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    /**
     * Obtener todos los cursos
     */
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerTodos() {
        List<Curso> cursos = cursoService.obtenerTodos();
        return ResponseEntity.ok(cursos);
    }

    /**
     * Obtener curso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        return cursoService.obtenerPorId(id)
                .map(curso -> ResponseEntity.ok(curso))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear nuevo curso
     */
    @PostMapping
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        try {
            Curso nuevoCurso = cursoService.crear(curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCurso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar curso existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long id, @RequestBody Curso cursoActualizado) {
        try {
            return cursoService.actualizar(id, cursoActualizado)
                    .map(curso -> ResponseEntity.ok(curso))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar curso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Buscar cursos por nombre
     */
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Curso>> buscarPorNombre(@PathVariable String nombre) {
        List<Curso> cursos = cursoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(cursos);
    }

    /**
     * Buscar cursos por descripción
     */
    @GetMapping("/buscar/descripcion/{descripcion}")
    public ResponseEntity<List<Curso>> buscarPorDescripcion(@PathVariable String descripcion) {
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
        List<Curso> cursos = cursoService.obtenerPorRangoDuracion(duracionMin, duracionMax);
        return ResponseEntity.ok(cursos);
    }

    /**
     * Obtener cursos sin prerequisitos
     */
    @GetMapping("/sin-prerequisitos")
    public ResponseEntity<List<Curso>> obtenerSinPrerequisitos() {
        List<Curso> cursos = cursoService.obtenerSinPrerequisitos();
        return ResponseEntity.ok(cursos);
    }

    /**
     * Obtener cursos que requieren un prerequisito específico
     */
    @GetMapping("/prerequisito/{prerequisitoCodigo}")
    public ResponseEntity<List<Curso>> obtenerConPrerequisito(@PathVariable String prerequisitoCodigo) {
        List<Curso> cursos = cursoService.obtenerConPrerequisito(prerequisitoCodigo);
        return ResponseEntity.ok(cursos);
    }

    /**
     * Obtener cursos ordenados por nombre
     */
    @GetMapping("/ordenados/nombre")
    public ResponseEntity<List<Curso>> obtenerOrdenadosPorNombre() {
        List<Curso> cursos = cursoService.obtenerOrdenadosPorNombre();
        return ResponseEntity.ok(cursos);
    }

    /**
     * Obtener cursos ordenados por duración
     */
    @GetMapping("/ordenados/duracion")
    public ResponseEntity<List<Curso>> obtenerOrdenadosPorDuracion(
            @RequestParam(defaultValue = "true") boolean ascendente) {
        List<Curso> cursos = cursoService.obtenerOrdenadosPorDuracion(ascendente);
        return ResponseEntity.ok(cursos);
    }

    /**
     * Verificar si existe curso por nombre
     */
    @GetMapping("/existe/nombre/{nombre}")
    public ResponseEntity<Boolean> existePorNombre(@PathVariable String nombre) {
        boolean existe = cursoService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }
}