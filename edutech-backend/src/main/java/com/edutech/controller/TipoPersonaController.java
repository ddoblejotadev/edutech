package com.edutech.controller;

//Importaciones Modelo y Service
import com.edutech.model.TipoPersona;
import com.edutech.service.TipoPersonaService;

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
@RequestMapping("/api/tipos-persona")
@CrossOrigin(origins = "*")
public class TipoPersonaController {

    @Autowired
    private TipoPersonaService tipoPersonaService;
    
    @GetMapping
    public ResponseEntity<List<TipoPersona>> obtenerTodos() {
        List<TipoPersona> tipos = tipoPersonaService.obtenerTodos();
        return ResponseEntity.ok(tipos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TipoPersona> obtenerPorId(@PathVariable Long id) {
        return tipoPersonaService.obtenerPorId(id)
                .map(tipo -> ResponseEntity.ok(tipo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TipoPersona> crear(@RequestBody TipoPersona tipoPersona) {
        try {
            TipoPersona nuevoTipo = tipoPersonaService.crear(tipoPersona);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TipoPersona> actualizar(@PathVariable Long id, @RequestBody TipoPersona tipoPersona) {
        try {
            return tipoPersonaService.actualizar(id, tipoPersona)
                    .map(tipo -> ResponseEntity.ok(tipo))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            tipoPersonaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
