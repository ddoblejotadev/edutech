package com.edutech.microservicio_curso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.edutech.microservicio_curso.model.TipoCurso;
import com.edutech.microservicio_curso.service.TipoCursoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-cursos")
public class TipoCursoController {
    
    private final TipoCursoService tipoCursoService;
    
    public TipoCursoController(TipoCursoService tipoCursoService) {
        this.tipoCursoService = tipoCursoService;
    }
    
    @GetMapping
    public ResponseEntity<List<TipoCurso>> getAllTiposCurso() {
        List<TipoCurso> tiposCurso = tipoCursoService.findAll();
        return ResponseEntity.ok(tiposCurso);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TipoCurso> getTipoCursoById(@PathVariable Integer id) {
        Optional<TipoCurso> tipoCurso = tipoCursoService.findById(id);
        return tipoCurso.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TipoCurso> createTipoCurso(@RequestBody TipoCurso tipoCurso) {
        // Verificar si ya existe un tipo de curso con el mismo nombre
        if (tipoCursoService.existsByNombre(tipoCurso.getNombre())) {
            return ResponseEntity.badRequest().build();
        }
        
        TipoCurso savedTipoCurso = tipoCursoService.save(tipoCurso);
        return new ResponseEntity<>(savedTipoCurso, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TipoCurso> updateTipoCurso(@PathVariable Integer id, @RequestBody TipoCurso tipoCurso) {
        Optional<TipoCurso> existingTipoCurso = tipoCursoService.findById(id);
        
        if (existingTipoCurso.isPresent()) {
            // Verificar si el nuevo nombre ya existe en otro tipo de curso
            Optional<TipoCurso> tipoCursoByNombre = tipoCursoService.findByNombre(tipoCurso.getNombre());
            if (tipoCursoByNombre.isPresent() && !tipoCursoByNombre.get().getId().equals(id)) {
                return ResponseEntity.badRequest().build();
            }
            
            tipoCurso.setId(id);
            TipoCurso updatedTipoCurso = tipoCursoService.save(tipoCurso);
            return ResponseEntity.ok(updatedTipoCurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoCurso(@PathVariable Integer id) {
        Optional<TipoCurso> tipoCurso = tipoCursoService.findById(id);
        
        if (tipoCurso.isPresent()) {
            tipoCursoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
