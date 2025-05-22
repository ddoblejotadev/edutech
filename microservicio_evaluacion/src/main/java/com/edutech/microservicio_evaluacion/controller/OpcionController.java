package com.edutech.microservicio_evaluacion.controller;

import com.edutech.microservicio_evaluacion.model.Opcion;
import com.edutech.microservicio_evaluacion.service.OpcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opciones")
public class OpcionController {
    
    private final OpcionService opcionService;
    
    @Autowired
    public OpcionController(OpcionService opcionService) {
        this.opcionService = opcionService;
    }
    
    @GetMapping
    public ResponseEntity<List<Opcion>> getAllOpciones() {
        return ResponseEntity.ok(opcionService.getAllOpciones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Opcion> getOpcionById(@PathVariable Integer id) {
        Opcion opcion = opcionService.getOpcionById(id);
        if (opcion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opcion);
    }
    
    @GetMapping("/pregunta/{idPregunta}")
    public ResponseEntity<List<Opcion>> getOpcionesByPregunta(@PathVariable Integer idPregunta) {
        return ResponseEntity.ok(opcionService.getOpcionesByPregunta(idPregunta));
    }
    
    @PostMapping
    public ResponseEntity<Opcion> crearOpcion(@RequestBody Opcion opcion) {
        Opcion nuevaOpcion = opcionService.crearOpcion(opcion);
        if (nuevaOpcion == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(nuevaOpcion, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Opcion> actualizarOpcion(@PathVariable Integer id, @RequestBody Opcion opcion) {
        Opcion opcionActualizada = opcionService.actualizarOpcion(id, opcion);
        if (opcionActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opcionActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOpcion(@PathVariable Integer id) {
        if (opcionService.eliminarOpcion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
