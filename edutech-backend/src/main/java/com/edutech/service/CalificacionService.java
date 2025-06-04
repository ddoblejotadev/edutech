package com.edutech.service;

//Importaciones Modelo y Repository
import com.edutech.model.Calificacion;
import com.edutech.repository.CalificacionRepository;

//Importaciones para funcionamiento de service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Importaciones Java
import java.util.List;
import java.util.Optional;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;
    
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
        validarCalificacion(calificacion);
        return calificacionRepository.save(calificacion);
    }
    
    /**
     * Actualizar calificación existente
     */
    public Calificacion actualizar(Long id, Calificacion calificacionActualizada) {
        
        return calificacionRepository.findById(id)
                .map(calificacionExistente -> {
                    calificacionExistente.setEvaluacion(calificacionActualizada.getEvaluacion());
                    calificacionExistente.setEstudiante(calificacionActualizada.getEstudiante());
                    calificacionExistente.setIntento(calificacionActualizada.getIntento());
                    calificacionExistente.setPuntajeObtenido(calificacionActualizada.getPuntajeObtenido());
                    calificacionExistente.setNota(calificacionActualizada.getNota());
                    calificacionExistente.setFechaEntrega(calificacionActualizada.getFechaEntrega());
                    calificacionExistente.setTiempoUtilizadoMinutos(calificacionActualizada.getTiempoUtilizadoMinutos());
                    calificacionExistente.setEstado(calificacionActualizada.getEstado());
                    calificacionExistente.setObservaciones(calificacionActualizada.getObservaciones());
                    return calificacionRepository.save(calificacionExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar calificación
     */
    public void eliminar(Long id) {
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
        
        if (calificacion.getNota() == null || calificacion.getNota() < 1.0 || calificacion.getNota() > 7.0) {
            throw new IllegalArgumentException("La nota debe estar entre 1.0 y 7.0");
        }
    }
}
