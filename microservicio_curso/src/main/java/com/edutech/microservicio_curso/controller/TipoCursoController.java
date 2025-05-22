package com.edutech.microservicio_curso.controller;

import com.edutech.microservicio_curso.model.TipoCurso;
import com.edutech.microservicio_curso.service.TipoCursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tiposcurso")
public class TipoCursoController {

    private final TipoCursoService tipoCursoService;

    public TipoCursoController(TipoCursoService tipoCursoService) {
        this.tipoCursoService = tipoCursoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoCurso>> getAllTiposCurso() {
        List<TipoCurso> tiposCurso = tipoCursoService.findAll();
        return new ResponseEntity<>(tiposCurso, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCurso> getTipoCursoById(@PathVariable Integer id) {
        return tipoCursoService.findById(id)
                .map(tipoCurso -> new ResponseEntity<>(tipoCurso, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoCurso> getTipoCursoByNombre(@PathVariable String nombre) {
        return tipoCursoService.findByNombre(nombre)
                .map(tipoCurso -> new ResponseEntity<>(tipoCurso, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TipoCurso> createTipoCurso(@RequestBody TipoCurso tipoCurso) {
        if (tipoCursoService.existsByNombre(tipoCurso.getNombre())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        TipoCurso savedTipoCurso = tipoCursoService.save(tipoCurso);
        return new ResponseEntity<>(savedTipoCurso, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCurso> updateTipoCurso(@PathVariable Integer id, @RequestBody TipoCurso tipoCurso) {
        return tipoCursoService.findById(id)
                .map(existingTipoCurso -> {
                    // Verificar si el nuevo nombre ya existe en otro registro
                    if (!existingTipoCurso.getNombre().equalsIgnoreCase(tipoCurso.getNombre()) &&
                        tipoCursoService.existsByNombre(tipoCurso.getNombre())) {
                        return new ResponseEntity<TipoCurso>(HttpStatus.CONFLICT);
                    }
                    
                    // Usar el nombre correcto del ID
                    tipoCurso.setId(id);
                    TipoCurso updatedTipoCurso = tipoCursoService.save(tipoCurso);
                    return new ResponseEntity<>(updatedTipoCurso, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoCurso(@PathVariable Integer id) {
        if (tipoCursoService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
