package com.edutech.microservicio_persona.controller;

import com.edutech.microservicio_persona.model.Persona;
import com.edutech.microservicio_persona.service.PersonaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    
    @Autowired
    private PersonaService personaService;

  
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas() {
        List<Persona> personas = personaService.findAll();
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<Persona> getPersonaByRut(@PathVariable String rut) {
        return personaService.findByRut(rut)
                .map(persona -> new ResponseEntity<>(persona, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Persona> getPersonaByCorreo(@PathVariable String correo) {
        return personaService.findByCorreo(correo)
                .map(persona -> new ResponseEntity<>(persona, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/tipo/{idTipoPersona}")
    public ResponseEntity<List<Persona>> getPersonasByTipoPersona(@PathVariable Integer idTipoPersona) {
        List<Persona> personas = personaService.findByTipoPersona(idTipoPersona);
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }
    
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<Persona>> buscarPersonasPorNombre(@PathVariable String nombre) {
        List<Persona> personas = personaService.buscarPorNombre(nombre);
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        // Verificar si ya existe una persona con ese correo
        if (persona.getCorreo() != null && personaService.existsByCorreo(persona.getCorreo())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        Persona savedPersona = personaService.save(persona);
        return new ResponseEntity<>(savedPersona, HttpStatus.CREATED);
    }

    @PutMapping("/{rut}")
    public ResponseEntity<Persona> updatePersona(@PathVariable String rut, @RequestBody Persona persona) {
        return personaService.findByRut(rut)
                .map(existingPersona -> {
                    persona.setRut(rut);
                    Persona updatedPersona = personaService.save(persona);
                    return new ResponseEntity<>(updatedPersona, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> deletePersona(@PathVariable String rut) {
        return personaService.findByRut(rut)
                .map(persona -> {
                    personaService.deleteByRut(rut);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
