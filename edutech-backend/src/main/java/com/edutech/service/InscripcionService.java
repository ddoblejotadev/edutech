package com.edutech.service;

import com.edutech.model.Inscripcion;
import com.edutech.model.Ejecucion;
import com.edutech.model.Persona;
import com.edutech.repository.InscripcionRepository;
import com.edutech.repository.EjecucionRepository;
import com.edutech.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InscripcionService {
    
    private final InscripcionRepository inscripcionRepository;
    private final EjecucionRepository ejecucionRepository;
    private final PersonaRepository personaRepository;
    
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
     * Inscribir estudiante en una ejecución
     */
    public Inscripcion inscribir(Long estudianteId, Long ejecucionId) {
        log.debug("Inscribiendo estudiante {} en ejecución {}", estudianteId, ejecucionId);
        
        // Validar que el estudiante y la ejecución existan
        Persona estudiante = personaRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId));
        
        Ejecucion ejecucion = ejecucionRepository.findById(ejecucionId)
                .orElseThrow(() -> new IllegalArgumentException("Ejecución no encontrada con ID: " + ejecucionId));
        
        // Validaciones de negocio
        validarInscripcion(estudiante, ejecucion);
        
        // Crear la inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setEjecucion(ejecucion);
        inscripcion.setFechaInscripcion(LocalDate.now());
        
        return inscripcionRepository.save(inscripcion);
    }
    
    /**
     * Crear inscripción con fecha específica
     */
    public Inscripcion crear(Inscripcion inscripcion) {
        log.debug("Creando inscripción para estudiante {} en ejecución {}", 
                  inscripcion.getEstudiante().getId(), inscripcion.getEjecucion().getId());
        
        // Validar que el estudiante y la ejecución existan
        validarEstudianteYEjecucion(inscripcion.getEstudiante().getId(), inscripcion.getEjecucion().getId());
        
        // Validaciones de negocio
        validarInscripcion(inscripcion.getEstudiante(), inscripcion.getEjecucion());
        
        return inscripcionRepository.save(inscripcion);
    }
    
    /**
     * Cancelar inscripción
     */
    public void cancelarInscripcion(Long id) {
        log.debug("Cancelando inscripción con ID: {}", id);
        
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada con ID: " + id));
        
        // Verificar que la ejecución no haya comenzado
        if (inscripcion.getEjecucion().getFecha

Inicio().isBefore(LocalDate.now()) || 
            inscripcion.getEjecucion().getFechaInicio().isEqual(LocalDate.now())) {
            throw new IllegalStateException("No se puede cancelar la inscripción porque la ejecución ya comenzó");
        }
        
        inscripcionRepository.delete(inscripcion);
    }
    
    /**
     * Cancelar inscripción por estudiante y ejecución
     */
    public void cancelarInscripcion(Long estudianteId, Long ejecucionId) {
        log.debug("Cancelando inscripción del estudiante {} en ejecución {}", estudianteId, ejecucionId);
        
        Inscripcion inscripcion = inscripcionRepository.findByEstudianteIdAndEjecucionId(estudianteId, ejecucionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
        
        cancelarInscripcion(inscripcion.getId());
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
        log.debug("Obteniendo inscripciones activas del estudiante: {}", estudianteId);
        return inscripcionRepository.findInscripcionesActivasDeEstudiante(estudianteId);
    }
    
    /**
     * Obtener inscripciones futuras de un estudiante
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerFuturasDeEstudiante(Long estudianteId) {
        log.debug("Obteniendo inscripciones futuras del estudiante: {}", estudianteId);
        return inscripcionRepository.findInscripcionesFuturasDeEstudiante(estudianteId);
    }
    
    /**
     * Obtener inscripciones pasadas de un estudiante
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPasadasDeEstudiante(Long estudianteId) {
        log.debug("Obteniendo inscripciones pasadas del estudiante: {}", estudianteId);
        return inscripcionRepository.findInscripcionesPasadasDeEstudiante(estudianteId);
    }
    
    /**
     * Obtener inscripciones por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        log.debug("Obteniendo inscripciones entre {} y {}", fechaInicio, fechaFin);
        return inscripcionRepository.findByFechaInscripcionBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener estudiantes inscritos en una ejecución
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerEstudiantesInscritos(Long ejecucionId) {
        log.debug("Obteniendo estudiantes inscritos en ejecución: {}", ejecucionId);
        return inscripcionRepository.findEstudiantesInscritosEnEjecucion(ejecucionId);
    }
    
    /**
     * Obtener inscripciones por curso
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPorCurso(Long cursoId) {
        log.debug("Obteniendo inscripciones del curso: {}", cursoId);
        return inscripcionRepository.findInscripcionesByCurso(cursoId);
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
        return inscripcionRepository.existsByEstudianteIdAndEjecucionId(estudianteId, ejecucionId);
    }
    
    /**
     * Verificar si estudiante está inscrito en alguna ejecución de un curso
     */
    @Transactional(readOnly = true)
    public boolean estaInscritoEnCurso(Long estudianteId, Long cursoId) {
        return inscripcionRepository.existeInscripcionEnCurso(estudianteId, cursoId);
    }
    
    /**
     * Obtener últimas inscripciones
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerUltimas() {
        log.debug("Obteniendo últimas inscripciones");
        return inscripcionRepository.findUltimasInscripciones();
    }
    
    /**
     * Obtener inscripciones de un estudiante ordenadas por fecha
     */
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPorEstudianteOrdenadas(Long estudianteId) {
        log.debug("Obteniendo inscripciones del estudiante {} ordenadas por fecha", estudianteId);
        return inscripcionRepository.findByEstudianteIdOrderByFechaInscripcionDesc(estudianteId);
    }
    
    // Métodos de validación privados
    
    private void validarInscripcion(Persona estudiante, Ejecucion ejecucion) {
        // Verificar que sea un estudiante
        if (!estudiante.getTipoPersona().getNombre().equalsIgnoreCase("Estudiante")) {
            throw new IllegalArgumentException("Solo los estudiantes pueden inscribirse en cursos");
        }
        
        // Verificar que no esté ya inscrito
        if (inscripcionRepository.existsByEstudianteIdAndEjecucionId(estudiante.getId(), ejecucion.getId())) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esta ejecución");
        }
        
        // Verificar que la ejecución no esté llena
        Integer inscritosActuales = inscripcionRepository.countByEjecucionId(ejecucion.getId());
        if (inscritosActuales >= ejecucion.getCupoMaximo()) {
            throw new IllegalArgumentException("La ejecución está llena (cupo máximo: " + ejecucion.getCupoMaximo() + ")");
        }
        
        // Verificar que la ejecución no haya comenzado
        if (ejecucion.getFechaInicio().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede inscribir en una ejecución que ya comenzó");
        }
        
        // Verificar prerequisitos del curso
        // TODO: Implementar validación de prerequisitos cuando tengamos historial académico
    }
    
    private void validarEstudianteYEjecucion(Long estudianteId, Long ejecucionId) {
        if (!personaRepository.existsById(estudianteId)) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId);
        }
        
        if (!ejecucionRepository.existsById(ejecucionId)) {
            throw new IllegalArgumentException("Ejecución no encontrada con ID: " + ejecucionId);
        }
    }
}
