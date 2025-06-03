package com.edutech.service;

import com.edutech.model.Evaluacion;
import com.edutech.model.Ejecucion;
import com.edutech.repository.EvaluacionRepository;
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
public class EvaluacionService {
    
    private final EvaluacionRepository evaluacionRepository;
    private final EjecucionRepository ejecucionRepository;
    
    /**
     * Obtener todas las evaluaciones
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerTodas() {
        log.debug("Obteniendo todas las evaluaciones");
        return evaluacionRepository.findAll();
    }
    
    /**
     * Obtener evaluación por ID
     */
    @Transactional(readOnly = true)
    public Optional<Evaluacion> obtenerPorId(Long id) {
        log.debug("Obteniendo evaluación con ID: {}", id);
        return evaluacionRepository.findById(id);
    }
    
    /**
     * Crear nueva evaluación
     */
    public Evaluacion crear(Evaluacion evaluacion) {
        log.debug("Creando nueva evaluación: {}", evaluacion.getTitulo());
        
        // Validaciones de negocio
        validarEjecucion(evaluacion.getEjecucion().getId());
        validarDatos(evaluacion);
        validarFechaHora(evaluacion.getFechaHoraInicio(), evaluacion.getEjecucion());
        
        return evaluacionRepository.save(evaluacion);
    }
    
    /**
     * Actualizar evaluación existente
     */
    public Evaluacion actualizar(Long id, Evaluacion evaluacionActualizada) {
        log.debug("Actualizando evaluación con ID: {}", id);
        
        return evaluacionRepository.findById(id)
                .map(evaluacionExistente -> {
                    // Validaciones
                    validarEjecucion(evaluacionActualizada.getEjecucion().getId());
                    validarDatos(evaluacionActualizada);
                    
                    // Verificar que no haya comenzado si se cambia la fecha
                    if (!evaluacionExistente.getFechaHoraInicio().equals(evaluacionActualizada.getFechaHoraInicio())) {
                        if (evaluacionExistente.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
                            throw new IllegalStateException("No se puede modificar la fecha de una evaluación que ya comenzó");
                        }
                        validarFechaHora(evaluacionActualizada.getFechaHoraInicio(), evaluacionActualizada.getEjecucion());
                    }
                    
                    // Actualizar campos
                    evaluacionExistente.setTitulo(evaluacionActualizada.getTitulo());
                    evaluacionExistente.setDescripcion(evaluacionActualizada.getDescripcion());
                    evaluacionExistente.setFechaHoraInicio(evaluacionActualizada.getFechaHoraInicio());
                    evaluacionExistente.setDuracionMinutos(evaluacionActualizada.getDuracionMinutos());
                    evaluacionExistente.setPuntajeMaximo(evaluacionActualizada.getPuntajeMaximo());
                    evaluacionExistente.setEjecucion(evaluacionActualizada.getEjecucion());
                    
                    return evaluacionRepository.save(evaluacionExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar evaluación
     */
    public void eliminar(Long id) {
        log.debug("Eliminando evaluación con ID: {}", id);
        
        Evaluacion evaluacion = evaluacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada con ID: " + id));
        
        // Verificar que no haya comenzado
        if (evaluacion.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede eliminar una evaluación que ya comenzó");
        }
        
        // Verificar que no tenga calificaciones asociadas
        // (Esta validación se implementaría con el CalificacionRepository)
        
        evaluacionRepository.delete(evaluacion);
    }
    
    /**
     * Obtener evaluaciones por ejecución
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorEjecucion(Long ejecucionId) {
        log.debug("Obteniendo evaluaciones de la ejecución: {}", ejecucionId);
        return evaluacionRepository.findByEjecucionId(ejecucionId);
    }
    
    /**
     * Obtener evaluaciones por curso
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorCurso(Long cursoId) {
        log.debug("Obteniendo evaluaciones del curso: {}", cursoId);
        return evaluacionRepository.findEvaluacionesByCurso(cursoId);
    }
    
    /**
     * Buscar evaluaciones por título
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> buscarPorTitulo(String titulo) {
        log.debug("Buscando evaluaciones por título: {}", titulo);
        return evaluacionRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    /**
     * Buscar evaluaciones por descripción
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> buscarPorDescripcion(String descripcion) {
        log.debug("Buscando evaluaciones por descripción: {}", descripcion);
        return evaluacionRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }
    
    /**
     * Obtener evaluaciones activas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerActivas() {
        log.debug("Obteniendo evaluaciones activas");
        return evaluacionRepository.findEvaluacionesActivas(LocalDateTime.now());
    }
    
    /**
     * Obtener evaluaciones futuras
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerFuturas() {
        log.debug("Obteniendo evaluaciones futuras");
        return evaluacionRepository.findEvaluacionesFuturas(LocalDateTime.now());
    }
    
    /**
     * Obtener evaluaciones pasadas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPasadas() {
        log.debug("Obteniendo evaluaciones pasadas");
        return evaluacionRepository.findEvaluacionesPasadas(LocalDateTime.now());
    }
    
    /**
     * Obtener evaluaciones por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Obteniendo evaluaciones entre {} y {}", fechaInicio, fechaFin);
        return evaluacionRepository.findByFechaHoraInicioBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener evaluaciones por rango de duración
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorRangoDuracion(Integer duracionMin, Integer duracionMax) {
        log.debug("Obteniendo evaluaciones con duración entre {} y {} minutos", duracionMin, duracionMax);
        return evaluacionRepository.findByDuracionMinutosBetween(duracionMin, duracionMax);
    }
    
    /**
     * Obtener evaluaciones por rango de puntaje
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorRangoPuntaje(Double puntajeMin, Double puntajeMax) {
        log.debug("Obteniendo evaluaciones con puntaje entre {} y {}", puntajeMin, puntajeMax);
        return evaluacionRepository.findByPuntajeMaximoBetween(puntajeMin, puntajeMax);
    }
    
    /**
     * Obtener evaluaciones de una ejecución ordenadas por fecha
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorEjecucionOrdenadas(Long ejecucionId, boolean ascendente) {
        log.debug("Obteniendo evaluaciones de ejecución {} ordenadas por fecha: {}", ejecucionId, ascendente ? "ascendente" : "descendente");
        
        return ejecucionRepository.findById(ejecucionId)
                .map(ejecucion -> ascendente ? 
                     evaluacionRepository.findByEjecucionOrderByFechaHoraInicioAsc(ejecucion) : 
                     evaluacionRepository.findByEjecucionOrderByFechaHoraInicioDesc(ejecucion))
                .orElse(List.of());
    }
    
    /**
     * Obtener evaluaciones con preguntas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerConPreguntas() {
        log.debug("Obteniendo evaluaciones que tienen preguntas");
        return evaluacionRepository.findEvaluacionesConPreguntas();
    }
    
    /**
     * Obtener evaluaciones sin preguntas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerSinPreguntas() {
        log.debug("Obteniendo evaluaciones sin preguntas");
        return evaluacionRepository.findEvaluacionesSinPreguntas();
    }
    
    /**
     * Contar evaluaciones por ejecución
     */
    @Transactional(readOnly = true)
    public Integer contarPorEjecucion(Long ejecucionId) {
        return evaluacionRepository.countByEjecucionId(ejecucionId);
    }
    
    /**
     * Contar preguntas en una evaluación
     */
    @Transactional(readOnly = true)
    public Integer contarPreguntas(Long evaluacionId) {
        return evaluacionRepository.countPreguntasEnEvaluacion(evaluacionId);
    }
    
    /**
     * Obtener próximas evaluaciones de una ejecución
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerProximasEvaluaciones(Long ejecucionId) {
        log.debug("Obteniendo próximas evaluaciones de la ejecución: {}", ejecucionId);
        return evaluacionRepository.findProximasEvaluacionesDeEjecucion(ejecucionId, LocalDateTime.now());
    }
    
    /**
     * Verificar si una evaluación está activa
     */
    @Transactional(readOnly = true)
    public boolean estaActiva(Long evaluacionId) {
        return evaluacionRepository.findById(evaluacionId)
                .map(evaluacion -> {
                    LocalDateTime ahora = LocalDateTime.now();
                    LocalDateTime fin = evaluacion.getFechaHoraInicio().plusMinutes(evaluacion.getDuracionMinutos());
                    return !ahora.isBefore(evaluacion.getFechaHoraInicio()) && ahora.isBefore(fin);
                })
                .orElse(false);
    }
    
    /**
     * Obtener fecha y hora de fin de una evaluación
     */
    @Transactional(readOnly = true)
    public Optional<LocalDateTime> obtenerFechaHoraFin(Long evaluacionId) {
        return evaluacionRepository.findById(evaluacionId)
                .map(evaluacion -> evaluacion.getFechaHoraInicio().plusMinutes(evaluacion.getDuracionMinutos()));
    }
    
    // Métodos de validación privados
    
    private void validarEjecucion(Long ejecucionId) {
        if (!ejecucionRepository.existsById(ejecucionId)) {
            throw new IllegalArgumentException("Ejecución no encontrada con ID: " + ejecucionId);
        }
    }
    
    private void validarDatos(Evaluacion evaluacion) {
        if (evaluacion.getTitulo() == null || evaluacion.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la evaluación es obligatorio");
        }
        
        if (evaluacion.getDuracionMinutos() == null || evaluacion.getDuracionMinutos() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }
        
        if (evaluacion.getDuracionMinutos() > 480) { // 8 horas máximo
            throw new IllegalArgumentException("La duración no puede exceder 480 minutos (8 horas)");
        }
        
        if (evaluacion.getPuntajeMaximo() == null || evaluacion.getPuntajeMaximo() <= 0) {
            throw new IllegalArgumentException("El puntaje máximo debe ser mayor a 0");
        }
    }
    
    private void validarFechaHora(LocalDateTime fechaHora, Ejecucion ejecucion) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora de inicio son obligatorias");
        }
        
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora de inicio no pueden ser anteriores al momento actual");
        }
        
        // Verificar que esté dentro del período de la ejecución
        if (fechaHora.toLocalDate().isBefore(ejecucion.getFechaInicio()) || 
            fechaHora.toLocalDate().isAfter(ejecucion.getFechaFin())) {
            throw new IllegalArgumentException("La evaluación debe realizarse durante el período de la ejecución");
        }
    }
}
