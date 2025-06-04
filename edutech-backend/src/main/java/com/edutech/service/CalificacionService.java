package com.edutech.service;

import com.edutech.model.Calificacion;
import com.edutech.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    
    /**
     * Obtener todas las calificaciones
     */
    public List<Calificacion> obtenerTodas() {
        return calificacionRepository.findAll();
    }
    
    /**
     * Obtener calificación por ID
     */
    public Optional<Calificacion> obtenerPorId(Long id) {
        return calificacionRepository.findById(id);
    }
    
    /**
     * Registrar nueva calificación
     */
    public Calificacion registrar(Calificacion calificacion) {
        log.debug("Registrando nueva calificación");
        validarCalificacion(calificacion);
        
        // Calcular nota chilena automáticamente
        calificacion.calcularNotaChilena();
        
        return calificacionRepository.save(calificacion);
    }

    /**
     * Actualizar calificación existente
     */
    public Calificacion actualizar(Long id, Calificacion calificacionActualizada) {
        log.debug("Actualizando calificación con ID: {}", id);
        
        return calificacionRepository.findById(id)
                .map(calificacionExistente -> {
                    calificacionExistente.setEvaluacion(calificacionActualizada.getEvaluacion());
                    calificacionExistente.setEstudiante(calificacionActualizada.getEstudiante());
                    calificacionExistente.setIntento(calificacionActualizada.getIntento());
                    calificacionExistente.setPuntajeObtenido(calificacionActualizada.getPuntajeObtenido());
                    calificacionExistente.setPuntajeMaximo(calificacionActualizada.getPuntajeMaximo());
                    calificacionExistente.setFechaEntrega(calificacionActualizada.getFechaEntrega());
                    calificacionExistente.setTiempoUtilizadoMinutos(calificacionActualizada.getTiempoUtilizadoMinutos());
                    calificacionExistente.setEstado(calificacionActualizada.getEstado());
                    calificacionExistente.setObservaciones(calificacionActualizada.getObservaciones());
                    
                    // Recalcular nota chilena
                    calificacionExistente.calcularNotaChilena();
                    
                    return calificacionRepository.save(calificacionExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar calificación
     */
    public void eliminar(Long id) {
        log.debug("Eliminando calificación con ID: {}", id);
        if (!calificacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Calificación no encontrada con ID: " + id);
        }
        calificacionRepository.deleteById(id);
    }
    
    /**
     * Obtener calificaciones por estudiante
     */
    public List<Calificacion> obtenerPorEstudiante(Long estudianteId) {
        return calificacionRepository.findByEstudianteId(estudianteId);
    }
    
    /**
     * Obtener calificaciones por evaluación
     */
    public List<Calificacion> obtenerPorEvaluacion(Long evaluacionId) {
        return calificacionRepository.findByEvaluacionId(evaluacionId);
    }
    
    /**
     * Obtener calificación específica de un estudiante en una evaluación
     */
    public Optional<Calificacion> obtenerCalificacionEspecifica(Long estudianteId, Long evaluacionId) {
        return calificacionRepository.findByEstudianteIdAndEvaluacionId(estudianteId, evaluacionId);
    }
    
    /**
     * Obtener calificaciones por rango de puntaje
     */
    public List<Calificacion> obtenerPorRangoPuntaje(Double puntajeMin, Double puntajeMax) {
        return calificacionRepository.findByPuntajeObtenidoBetween(puntajeMin, puntajeMax);
    }
    
    /**
     * Obtener calificaciones aprobadas
     */
    public List<Calificacion> obtenerAprobadas() {
        return calificacionRepository.findCalificacionesAprobadas();
    }
    
    /**
     * Obtener calificaciones reprobadas
     */
    public List<Calificacion> obtenerReprobadas() {
        return calificacionRepository.findCalificacionesReprobadas();
    }
    
    /**
     * Calcular promedio por evaluación
     */
    public Double calcularPromedioEvaluacion(Long evaluacionId) {
        return calificacionRepository.calcularPromedioByEvaluacion(evaluacionId);
    }
    
    /**
     * Calcular promedio por estudiante
     */
    public Double calcularPromedioEstudiante(Long estudianteId) {
        return calificacionRepository.calcularPromedioByEstudiante(estudianteId);
    }
    
    /**
     * Verificar si estudiante ya tiene calificación en evaluación
     */
    public boolean existeCalificacion(Long estudianteId, Long evaluacionId) {
        return calificacionRepository.existsByEstudianteIdAndEvaluacionId(estudianteId, evaluacionId);
    }
    
    /**
     * Calcular porcentaje obtenido de una calificación
     */
    public Double calcularPorcentaje(Long calificacionId) {
        return calificacionRepository.findById(calificacionId)
                .map(Calificacion::getPorcentajeObtenido)
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + calificacionId));
    }
    
    /**
     * Verificar si una calificación está aprobada
     */
    public boolean estaAprobada(Long calificacionId) {
        return calificacionRepository.findById(calificacionId)
                .map(Calificacion::isAprobada)
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + calificacionId));
    }
    
    // Métodos de validación privados
    
    private void validarCalificacion(Calificacion calificacion) {
        if (calificacion.getEstudiante() == null) {
            throw new IllegalArgumentException("El estudiante es obligatorio");
        }
        
        if (calificacion.getEvaluacion() == null) {
            throw new IllegalArgumentException("La evaluación es obligatoria");
        }
        
        if (calificacion.getPuntajeObtenido() == null || calificacion.getPuntajeObtenido() < 0) {
            throw new IllegalArgumentException("El puntaje obtenido no puede ser negativo");
        }
        
        // Validar que el puntaje no exceda el máximo
        if (calificacion.getPuntajeMaximo() != null && 
            calificacion.getPuntajeObtenido() > calificacion.getPuntajeMaximo()) {
            throw new IllegalArgumentException("El puntaje obtenido no puede exceder el puntaje máximo");
        }
        
        // Si se proporciona nota manual, validar rango chileno
        if (calificacion.getNota() != null && 
            (calificacion.getNota() < 1.0 || calificacion.getNota() > 7.0)) {
            throw new IllegalArgumentException("La nota debe estar entre 1.0 y 7.0 (sistema chileno)");
        }
    }
}
