package com.edutech.microservicio_ejecucion.controller;

import com.edutech.microservicio_ejecucion.model.Ejecucion;
import com.edutech.microservicio_ejecucion.service.EjecucionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejecuciones")
public class EjecucionController {
    
    private final EjecucionService ejecucionService;
    
    public EjecucionController(EjecucionService ejecucionService) {
        this.ejecucionService = ejecucionService;
    }
    
    @GetMapping
    public ResponseEntity<List<Ejecucion>> getAllEjecuciones() {
        return ResponseEntity.ok(ejecucionService.getAllEjecuciones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ejecucion> getEjecucionById(@PathVariable Integer id) {
        Ejecucion ejecucion = ejecucionService.getEjecucionById(id);
        if (ejecucion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ejecucion);
    }
    
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<Ejecucion>> getEjecucionesByCurso(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(ejecucionService.getEjecucionesByCurso(idCurso));
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Ejecucion>> getEjecucionesByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(ejecucionService.getEjecucionesByEstado(estado));
    }
    
    @PostMapping
    public ResponseEntity<Ejecucion> createEjecucion(@RequestBody Ejecucion ejecucion) {
        return new ResponseEntity<>(ejecucionService.createEjecucion(ejecucion), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Ejecucion> updateEjecucion(@PathVariable Integer id, @RequestBody Ejecucion ejecucion) {
        Ejecucion updated = ejecucionService.updateEjecucion(id, ejecucion);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEjecucion(@PathVariable Integer id) {
        if (ejecucionService.deleteEjecucion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
