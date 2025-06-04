package com.edutech.service;

import com.edutech.model.Persona;
import com.edutech.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PersonaService {
    
    private final PersonaRepository personaRepository;
    
    /**
     * Obtener todas las personas con paginación
     */
    @Transactional(readOnly = true)
    public Page<Persona> obtenerTodas(Pageable pageable) {
        log.debug("Obteniendo todas las personas con paginación");
        return personaRepository.findAll(pageable);
    }
    
    /**
     * Obtener todas las personas sin paginación
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerTodas() {
        log.debug("Obteniendo todas las personas");
        return personaRepository.findAll();
    }
    
    /**
     * Obtener persona por ID
     */
    @Transactional(readOnly = true)
    public Optional<Persona> obtenerPorId(Long id) {
        log.debug("Obteniendo persona con ID: {}", id);
        return personaRepository.findById(id);
    }
    
    /**
     * Obtener persona por email
     */
    @Transactional(readOnly = true)
    public Optional<Persona> obtenerPorEmail(String email) {
        log.debug("Obteniendo persona por email: {}", email);
        return personaRepository.findByEmailIgnoreCase(email);
    }
    
    /**
     * Obtener persona por RUT
     */
    @Transactional(readOnly = true)
    public Optional<Persona> obtenerPorRut(String rut) {
        log.debug("Obteniendo persona por RUT: {}", rut);
        return personaRepository.findByRut(rut);
    }
    
    /**
     * Crear nueva persona
     */
    public Persona crear(Persona persona) {
        log.debug("Creando nueva persona: {}", persona.getNombres());
        return personaRepository.save(persona);
    }
    
    /**
     * Actualizar persona existente
     */
    public Optional<Persona> actualizar(Long id, Persona personaActualizada) {
        log.debug("Actualizando persona con ID: {}", id);
        return personaRepository.findById(id)
                .map(personaExistente -> {
                    personaExistente.setNombres(personaActualizada.getNombres());
                    personaExistente.setApellidoPaterno(personaActualizada.getApellidoPaterno());
                    personaExistente.setApellidoMaterno(personaActualizada.getApellidoMaterno());
                    personaExistente.setCorreo(personaActualizada.getCorreo());
                    personaExistente.setTelefono(personaActualizada.getTelefono());
                    personaExistente.setTipoPersona(personaActualizada.getTipoPersona());
                    personaExistente.setActivo(personaActualizada.getActivo());
                    return personaRepository.save(personaExistente);
                });
    }
    
    /**
     * Eliminar persona
     */
    public boolean eliminar(Long id) {
        log.debug("Eliminando persona con ID: {}", id);
        if (personaRepository.existsById(id)) {
            personaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Buscar personas por nombre o apellido
     */
    @Transactional(readOnly = true)
    public List<Persona> buscarPorNombreOApellido(String termino) {
        log.debug("Buscando personas por término: {}", termino);
        return personaRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(termino, termino);
    }
    
    /**
     * Obtener personas por tipo
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerPorTipo(Long tipoPersonaId) {
        log.debug("Obteniendo personas por tipo: {}", tipoPersonaId);
        return personaRepository.findByTipoPersonaId(tipoPersonaId);
    }
    
    /**
     * Obtener estudiantes
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerEstudiantes() {
        log.debug("Obteniendo estudiantes");
        return personaRepository.findByTipoPersonaNombreIgnoreCase("ESTUDIANTE");
    }
    
    /**
     * Obtener profesores
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerProfesores() {
        log.debug("Obteniendo profesores");
        return personaRepository.findByTipoPersonaNombreIgnoreCase("PROFESOR");
    }
    
    /**
     * Verificar si existe persona por RUT
     */
    @Transactional(readOnly = true)
    public boolean existePorRut(String rut) {
        return personaRepository.existsByRut(rut);
    }
    
    /**
     * Verificar si existe persona por email
     */
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return personaRepository.existsByEmailIgnoreCase(email);
    }
}
