package com.edutech.service;

import com.edutech.model.Persona;
import com.edutech.model.TipoPersona;
import com.edutech.repository.PersonaRepository;
import com.edutech.repository.TipoPersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PersonaService {
    
    private final PersonaRepository personaRepository;
    private final TipoPersonaRepository tipoPersonaRepository;
    
    /**
     * Obtener todas las personas con paginación
     */
    @Transactional(readOnly = true)
    public Page<Persona> obtenerTodas(Pageable pageable) {
        log.debug("Obteniendo todas las personas con paginación");
        return personaRepository.findAll(pageable);
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
     * Obtener persona por RUT
     */
    @Transactional(readOnly = true)
    public Optional<Persona> obtenerPorRut(String rut) {
        log.debug("Obteniendo persona con RUT: {}", rut);
        return personaRepository.findByRut(rut);
    }
    
    /**
     * Obtener persona por email
     */
    @Transactional(readOnly = true)
    public Optional<Persona> obtenerPorEmail(String email) {
        log.debug("Obteniendo persona con email: {}", email);
        return personaRepository.findByEmailIgnoreCase(email);
    }
    
    /**
     * Crear nueva persona
     */
    public Persona crear(Persona persona) {
        log.debug("Creando nueva persona: {} {}", persona.getNombres(), persona.getApellidos());
        
        // Validaciones de negocio
        validarPersonaUnica(persona);
        validarTipoPersona(persona.getTipoPersona().getId());
        
        return personaRepository.save(persona);
    }
    
    /**
     * Actualizar persona existente
     */
    public Persona actualizar(Long id, Persona personaActualizada) {
        log.debug("Actualizando persona con ID: {}", id);
        
        return personaRepository.findById(id)
                .map(personaExistente -> {
                    // Validar si cambió el RUT o email
                    if (!personaExistente.getRut().equals(personaActualizada.getRut())) {
                        if (personaRepository.existsByRut(personaActualizada.getRut())) {
                            throw new IllegalArgumentException("Ya existe una persona con el RUT: " + personaActualizada.getRut());
                        }
                    }
                    
                    if (!personaExistente.getEmail().equalsIgnoreCase(personaActualizada.getEmail())) {
                        if (personaRepository.existsByEmailIgnoreCase(personaActualizada.getEmail())) {
                            throw new IllegalArgumentException("Ya existe una persona con el email: " + personaActualizada.getEmail());
                        }
                    }
                    
                    // Validar tipo de persona
                    if (!personaExistente.getTipoPersona().getId().equals(personaActualizada.getTipoPersona().getId())) {
                        validarTipoPersona(personaActualizada.getTipoPersona().getId());
                    }
                    
                    // Actualizar campos
                    personaExistente.setRut(personaActualizada.getRut());
                    personaExistente.setNombres(personaActualizada.getNombres());
                    personaExistente.setApellidos(personaActualizada.getApellidos());
                    personaExistente.setEmail(personaActualizada.getEmail());
                    personaExistente.setTelefono(personaActualizada.getTelefono());
                    personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());
                    personaExistente.setDireccion(personaActualizada.getDireccion());
                    personaExistente.setTipoPersona(personaActualizada.getTipoPersona());
                    
                    return personaRepository.save(personaExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar persona
     */
    public void eliminar(Long id) {
        log.debug("Eliminando persona con ID: {}", id);
        
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));
        
        // Verificar que no tenga inscripciones o calificaciones asociadas
        // (esto se puede expandir según las reglas de negocio)
        
        personaRepository.delete(persona);
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
        log.debug("Obteniendo personas del tipo: {}", tipoPersonaId);
        return personaRepository.findByTipoPersonaId(tipoPersonaId);
    }
    
    /**
     * Obtener estudiantes (asumiendo que existe un tipo "Estudiante")
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerEstudiantes() {
        return tipoPersonaRepository.findByNombreIgnoreCase("Estudiante")
                .map(tipo -> personaRepository.findByTipoPersona(tipo))
                .orElse(List.of());
    }
    
    /**
     * Obtener profesores (asumiendo que existe un tipo "Profesor")
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerProfesores() {
        return tipoPersonaRepository.findByNombreIgnoreCase("Profesor")
                .map(tipo -> personaRepository.findByTipoPersona(tipo))
                .orElse(List.of());
    }
    
    /**
     * Buscar personas por rango de edad
     */
    @Transactional(readOnly = true)
    public List<Persona> buscarPorRangoEdad(Integer edadMinima, Integer edadMaxima) {
        LocalDate fechaMaxima = LocalDate.now().minusYears(edadMinima);
        LocalDate fechaMinima = LocalDate.now().minusYears(edadMaxima + 1);
        
        return personaRepository.findByFechaNacimientoBetween(fechaMinima, fechaMaxima);
    }
    
    /**
     * Buscar personas por email parcial
     */
    @Transactional(readOnly = true)
    public List<Persona> buscarPorEmail(String emailParcial) {
        return personaRepository.findByEmailContainingIgnoreCase(emailParcial);
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
    
    /**
     * Contar personas por tipo
     */
    @Transactional(readOnly = true)
    public Long contarPorTipo(Long tipoPersonaId) {
        return personaRepository.countByTipoPersonaId(tipoPersonaId);
    }
    
    /**
     * Obtener personas ordenadas por nombre completo
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerOrdenadasPorNombre() {
        return personaRepository.findAllByOrderByNombresAscApellidosAsc();
    }
    
    // Métodos de validación privados
    
    private void validarPersonaUnica(Persona persona) {
        if (personaRepository.existsByRut(persona.getRut())) {
            throw new IllegalArgumentException("Ya existe una persona con el RUT: " + persona.getRut());
        }
        
        if (personaRepository.existsByEmailIgnoreCase(persona.getEmail())) {
            throw new IllegalArgumentException("Ya existe una persona con el email: " + persona.getEmail());
        }
    }
    
    private void validarTipoPersona(Long tipoPersonaId) {
        if (!tipoPersonaRepository.existsById(tipoPersonaId)) {
            throw new IllegalArgumentException("Tipo de persona no encontrado con ID: " + tipoPersonaId);
        }
    }
}
