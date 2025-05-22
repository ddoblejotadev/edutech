package com.edutech.microservicio_curso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.edutech.microservicio_curso.model.Curso;
import com.edutech.microservicio_curso.service.CursoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    
    private final CursoService cursoService;
    
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Curso>> getAllCursos() {
        List<Curso> cursos = cursoService.findAll();
        return ResponseEntity.ok(cursos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Curso> getCursoById(@PathVariable Integer id) {
        Optional<Curso> curso = cursoService.findById(id);
        return curso.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Curso> createCurso(@RequestBody Curso curso) {
        Curso savedCurso = cursoService.save(curso);
        return new ResponseEntity<>(savedCurso, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Curso> updateCurso(@PathVariable Integer id, @RequestBody Curso curso) {
        Optional<Curso> existingCurso = cursoService.findById(id);
        
        if (existingCurso.isPresent()) {
            curso.setIdCurso(id);
            Curso updatedCurso = cursoService.save(curso);
            return ResponseEntity.ok(updatedCurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Integer id) {
        Optional<Curso> curso = cursoService.findById(id);
        
        if (curso.isPresent()) {
            cursoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
