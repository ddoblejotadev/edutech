package com.edutech.service;

//Importaciones del model y repository
import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;
import com.edutech.repository.InscripcionRepository;
import com.edutech.repository.PersonaRepository;
import com.edutech.repository.EjecucionRepository;

//Importacion para dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
//Importaciones Java
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InscripcionService {
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private EjecucionRepository ejecucionRepository;
    
    /**
     * Obtener todas las inscripciones
     */
    public List<Inscripcion> obtenerTodas() {
        return inscripcionRepository.findAll();
    }
    
    /**
     * Obtener inscripción por ID
     */
    public Optional<Inscripcion> obtenerPorId(Long id) {
        return inscripcionRepository.findById(id);
    }
    
    /**
     * Crear nueva inscripción
     */
    public Inscripcion crear(Inscripcion inscripcion) {
        
        // Validaciones básicas
        validarInscripcion(inscripcion);
        
        // Establecer fecha de inscripción
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        
        return inscripcionRepository.save(inscripcion);
    }
    
    /**
     * Actualizar inscripción existente
     */
    public Optional<Inscripcion> actualizar(Long id, Inscripcion inscripcionActualizada) {
        
        return inscripcionRepository.findById(id)
                .map(inscripcionExistente -> {
                    inscripcionExistente.setEjecucion(inscripcionActualizada.getEjecucion());
                    inscripcionExistente.setPersona(inscripcionActualizada.getPersona());
                    inscripcionExistente.setEstado(inscripcionActualizada.getEstado());
                    inscripcionExistente.setActivo(inscripcionActualizada.getActivo());
                    return inscripcionRepository.save(inscripcionExistente);
                });
    }
    
    /**
     * Eliminar inscripción
     */
    public boolean eliminar(Long id) {
        if (inscripcionRepository.existsById(id)) {
            inscripcionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Inscribir estudiante en una ejecución
     */
    public Inscripcion inscribir(Long estudianteId, Long ejecucionId) {
        
        // Verificar que el estudiante existe
        Persona estudiante = personaRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        
        // Verificar que la ejecución existe
        var ejecucion = ejecucionRepository.findById(ejecucionId)
                .orElseThrow(() -> new IllegalArgumentException("Ejecución no encontrada"));
        
        // Verificar que no esté ya inscrito
        if (inscripcionRepository.existsByPersonaIdAndEjecucionId(estudianteId, ejecucionId)) {
            throw new IllegalStateException("El estudiante ya está inscrito en esta ejecución");
        }

        // NUEVA VALIDACIÓN: Verificar que la fecha de inicio no sea futura
        if (ejecucion.getFechaInicio().isAfter(LocalDate.now())) {
            throw new IllegalStateException("No se puede inscribir a una ejecución que aún no ha comenzado");
        }
 
        // Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setPersona(estudiante);
        inscripcion.setEjecucion(ejecucion);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado("ACTIVA");
        inscripcion.setActivo(true);
        
        return inscripcionRepository.save(inscripcion);
    }
    
    /**
     * Cancelar inscripción por ID
     */
    public void cancelarInscripcion(Long inscripcionId) {
        
        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
        
        inscripcion.setEstado("CANCELADA");
        inscripcion.setActivo(false);
        inscripcionRepository.save(inscripcion);
    }
    
    /**
     * Cancelar inscripción por estudiante y ejecución
     */
    public void cancelarInscripcion(Long estudianteId, Long ejecucionId) {
        
        Inscripcion inscripcion = inscripcionRepository.findByPersonaIdAndEjecucionId(estudianteId, ejecucionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
        
        inscripcion.setEstado("CANCELADA");
        inscripcion.setActivo(false);
        inscripcionRepository.save(inscripcion);
    }
    
    /**
     * Obtener inscripciones por estudiante
     */
    public List<Inscripcion> obtenerPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByPersonaId(estudianteId);
    }
    
    /**
     * Obtener inscripciones por ejecución
     */
    public List<Inscripcion> obtenerPorEjecucion(Long ejecucionId) {
        return inscripcionRepository.findByEjecucionId(ejecucionId);
    }
    
    /**
     * Obtener inscripciones activas de un estudiante
     */
    public List<Inscripcion> obtenerActivasDeEstudiante(Long estudianteId) {
        return inscripcionRepository.findByPersonaIdAndActivoTrue(estudianteId);
    }
    
    /**
     * Obtener inscripciones futuras de un estudiante
     */
    public List<Inscripcion> obtenerFuturasDeEstudiante(Long estudianteId) {
        return inscripcionRepository.findInscripcionesFuturasByEstudiante(estudianteId);
    }
    
    /**
     * Obtener inscripciones pasadas de un estudiante
     */
    public List<Inscripcion> obtenerPasadasDeEstudiante(Long estudianteId) {
        return inscripcionRepository.findInscripcionesPasadasByEstudiante(estudianteId);
    }
    
    /**
     * Obtener estudiantes inscritos en una ejecución
     */
    public List<Persona> obtenerEstudiantesInscritos(Long ejecucionId) {
        return inscripcionRepository.findEstudiantesByEjecucionId(ejecucionId);
    }
    
    /**
     * Obtener inscripciones por curso
     */
    public List<Inscripcion> obtenerPorCurso(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId);
    }
    
    /**
     * Contar inscripciones por ejecución
     */
    public Integer contarPorEjecucion(Long ejecucionId) {
        return inscripcionRepository.countByEjecucionId(ejecucionId);
    }
    
    /**
     * Contar inscripciones por estudiante
     */
    public Integer contarPorEstudiante(Long estudianteId) {
        return inscripcionRepository.countByPersonaId(estudianteId);
    }
    
    /**
     * Verificar si estudiante está inscrito en ejecución
     */
    public boolean estaInscrito(Long estudianteId, Long ejecucionId) {
        return inscripcionRepository.existsByPersonaIdAndEjecucionId(estudianteId, ejecucionId);
    }
    
    /**
     * Verificar si estudiante está inscrito activo en ejecución
     */
    public boolean estaInscritoActivo(Long estudianteId, Long ejecucionId) {
        return inscripcionRepository.existsByPersonaIdAndEjecucionIdAndActivoTrue(estudianteId, ejecucionId);
    }
    
    /**
     * Verificar si estudiante está inscrito en alguna ejecución de un curso
     */
    public boolean estaInscritoEnCurso(Long estudianteId, Long cursoId) {
        return inscripcionRepository.existsByPersonaIdAndCursoId(estudianteId, cursoId);
    }
    
    /**
     * Obtener últimas inscripciones
     */
    public List<Inscripcion> obtenerUltimas() {
        return inscripcionRepository.findTop10ByOrderByFechaInscripcionDesc();
    }
    
    // Métodos de validación privados
    
    private void validarInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getPersona() == null || inscripcion.getPersona().getId() == null) {
            throw new IllegalArgumentException("El estudiante es obligatorio");
        }
        
        if (inscripcion.getEjecucion() == null || inscripcion.getEjecucion().getId() == null) {
            throw new IllegalArgumentException("La ejecución es obligatoria");
        }
    }
}
