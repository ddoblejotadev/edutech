package com.edutech.service;

import com.edutech.model.Calificacion;
import com.edutech.model.Evaluacion;
import com.edutech.model.Persona;
import com.edutech.repository.CalificacionRepository;
import com.edutech.repository.EvaluacionRepository;
import com.edutech.repository.PersonaRepository;
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
public class CalificacionService {
    
    private final CalificacionRepository calificacionRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final PersonaRepository personaRepository;
    
    /**
     * Obtener todas las calificaciones
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerTodas() {
        log.debug("Obteniendo todas las calificaciones");
        return calificacionRepository.findAll();
    }
    
    /**
     * Obtener calificación por ID
     */
    @Transactional(readOnly = true)
    public Optional<Calificacion> obtenerPorId(Long id) {
        log.debug("Obteniendo calificación con ID: {}", id);
        return calificacionRepository.findById(id);
    }
    
    /**
     * Registrar nueva calificación
     */
    public Calificacion registrar(Calificacion calificacion) {
        log.debug("Registrando calificación para estudiante {} en evaluación {}", 
                  calificacion.getEstudiante().getId(), calificacion.getEvaluacion().getId());
        
        // Validaciones de negocio
        validarEstudianteYEvaluacion(calificacion.getEstudiante().getId(), calificacion.getEvaluacion().getId());
        validarDatos(calificacion);
        
        // Verificar que no exista ya una calificación
        if (calificacionRepository.existsByEstudianteIdAndEvaluacionId(
                calificacion.getEstudiante().getId(), calificacion.getEvaluacion().getId())) {
            throw new IllegalArgumentException("Ya existe una calificación para este estudiante en esta evaluación");
        }
        
        // Establecer fecha de realización si no se especifica
        if (calificacion.getFechaRealizacion() == null) {
            calificacion.setFechaRealizacion(LocalDateTime.now());
        }
        
        return calificacionRepository.save(calificacion);
    }
    
    /**
     * Actualizar calificación existente
     */
    public Calificacion actualizar(Long id, Calificacion calificacionActualizada) {
        log.debug("Actualizando calificación con ID: {}", id);
        
        return calificacionRepository.findById(id)
                .map(calificacionExistente -> {
                    // Validaciones
                    validarEstudianteYEvaluacion(calificacionActualizada.getEstudiante().getId(), 
                                               calificacionActualizada.getEvaluacion().getId());
                    validarDatos(calificacionActualizada);
                    
                    // Actualizar campos
                    calificacionExistente.setPuntajeObtenido(calificacionActualizada.getPuntajeObtenido());
                    calificacionExistente.setFechaRealizacion(calificacionActualizada.getFechaRealizacion());
                    calificacionExistente.setEstudiante(calificacionActualizada.getEstudiante());
                    calificacionExistente.setEvaluacion(calificacionActualizada.getEvaluacion());
                    
                    return calificacionRepository.save(calificacionExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar calificación
     */
    public void eliminar(Long id) {
        log.debug("Eliminando calificación con ID: {}", id);
        
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + id));
        
        calificacionRepository.delete(calificacion);
    }
    
    /**
     * Obtener calificaciones por estudiante
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorEstudiante(Long estudianteId) {
        log.debug("Obteniendo calificaciones del estudiante: {}", estudianteId);
        return calificacionRepository.findByEstudianteId(estudianteId);
    }
    
    /**
     * Obtener calificaciones por evaluación
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorEvaluacion(Long evaluacionId) {
        log.debug("Obteniendo calificaciones de la evaluación: {}", evaluacionId);
        return calificacionRepository.findByEvaluacionId(evaluacionId);
    }
    
    /**
     * Obtener calificación específica de un estudiante en una evaluación
     */
    @Transactional(readOnly = true)
    public Optional<Calificacion> obtenerCalificacionEspecifica(Long estudianteId, Long evaluacionId) {
        log.debug("Obteniendo calificación del estudiante {} en evaluación {}", estudianteId, evaluacionId);
        return calificacionRepository.findByEstudianteIdAndEvaluacionId(estudianteId, evaluacionId);
    }
    
    /**
     * Obtener calificaciones por rango de puntaje
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorRangoPuntaje(Double puntajeMin, Double puntajeMax) {
        log.debug("Obteniendo calificaciones con puntaje entre {} y {}", puntajeMin, puntajeMax);
        return calificacionRepository.findByPuntajeObtenidoBetween(puntajeMin, puntajeMax);
    }
    
    /**
     * Obtener calificaciones por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Obteniendo calificaciones entre {} y {}", fechaInicio, fechaFin);
        return calificacionRepository.findByFechaRealizacionBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener calificaciones de un estudiante ordenadas por fecha
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorEstudianteOrdenadas(Long estudianteId) {
        log.debug("Obteniendo calificaciones del estudiante {} ordenadas por fecha", estudianteId);
        return calificacionRepository.findByEstudianteIdOrderByFechaRealizacionDesc(estudianteId);
    }
    
    /**
     * Obtener calificaciones de una evaluación ordenadas por puntaje
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorEvaluacionOrdenadas(Long evaluacionId, boolean ascendente) {
        log.debug("Obteniendo calificaciones de evaluación {} ordenadas por puntaje: {}", 
                  evaluacionId, ascendente ? "ascendente" : "descendente");
        
        return evaluacionRepository.findById(evaluacionId)
                .map(evaluacion -> ascendente ? 
                     calificacionRepository.findByEvaluacionOrderByPuntajeObtenidoAsc(evaluacion) : 
                     calificacionRepository.findByEvaluacionOrderByPuntajeObtenidoDesc(evaluacion))
                .orElse(List.of());
    }
    
    /**
     * Obtener calificaciones por curso
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorCurso(Long cursoId) {
        log.debug("Obteniendo calificaciones del curso: {}", cursoId);
        return calificacionRepository.findCalificacionesByCurso(cursoId);
    }
    
    /**
     * Obtener calificaciones por ejecución
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorEjecucion(Long ejecucionId) {
        log.debug("Obteniendo calificaciones de la ejecución: {}", ejecucionId);
        return calificacionRepository.findCalificacionesByEjecucion(ejecucionId);
    }
    
    /**
     * Obtener calificaciones aprobadas (≥60%)
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerAprobadas() {
        log.debug("Obteniendo calificaciones aprobadas");
        return calificacionRepository.findCalificacionesAprobadas();
    }
    
    /**
     * Obtener calificaciones reprobadas (<60%)
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerReprobadas() {
        log.debug("Obteniendo calificaciones reprobadas");
        return calificacionRepository.findCalificacionesReprobadas();
    }
    
    /**
     * Obtener calificaciones por porcentaje de acierto
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorPorcentajeRange(Double porcentajeMin, Double porcentajeMax) {
        log.debug("Obteniendo calificaciones con porcentaje entre {}% y {}%", porcentajeMin, porcentajeMax);
        return calificacionRepository.findCalificacionesByPorcentajeRange(porcentajeMin, porcentajeMax);
    }
    
    /**
     * Obtener mejores calificaciones
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerMejoresCalificaciones() {
        log.debug("Obteniendo mejores calificaciones");
        return calificacionRepository.findTopCalificaciones();
    }
    
    /**
     * Obtener calificaciones recientes
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerRecientes() {
        log.debug("Obteniendo calificaciones recientes");
        return calificacionRepository.findCalificacionesRecientes();
    }
    
    /**
     * Obtener calificaciones de un estudiante en un curso específico
     */
    @Transactional(readOnly = true)
    public List<Calificacion> obtenerDeEstudianteEnCurso(Long estudianteId, Long cursoId) {
        log.debug("Obteniendo calificaciones del estudiante {} en curso {}", estudianteId, cursoId);
        return calificacionRepository.findCalificacionesDeEstudianteEnCurso(estudianteId, cursoId);
    }
    
    // Métodos de estadísticas
    
    /**
     * Calcular promedio por evaluación
     */
    @Transactional(readOnly = true)
    public Double calcularPromedioEvaluacion(Long evaluacionId) {
        return calificacionRepository.calcularPromedioByEvaluacion(evaluacionId);
    }
    
    /**
     * Calcular promedio por estudiante
     */
    @Transactional(readOnly = true)
    public Double calcularPromedioEstudiante(Long estudianteId) {
        return calificacionRepository.calcularPromedioByEstudiante(estudianteId);
    }
    
    /**
     * Obtener puntaje máximo de una evaluación
     */
    @Transactional(readOnly = true)
    public Double obtenerPuntajeMaximoEvaluacion(Long evaluacionId) {
        return calificacionRepository.findMaxPuntajeByEvaluacion(evaluacionId);
    }
    
    /**
     * Obtener puntaje mínimo de una evaluación
     */
    @Transactional(readOnly = true)
    public Double obtenerPuntajeMinimoEvaluacion(Long evaluacionId) {
        return calificacionRepository.findMinPuntajeByEvaluacion(evaluacionId);
    }
    
    /**
     * Obtener puntaje máximo de un estudiante
     */
    @Transactional(readOnly = true)
    public Double obtenerPuntajeMaximoEstudiante(Long estudianteId) {
        return calificacionRepository.findMaxPuntajeByEstudiante(estudianteId);
    }
    
    /**
     * Obtener puntaje mínimo de un estudiante
     */
    @Transactional(readOnly = true)
    public Double obtenerPuntajeMinimoEstudiante(Long estudianteId) {
        return calificacionRepository.findMinPuntajeByEstudiante(estudianteId);
    }
    
    /**
     * Calcular porcentaje de aprobación por evaluación
     */
    @Transactional(readOnly = true)
    public Double calcularPorcentajeAprobacion(Long evaluacionId) {
        return calificacionRepository.calcularPorcentajeAprobacionByEvaluacion(evaluacionId);
    }
    
    /**
     * Contar calificaciones por evaluación
     */
    @Transactional(readOnly = true)
    public Integer contarPorEvaluacion(Long evaluacionId) {
        return calificacionRepository.countByEvaluacionId(evaluacionId);
    }
    
    /**
     * Contar calificaciones por estudiante
     */
    @Transactional(readOnly = true)
    public Integer contarPorEstudiante(Long estudianteId) {
        return calificacionRepository.countByEstudianteId(estudianteId);
    }
    
    /**
     * Verificar si existe calificación
     */
    @Transactional(readOnly = true)
    public boolean existeCalificacion(Long estudianteId, Long evaluacionId) {
        return calificacionRepository.existsByEstudianteIdAndEvaluacionId(estudianteId, evaluacionId);
    }
    
    /**
     * Calcular porcentaje obtenido
     */
    @Transactional(readOnly = true)
    public Double calcularPorcentaje(Long calificacionId) {
        return calificacionRepository.findById(calificacionId)
                .map(calificacion -> {
                    double porcentaje = (calificacion.getPuntajeObtenido() / calificacion.getEvaluacion().getPuntajeMaximo()) * 100;
                    return Math.round(porcentaje * 100.0) / 100.0; // Redondear a 2 decimales
                })
                .orElse(0.0);
    }
    
    /**
     * Determinar si una calificación está aprobada
     */
    @Transactional(readOnly = true)
    public boolean estaAprobada(Long calificacionId) {
        Double porcentaje = calcularPorcentaje(calificacionId);
        return porcentaje >= 60.0;
    }
    
    // Métodos de validación privados
    
    private void validarEstudianteYEvaluacion(Long estudianteId, Long evaluacionId) {
        if (!personaRepository.existsById(estudianteId)) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId);
        }
        
        if (!evaluacionRepository.existsById(evaluacionId)) {
            throw new IllegalArgumentException("Evaluación no encontrada con ID: " + evaluacionId);
        }
        
        // Verificar que la persona sea un estudiante
        Persona persona = personaRepository.findById(estudianteId).orElse(null);
        if (persona != null && !persona.getTipoPersona().getNombre().equalsIgnoreCase("Estudiante")) {
            throw new IllegalArgumentException("La persona especificada no es un estudiante");
        }
    }
    
    private void validarDatos(Calificacion calificacion) {
        if (calificacion.getPuntajeObtenido() == null) {
            throw new IllegalArgumentException("El puntaje obtenido es obligatorio");
        }
        
        if (calificacion.getPuntajeObtenido() < 0) {
            throw new IllegalArgumentException("El puntaje obtenido no puede ser negativo");
        }
        
        // Verificar que no exceda el puntaje máximo de la evaluación
        Evaluacion evaluacion = evaluacionRepository.findById(calificacion.getEvaluacion().getId()).orElse(null);
        if (evaluacion != null && calificacion.getPuntajeObtenido() > evaluacion.getPuntajeMaximo()) {
            throw new IllegalArgumentException("El puntaje obtenido no puede exceder el puntaje máximo de la evaluación: " + evaluacion.getPuntajeMaximo());
        }
    }
}
