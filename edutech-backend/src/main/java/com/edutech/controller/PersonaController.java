package com.edutech.controller;

//Importaciones Modelo y Service
import com.edutech.model.Persona;
import com.edutech.service.PersonaService;

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
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
public class PersonaController {

    @Autowired
    private PersonaService personaService;
    
    /**
     * Obtener todas las personas con paginación
     */
    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodas() {
        List<Persona> personas = personaService.obtenerTodas();
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener persona por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        return personaService.obtenerPorId(id)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener persona por RUT
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Persona> obtenerPorRut(@PathVariable String rut) {
        return personaService.obtenerPorRut(rut)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener persona por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Persona> obtenerPorEmail(@PathVariable String email) {
        return personaService.obtenerPorEmail(email)
                .map(persona -> ResponseEntity.ok(persona))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva persona
     */
    @PostMapping
    public ResponseEntity<Persona> crearPersona(@RequestBody Persona persona) {
        try {
            Persona nuevaPersona = personaService.crear(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar persona existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizarPersona(@PathVariable Long id, @RequestBody Persona personaActualizada) {
        try {
            return personaService.actualizar(id, personaActualizada)
                    .map(persona -> ResponseEntity.ok(persona))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar persona
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            personaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Buscar personas por nombre o apellido
     */
    @GetMapping("/buscar/{termino}")
    public ResponseEntity<List<Persona>> buscarPorNombreOApellido(@PathVariable String termino) {
        List<Persona> personas = personaService.buscarPorNombreOApellido(termino);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener personas por tipo
     */
    @GetMapping("/tipo/{tipoPersonaId}")
    public ResponseEntity<List<Persona>> obtenerPorTipo(@PathVariable Long tipoPersonaId) {
        List<Persona> personas = personaService.obtenerPorTipo(tipoPersonaId);
        return ResponseEntity.ok(personas);
    }
    
    /**
     * Obtener estudiantes
     */
    @GetMapping("/estudiantes")
    public ResponseEntity<List<Persona>> obtenerEstudiantes() {
        List<Persona> estudiantes = personaService.obtenerEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
    
    /**
     * Obtener profesores
     */
    @GetMapping("/profesores")
    public ResponseEntity<List<Persona>> obtenerProfesores() {
        List<Persona> profesores = personaService.obtenerProfesores();
        return ResponseEntity.ok(profesores);
    }
    
    /**
     * Verificar si existe persona por RUT
     */
    @GetMapping("/existe/rut/{rut}")
    public ResponseEntity<Boolean> existePorRut(@PathVariable String rut) {
        boolean existe = personaService.existePorRut(rut);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Verificar si existe persona por email
     */
    @GetMapping("/existe/email/{email}")
    public ResponseEntity<Boolean> existePorEmail(@PathVariable String email) {
        boolean existe = personaService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }
}