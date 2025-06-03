package com.edutech.controller;

import com.edutech.model.TipoPersona;
import com.edutech.service.TipoPersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-persona")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TipoPersonaController {
    
    private final TipoPersonaService tipoPersonaService;
    
    /**
     * Obtener todos los tipos de persona
     */
    @GetMapping
    public ResponseEntity<List<TipoPersona>> obtenerTodos() {
        log.debug("GET /api/tipos-persona - Obteniendo todos los tipos de persona");
        List<TipoPersona> tipos = tipoPersonaService.obtenerTodos();
        return ResponseEntity.ok(tipos);
    }
    
    /**
     * Obtener tipo de persona por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoPersona> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/tipos-persona/{} - Obteniendo tipo de persona por ID", id);
        return tipoPersonaService.obtenerPorId(id)
                .map(tipo -> ResponseEntity.ok(tipo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener tipo de persona por nombre
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoPersona> obtenerPorNombre(@PathVariable String nombre) {
        log.debug("GET /api/tipos-persona/nombre/{} - Obteniendo tipo de persona por nombre", nombre);
        return tipoPersonaService.obtenerPorNombre(nombre)
                .map(tipo -> ResponseEntity.ok(tipo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nuevo tipo de persona
     */
    @PostMapping
    public ResponseEntity<TipoPersona> crear(@Valid @RequestBody TipoPersona tipoPersona) {
        log.debug("POST /api/tipos-persona - Creando nuevo tipo de persona: {}", tipoPersona.getNombre());
        try {
            TipoPersona tipoCreado = tipoPersonaService.crear(tipoPersona);
            return ResponseEntity.status(HttpStatus.CREATED).body(tipoCreado);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear tipo de persona: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar tipo de persona existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoPersona> actualizar(@PathVariable Long id, @Valid @RequestBody TipoPersona tipoPersona) {
        log.debug("PUT /api/tipos-persona/{} - Actualizando tipo de persona", id);
        try {
            TipoPersona tipoActualizado = tipoPersonaService.actualizar(id, tipoPersona);
            return ResponseEntity.ok(tipoActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar tipo de persona: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar tipo de persona
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/tipos-persona/{} - Eliminando tipo de persona", id);
        try {
            tipoPersonaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar tipo de persona: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verificar si existe un tipo de persona por nombre
     */
    @GetMapping("/existe/nombre/{nombre}")
    public ResponseEntity<Boolean> existePorNombre(@PathVariable String nombre) {
        log.debug("GET /api/tipos-persona/existe/nombre/{} - Verificando existencia por nombre", nombre);
        boolean existe = tipoPersonaService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Contar total de tipos de persona
     */
    @GetMapping("/count")
    public ResponseEntity<Long> contar() {
        log.debug("GET /api/tipos-persona/count - Contando tipos de persona");
        Long count = tipoPersonaService.contar();
        return ResponseEntity.ok(count);
    }
}
