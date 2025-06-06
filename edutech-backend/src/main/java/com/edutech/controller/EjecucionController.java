package com.edutech.controller;

//Importaciones Modelo y Service
import com.edutech.model.Ejecucion;
import com.edutech.service.EjecucionService;

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
@RequestMapping("/api/ejecuciones")
@CrossOrigin(origins = "*")
public class EjecucionController {

    @Autowired
    private EjecucionService ejecucionService;
    
    @GetMapping
    public ResponseEntity<List<Ejecucion>> obtenerTodas() {
        List<Ejecucion> ejecuciones = ejecucionService.obtenerTodas();
        return ResponseEntity.ok(ejecuciones);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ejecucion> obtenerPorId(@PathVariable Long id) {
        return ejecucionService.obtenerPorId(id)
                .map(ejecucion -> ResponseEntity.ok(ejecucion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Ejecucion> crear(@RequestBody Ejecucion ejecucion) {
        try {
            Ejecucion nuevaEjecucion = ejecucionService.crear(ejecucion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEjecucion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Ejecucion> actualizar(@PathVariable Long id, @RequestBody Ejecucion ejecucion) {
        try {
            Ejecucion ejecucionActualizada = ejecucionService.actualizar(id, ejecucion);
            return ResponseEntity.ok(ejecucionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            ejecucionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
