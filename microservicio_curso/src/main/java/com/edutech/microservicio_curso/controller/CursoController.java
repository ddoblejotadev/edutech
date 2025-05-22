package com.edutech.microservicio_curso.controller;

import com.edutech.microservicio_curso.model.Curso;
import com.edutech.microservicio_curso.service.CursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    
    @Autowired
    private CursoService cursoService;
    
    // Método para debugging
    private void logRequest(String method, String path) {
        System.out.println("Request: " + method + " " + path);
    }

    @GetMapping
    public ResponseEntity<?> getAllCursos() {
        logRequest("GET", "/api/cursos");
        try {
            List<Curso> cursos = cursoService.findAll();
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCursoById(@PathVariable Integer id) {
        logRequest("GET", "/api/cursos/" + id);
        try {
            return cursoService.findById(id)
                    .map(curso -> new ResponseEntity<>(curso, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> getCursosByCategoria(@PathVariable String categoria) {
        logRequest("GET", "/api/cursos/categoria/" + categoria);
        try {
            List<Curso> cursos = cursoService.findByCategoria(categoria);
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/tipo/{idTipoCurso}")
    public ResponseEntity<?> getCursosByTipo(@PathVariable Integer idTipoCurso) {
        logRequest("GET", "/api/cursos/tipo/" + idTipoCurso);
        try {
            List<Curso> cursos = cursoService.findByTipoCurso(idTipoCurso);
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<?> searchCursosByNombre(@PathVariable String nombre) {
        logRequest("GET", "/api/cursos/buscar/" + nombre);
        try {
            List<Curso> cursos = cursoService.searchByNombre(nombre);
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCurso(@RequestBody Curso curso) {
        try {
            Curso savedCurso = cursoService.save(curso);
            return new ResponseEntity<>(savedCurso, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCurso(@PathVariable Integer id, @RequestBody Curso curso) {
        try {
            return cursoService.findById(id)
                    .map(existingCurso -> {
                        curso.setIdCurso(id);  // Aquí está la corrección
                        Curso updatedCurso = cursoService.save(curso);
                        return new ResponseEntity<>(updatedCurso, HttpStatus.OK);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurso(@PathVariable Integer id) {
        try {
            return cursoService.findById(id)
                    .map(curso -> {
                        cursoService.deleteById(id);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
