package com.edutech.controller;

import com.edutech.model.Persona;
import com.edutech.service.PersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<Page<Persona>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/personas - Obteniendo personas con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Persona> personas = personaService.obtenerTodas(pageable);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener persona por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/personas/{} - Obteniendo persona por ID", id);
        return personaService.obtenerPorId(id)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener persona por RUT
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Persona> obtenerPorRut(@PathVariable String rut) {
        log.debug("GET /api/personas/rut/{} - Obteniendo persona por RUT", rut);
        return personaService.obtenerPorRut(rut)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener persona por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Persona> obtenerPorEmail(@PathVariable String email) {
        log.debug("GET /api/personas/email/{} - Obteniendo persona por email", email);
        return personaService.obtenerPorEmail(email)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva persona
     */
    @PostMapping
    public ResponseEntity<Persona> crear(@Valid @RequestBody Persona persona) {
        log.debug("POST /api/personas - Creando nueva persona: {} {}", persona.getNombres(), persona.getApellidos());
        try {
            Persona personaCreada = personaService.crear(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(personaCreada);
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
        log.debug("PUT /api/personas/{} - Actualizando persona", id);
        try {
            Persona personaActualizada = personaService.actualizar(id, persona);
            return ResponseEntity.ok(personaActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar persona: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar persona
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/personas/{} - Eliminando persona", id);
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
    @GetMapping("/buscar")
    public ResponseEntity<List<Persona>> buscarPorNombreOApellido(@RequestParam String termino) {
        log.debug("GET /api/personas/buscar?termino={} - Buscando personas", termino);
        List<Persona> personas = personaService.buscarPorNombreOApellido(termino);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener personas por tipo
     */
    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<Persona>> obtenerPorTipo(@PathVariable Long tipoId) {
        log.debug("GET /api/personas/tipo/{} - Obteniendo personas por tipo", tipoId);
        List<Persona> personas = personaService.obtenerPorTipo(tipoId);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener estudiantes
     */
    @GetMapping("/estudiantes")
    public ResponseEntity<List<Persona>> obtenerEstudiantes() {
        log.debug("GET /api/personas/estudiantes - Obteniendo estudiantes");
        List<Persona> estudiantes = personaService.obtenerEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
    
    /**
     * Obtener profesores
     */
    @GetMapping("/profesores")
    public ResponseEntity<List<Persona>> obtenerProfesores() {
        log.debug("GET /api/personas/profesores - Obteniendo profesores");
        List<Persona> profesores = personaService.obtenerProfesores();
        return ResponseEntity.ok(profesores);
    }
    
    /**
     * Buscar personas por rango de edad
     */
    @GetMapping("/edad")
    public ResponseEntity<List<Persona>> buscarPorRangoEdad(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        log.debug("GET /api/personas/edad?min={}&max={} - Buscando por rango de edad", min, max);
        List<Persona> personas = personaService.buscarPorRangoEdad(min, max);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Buscar personas por email parcial
     */
    @GetMapping("/buscar-email")
    public ResponseEntity<List<Persona>> buscarPorEmail(@RequestParam String email) {
        log.debug("GET /api/personas/buscar-email?email={} - Buscando por email", email);
        List<Persona> personas = personaService.buscarPorEmail(email);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Verificar si existe persona por RUT
     */
    @GetMapping("/existe/rut/{rut}")
    public ResponseEntity<Boolean> existePorRut(@PathVariable String rut) {
        log.debug("GET /api/personas/existe/rut/{} - Verificando existencia por RUT", rut);
        boolean existe = personaService.existePorRut(rut);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Verificar si existe persona por email
     */
    @GetMapping("/existe/email/{email}")
    public ResponseEntity<Boolean> existePorEmail(@PathVariable String email) {
        log.debug("GET /api/personas/existe/email/{} - Verificando existencia por email", email);
        boolean existe = personaService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Contar personas por tipo
     */
    @GetMapping("/count/tipo/{tipoId}")
    public ResponseEntity<Long> contarPorTipo(@PathVariable Long tipoId) {
        log.debug("GET /api/personas/count/tipo/{} - Contando personas por tipo", tipoId);
        Long count = personaService.contarPorTipo(tipoId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Obtener personas ordenadas por nombre
     */
    @GetMapping("/ordenadas")
    public ResponseEntity<List<Persona>> obtenerOrdenadasPorNombre() {
        log.debug("GET /api/personas/ordenadas - Obteniendo personas ordenadas por nombre");
        List<Persona> personas = personaService.obtenerOrdenadasPorNombre();
        return ResponseEntity.ok(personas);
    }
}
