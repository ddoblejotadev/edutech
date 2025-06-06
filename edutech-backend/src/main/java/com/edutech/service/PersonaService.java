package com.edutech.service;

//Importaciones del model y repository
import com.edutech.model.Persona;
import com.edutech.repository.PersonaRepository;

//Importacion para dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Importaciones Java
import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;
    
    /**
     * Obtener todas las personas
     */
    public List<Persona> obtenerTodas() {
        return personaRepository.findAll();
    }
    
    /**
     * Obtener persona por ID
     */
    public Optional<Persona> obtenerPorId(Long id) {
        return personaRepository.findById(id);
    }
    
    /**
     * Obtener persona por RUT
     */
    public Optional<Persona> obtenerPorRut(String rut) {
        return personaRepository.findByRut(rut);
    }
    
    /**
     * Obtener persona por email
     */
    public Optional<Persona> obtenerPorEmail(String email) {
        return personaRepository.findByCorreoIgnoreCase(email);
    }
    
    /**
     * Crear nueva persona
     */
    public Persona crear(Persona persona) {
        validarPersona(persona);
        
        if (personaRepository.existsByRut(persona.getRut())) {
            throw new IllegalArgumentException("Ya existe una persona con el RUT: " + persona.getRut());
        }
        
        return personaRepository.save(persona);
    }
    
    /**
     * Actualizar persona existente
     */
    public Optional<Persona> actualizar(Long id, Persona personaActualizada) {
        return personaRepository.findById(id)
                .map(personaExistente -> {
                    validarPersona(personaActualizada);
                    
                    Optional<Persona> personaConMismoRut = personaRepository.findByRut(personaActualizada.getRut());
                    if (personaConMismoRut.isPresent() && !personaConMismoRut.get().getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe una persona con el RUT: " + personaActualizada.getRut());
                    }
                    
                    personaExistente.setRut(personaActualizada.getRut());
                    personaExistente.setNombres(personaActualizada.getNombres());
                    personaExistente.setApellidos(personaActualizada.getApellidos());
                    personaExistente.setEmail(personaActualizada.getEmail());
                    personaExistente.setTelefono(personaActualizada.getTelefono());
                    personaExistente.setDireccion(personaActualizada.getDireccion());
                    personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());
                    personaExistente.setTipoPersona(personaActualizada.getTipoPersona());
                    personaExistente.setActivo(personaActualizada.getActivo());
                    
                    return personaRepository.save(personaExistente);
                });
    }
    
    /**
     * Eliminar persona
     */
    public void eliminar(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));
        
        personaRepository.delete(persona);
    }
    
    /**
     * Buscar personas por nombre o apellido
     */
    public List<Persona> buscarPorNombreOApellido(String termino) {
        // Usar la consulta personalizada que ya maneja la búsqueda completa
        return personaRepository.buscarPersonas(termino);
    }
    
    /**
     * Obtener personas por tipo
     */
    public List<Persona> obtenerPorTipo(Long tipoPersonaId) {
        return personaRepository.findByTipoPersonaId(tipoPersonaId);
    }
    
    /**
     * Obtener estudiantes
     */
    public List<Persona> obtenerEstudiantes() {
        return personaRepository.findEstudiantes();
    }
    
    /**
     * Obtener profesores
     */
    public List<Persona> obtenerProfesores() {
        return personaRepository.findProfesores();
    }
    
    /**
     * Verificar si existe persona por RUT
     */
    public boolean existePorRut(String rut) {
        return personaRepository.existsByRut(rut);
    }
    
    /**
     * Verificar si existe persona por email
     */
    public boolean existePorEmail(String email) {
        return personaRepository.existsByCorreoIgnoreCase(email);
    }
    
    // Métodos de validación privados
    
    private void validarPersona(Persona persona) {
        if (persona.getRut() == null || persona.getRut().trim().isEmpty()) {
            throw new IllegalArgumentException("El RUT es obligatorio");
        }
        
        if (persona.getNombres() == null || persona.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (persona.getApellidos() == null || persona.getApellidos().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        
        if (persona.getEmail() == null || persona.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        
        if (persona.getTipoPersona() == null || persona.getTipoPersona().getId() == null) {
            throw new IllegalArgumentException("El tipo de persona es obligatorio");
        }
    }
}
