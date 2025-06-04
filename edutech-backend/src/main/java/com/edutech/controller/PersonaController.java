package com.edutech.controller;

import com.edutech.model.Persona;
import com.edutech.service.PersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PersonaController {
    
    private final PersonaService personaService;
    
    /**
     * Obtener todas las personas con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Persona>> obtenerTodas(Pageable pageable) {
        log.info("GET /api/personas - Obteniendo todas las personas con paginación");
        Page<Persona> personas = personaService.obtenerTodas(pageable);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener persona por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/personas/{} - Obteniendo persona por ID", id);
        return personaService.obtenerPorId(id)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener persona por RUT
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Persona> obtenerPorRut(@PathVariable String rut) {
        log.info("GET /api/personas/rut/{} - Obteniendo persona por RUT", rut);
        return personaService.obtenerPorRut(rut)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener persona por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Persona> obtenerPorEmail(@PathVariable String email) {
        log.info("GET /api/personas/email/{} - Obteniendo persona por email", email);
        return personaService.obtenerPorEmail(email)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva persona
     */
    @PostMapping
    public ResponseEntity<Persona> crear(@Valid @RequestBody Persona persona) {
        log.info("POST /api/personas - Creando nueva persona");
        try {
            Persona nuevaPersona = personaService.crear(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear persona: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar persona existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @Valid @RequestBody Persona persona) {
        log.info("PUT /api/personas/{} - Actualizando persona", id);
        try {
            Persona personaActualizada = personaService.actualizar(id, persona);
            return ResponseEntity.ok(personaActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar persona: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar persona
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/personas/{} - Eliminando persona", id);
        try {
            personaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar persona: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Buscar personas por nombre o apellido
     */
    @GetMapping("/buscar/{termino}")
    public ResponseEntity<List<Persona>> buscarPorNombreOApellido(@PathVariable String termino) {
        log.info("GET /api/personas/buscar/{} - Buscando personas por término", termino);
        List<Persona> personas = personaService.buscarPorNombreOApellido(termino);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener personas por tipo
     */
    @GetMapping("/tipo/{tipoPersonaId}")
    public ResponseEntity<List<Persona>> obtenerPorTipo(@PathVariable Long tipoPersonaId) {
        log.info("GET /api/personas/tipo/{} - Obteniendo personas por tipo", tipoPersonaId);
        List<Persona> personas = personaService.obtenerPorTipo(tipoPersonaId);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener estudiantes
     */
    @GetMapping("/estudiantes")
    public ResponseEntity<List<Persona>> obtenerEstudiantes() {
        log.info("GET /api/personas/estudiantes - Obteniendo estudiantes");
        List<Persona> estudiantes = personaService.obtenerEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
    
    /**
     * Obtener profesores
     */
    @GetMapping("/profesores")
    public ResponseEntity<List<Persona>> obtenerProfesores() {
        log.info("GET /api/personas/profesores - Obteniendo profesores");
        List<Persona> profesores = personaService.obtenerProfesores();
        return ResponseEntity.ok(profesores);
    }
    
    /**
     * Verificar si existe persona por RUT
     */
    @GetMapping("/existe/rut/{rut}")
    public ResponseEntity<Boolean> existePorRut(@PathVariable String rut) {
        log.info("GET /api/personas/existe/rut/{} - Verificando existencia por RUT", rut);
        boolean existe = personaService.existePorRut(rut);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Verificar si existe persona por email
     */
    @GetMapping("/existe/email/{email}")
    public ResponseEntity<Boolean> existePorEmail(@PathVariable String email) {
        log.info("GET /api/personas/existe/email/{} - Verificando existencia por email", email);
        boolean existe = personaService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }
}