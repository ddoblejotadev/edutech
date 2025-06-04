package com.edutech.service;

import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;
import com.edutech.repository.InscripcionRepository;
import com.edutech.repository.PersonaRepository;
import com.edutech.repository.EjecucionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InscripcionService {
    
    private final InscripcionRepository inscripcionRepository;
    private final PersonaRepository personaRepository;
    private final EjecucionRepository ejecucionRepository;
    
    /**
     * Obtener todas las inscripciones
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerTodas() {
        log.debug("Obteniendo todas las inscripciones");
        return inscripcionRepository.findAll();
    }
    
    /**
     * Obtener inscripción por ID
     */
    @Transactional(readOnly = true)
    public Optional<Inscripcion> obtenerPorId(Long id) {
        log.debug("Obteniendo inscripción con ID: {}", id);
        return inscripcionRepository.findById(id);
    }
    
    /**
     * Crear nueva inscripción
     */
    public Inscripcion crear(Inscripcion inscripcion) {
        log.debug("Creando nueva inscripción");
        
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
        log.debug("Actualizando inscripción con ID: {}", id);
        
        return inscripcionRepository.findById(id)
                .map(inscripcionExistente -> {
                    inscripcionExistente.setEjecucion(inscripcionActualizada.getEjecucion());
                    inscripcionExistente.setEstudiante(inscripcionActualizada.getEstudiante());
                    inscripcionExistente.setEstado(inscripcionActualizada.getEstado());
                    inscripcionExistente.setNotaFinal(inscripcionActualizada.getNotaFinal());
                    inscripcionExistente.setActivo(inscripcionActualizada.getActivo());
                    return inscripcionRepository.save(inscripcionExistente);
                });
    }
    
    /**
     * Eliminar inscripción
     */
    public boolean eliminar(Long id) {
        log.debug("Eliminando inscripción con ID: {}", id);
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
        log.debug("Inscribiendo estudiante {} en ejecución {}", estudianteId, ejecucionId);
        
        // Verificar que el estudiante existe
        Persona estudiante = personaRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        
        // Verificar que la ejecución existe
        var ejecucion = ejecucionRepository.findById(ejecucionId)
                .orElseThrow(() -> new IllegalArgumentException("Ejecución no encontrada"));
        
        // Verificar que no esté ya inscrito
        if (inscripcionRepository.existsByEstudianteIdAndEjecucionId(estudianteId, ejecucionId)) {
            throw new IllegalStateException("El estudiante ya está inscrito en esta ejecución");
        }
        
        // Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudiante(estudiante);
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
        log.debug("Cancelando inscripción con ID: {}", inscripcionId);
        
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
        log.debug("Cancelando inscripción de estudiante {} en ejecución {}", estudianteId, ejecucionId);
        
        Inscripcion inscripcion = inscripcionRepository.findByEstudianteIdAndEjecucionId(estudianteId, ejecucionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
        
        inscripcion.setEstado("CANCELADA");
        inscripcion.setActivo(false);
        inscripcionRepository.save(inscripcion);
    }
    
    /**
     * Obtener inscripciones por estudiante
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPorEstudiante(Long estudianteId) {
        log.debug("Obteniendo inscripciones del estudiante: {}", estudianteId);
        return inscripcionRepository.findByEstudianteId(estudianteId);
    }
    
    /**
     * Obtener inscripciones por ejecución
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPorEjecucion(Long ejecucionId) {
        log.debug("Obteniendo inscripciones de la ejecución: {}", ejecucionId);
        return inscripcionRepository.findByEjecucionId(ejecucionId);
    }
    
    /**
     * Obtener inscripciones activas de un estudiante
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerActivasDeEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteIdAndActivoTrue(estudianteId);
    }
    
    /**
     * Obtener inscripciones futuras de un estudiante
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerFuturasDeEstudiante(Long estudianteId) {
        return inscripcionRepository.findInscripcionesFuturasByEstudiante(estudianteId);
    }
    
    /**
     * Obtener inscripciones pasadas de un estudiante
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPasadasDeEstudiante(Long estudianteId) {
        return inscripcionRepository.findInscripcionesPasadasByEstudiante(estudianteId);
    }
    
    /**
     * Obtener estudiantes inscritos en una ejecución
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerEstudiantesInscritos(Long ejecucionId) {
        return inscripcionRepository.findEstudiantesByEjecucionId(ejecucionId);
    }
    
    /**
     * Obtener inscripciones por curso
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPorCurso(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId);
    }
    
    /**
     * Contar inscripciones por ejecución
     */
    @Transactional(readOnly = true)
    public Integer contarPorEjecucion(Long ejecucionId) {
        return inscripcionRepository.countByEjecucionId(ejecucionId);
    }
    
    /**
     * Contar inscripciones por estudiante
     */
    @Transactional(readOnly = true)
    public Integer contarPorEstudiante(Long estudianteId) {
        return inscripcionRepository.countByEstudianteId(estudianteId);
    }
    
    /**
     * Verificar si estudiante está inscrito en ejecución
     */
    @Transactional(readOnly = true)
    public boolean estaInscrito(Long estudianteId, Long ejecucionId) {
        return inscripcionRepository.existsByEstudianteIdAndEjecucionIdAndActivoTrue(estudianteId, ejecucionId);
    }
    
    /**
     * Verificar si estudiante está inscrito en alguna ejecución de un curso
     */
    @Transactional(readOnly = true)
    public boolean estaInscritoEnCurso(Long estudianteId, Long cursoId) {
        return inscripcionRepository.existsByEstudianteIdAndCursoId(estudianteId, cursoId);
    }
    
    /**
     * Obtener últimas inscripciones
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerUltimas() {
        return inscripcionRepository.findTop10ByOrderByFechaInscripcionDesc();
    }
    
    // Métodos de validación privados
    
    private void validarInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getEstudiante() == null || inscripcion.getEstudiante().getId() == null) {
            throw new IllegalArgumentException("El estudiante es obligatorio");
        }
        
        if (inscripcion.getEjecucion() == null || inscripcion.getEjecucion().getId() == null) {
            throw new IllegalArgumentException("La ejecución es obligatoria");
        }
    }
}
