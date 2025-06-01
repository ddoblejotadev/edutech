package com.edutech.microservicio_persona.service;

import com.edutech.microservicio_persona.model.Persona;
import com.edutech.microservicio_persona.repository.PersonaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    @Autowired
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Persona> findAll() {
        return personaRepository.findAll();
    }    public Optional<Persona> findByRut(String rut) {
        return personaRepository.findByRut(rut);
    }

    public Optional<Persona> findByCorreo(String correo) {
        return personaRepository.findByCorreo(correo);
    }

    public List<Persona> findByTipoPersona(Integer idTipoPersona) {
        return personaRepository.findByTipoPersonaIdTipoPersona(idTipoPersona);
    }
    
    public List<Persona> buscarPorNombre(String nombre) {
        return personaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Persona save(Persona persona) {
        return personaRepository.save(persona);
    }    @Transactional
    public void deleteByRut(String rut) {
        personaRepository.deleteByRut(rut);
    }

    @Transactional
    public void deleteById(Integer id) {
        personaRepository.deleteById(id);
    }
}
