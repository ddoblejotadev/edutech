package com.edutech.service;

import com.edutech.model.Evaluacion;
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
        
        // Validaciones
        validarEjecucion(evaluacion.getEjecucion().getId());
        validarFechas(evaluacion.getFechaDisponible(), evaluacion.getFechaLimite());
        validarPuntaje(evaluacion.getPuntajeTotal());
        validarNotasChilenas(evaluacion);
        
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
                    validarFechas(evaluacionActualizada.getFechaDisponible(), evaluacionActualizada.getFechaLimite());
                    
                    // Verificar que no haya comenzado si se cambia la fecha
                    if (!evaluacionExistente.getFechaDisponible().equals(evaluacionActualizada.getFechaDisponible())) {
                        if (evaluacionExistente.getFechaDisponible().isBefore(LocalDateTime.now())) {
                            throw new IllegalStateException("No se puede modificar la fecha de una evaluación que ya comenzó");
                        }
                    }
                    
                    // Actualizar campos
                    evaluacionExistente.setTitulo(evaluacionActualizada.getTitulo());
                    evaluacionExistente.setDescripcion(evaluacionActualizada.getDescripcion());
                    evaluacionExistente.setTipo(evaluacionActualizada.getTipo());
                    evaluacionExistente.setFechaDisponible(evaluacionActualizada.getFechaDisponible());
                    evaluacionExistente.setFechaLimite(evaluacionActualizada.getFechaLimite());
                    evaluacionExistente.setDuracionMinutos(evaluacionActualizada.getDuracionMinutos());
                    evaluacionExistente.setPuntajeTotal(evaluacionActualizada.getPuntajeTotal());
                    evaluacionExistente.setPonderacion(evaluacionActualizada.getPonderacion());
                    evaluacionExistente.setIntentosPermitidos(evaluacionActualizada.getIntentosPermitidos());
                    evaluacionExistente.setNotaMinimaAprobacion(evaluacionActualizada.getNotaMinimaAprobacion());
                    evaluacionExistente.setNotaMaxima(evaluacionActualizada.getNotaMaxima());
                    evaluacionExistente.setExigenciaPorcentual(evaluacionActualizada.getExigenciaPorcentual());
                    evaluacionExistente.setActivo(evaluacionActualizada.getActivo());
                    evaluacionExistente.setPublicada(evaluacionActualizada.getPublicada());
                    
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
        if (evaluacion.getFechaDisponible().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede eliminar una evaluación que ya comenzó");
        }
        
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
     * Buscar evaluaciones por título
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> buscarPorTitulo(String titulo) {
        log.debug("Buscando evaluaciones por título: {}", titulo);
        return evaluacionRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    /**
     * Buscar evaluaciones por tipo
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorTipo(String tipo) {
        log.debug("Obteniendo evaluaciones por tipo: {}", tipo);
        return evaluacionRepository.findByTipoIgnoreCase(tipo);
    }
    
    /**
     * Obtener evaluaciones activas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerActivas() {
        log.debug("Obteniendo evaluaciones activas");
        return evaluacionRepository.findByActivoTrue();
    }
    
    /**
     * Obtener evaluaciones publicadas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPublicadas() {
        log.debug("Obteniendo evaluaciones publicadas");
        return evaluacionRepository.findByPublicadaTrue();
    }
    
    /**
     * Obtener evaluaciones disponibles
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerDisponibles() {
        log.debug("Obteniendo evaluaciones disponibles");
        LocalDateTime ahora = LocalDateTime.now();
        return evaluacionRepository.findByActivoTrueAndPublicadaTrueAndFechaInicioBeforeAndFechaFinAfter(ahora);
    }
    
    /**
     * Obtener evaluaciones futuras
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerFuturas() {
        log.debug("Obteniendo evaluaciones futuras");
        return evaluacionRepository.findByFechaInicioAfter(LocalDateTime.now());
    }
    
    /**
     * Obtener evaluaciones vencidas
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerVencidas() {
        log.debug("Obteniendo evaluaciones vencidas");
        return evaluacionRepository.findByFechaFinBefore(LocalDateTime.now());
    }
    
    /**
     * Obtener evaluaciones por rango de fechas de inicio
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorRangoFechasInicio(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Obteniendo evaluaciones con fecha de inicio entre {} y {}", fechaInicio, fechaFin);
        return evaluacionRepository.findByFechaInicioBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener evaluaciones por rango de puntaje
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerPorRangoPuntaje(Double puntajeMin, Double puntajeMax) {
        log.debug("Obteniendo evaluaciones con puntaje entre {} y {}", puntajeMin, puntajeMax);
        return evaluacionRepository.findByPuntajeTotalBetween(puntajeMin, puntajeMax);
    }
    
    /**
     * Verificar si una evaluación está disponible
     */
    @Transactional(readOnly = true)
    public boolean estaDisponible(Long evaluacionId) {
        return evaluacionRepository.findById(evaluacionId)
                .map(Evaluacion::isDisponible)
                .orElse(false);
    }
    
    /**
     * Verificar si una evaluación está vencida
     */
    @Transactional(readOnly = true)
    public boolean estaVencida(Long evaluacionId) {
        return evaluacionRepository.findById(evaluacionId)
                .map(Evaluacion::isVencida)
                .orElse(true);
    }
    
    /**
     * Obtener tiempo restante para una evaluación
     */
    @Transactional(readOnly = true)
    public Optional<Long> obtenerTiempoRestante(Long evaluacionId) {
        return evaluacionRepository.findById(evaluacionId)
                .map(evaluacion -> {
                    LocalDateTime ahora = LocalDateTime.now();
                    if (evaluacion.getFechaLimite().isAfter(ahora)) {
                        return java.time.Duration.between(ahora, evaluacion.getFechaLimite()).toMinutes();
                    }
                    return 0L;
                });
    }
    
    /**
     * Contar evaluaciones por ejecución
     */
    @Transactional(readOnly = true)
    public Integer contarPorEjecucion(Long ejecucionId) {
        return evaluacionRepository.countByEjecucionId(ejecucionId);
    }
    
    /**
     * Obtener evaluaciones próximas a vencer
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerProximasAVencer(int horas) {
        log.debug("Obteniendo evaluaciones que vencen en las próximas {} horas", horas);
        LocalDateTime limite = LocalDateTime.now().plusHours(horas);
        return evaluacionRepository.findByFechaFinBetween(LocalDateTime.now(), limite);
    }
    
    /**
     * Activar evaluación
     */
    public void activar(Long id) {
        log.debug("Activando evaluación con ID: {}", id);
        evaluacionRepository.findById(id).ifPresent(evaluacion -> {
            evaluacion.setActivo(true);
            evaluacionRepository.save(evaluacion);
        });
    }
    
    /**
     * Desactivar evaluación
     */
    public void desactivar(Long id) {
        log.debug("Desactivando evaluación con ID: {}", id);
        evaluacionRepository.findById(id).ifPresent(evaluacion -> {
            evaluacion.setActivo(false);
            evaluacionRepository.save(evaluacion);
        });
    }
    
    /**
     * Publicar evaluación
     */
    public void publicar(Long id) {
        log.debug("Publicando evaluación con ID: {}", id);
        evaluacionRepository.findById(id).ifPresent(evaluacion -> {
            evaluacion.setPublicada(true);
            evaluacionRepository.save(evaluacion);
        });
    }
    
    /**
     * Despublicar evaluación
     */
    public void despublicar(Long id) {
        log.debug("Despublicando evaluación con ID: {}", id);
        evaluacionRepository.findById(id).ifPresent(evaluacion -> {
            evaluacion.setPublicada(false);
            evaluacionRepository.save(evaluacion);
        });
    }
    
    // Métodos de validación privados
    
    private void validarEjecucion(Long ejecucionId) {
        if (!ejecucionRepository.existsById(ejecucionId)) {
            throw new IllegalArgumentException("Ejecución no encontrada con ID: " + ejecucionId);
        }
    }
    
    private void validarFechas(LocalDateTime fechaDisponible, LocalDateTime fechaLimite) {
        LocalDateTime ahora = LocalDateTime.now();
        
        if (fechaDisponible == null || fechaLimite == null) {
            throw new IllegalArgumentException("Las fechas de disponibilidad y límite son obligatorias");
        }
        
        if (fechaDisponible.isBefore(ahora)) {
            throw new IllegalArgumentException("La fecha de disponibilidad no puede ser anterior al momento actual");
        }
        
        if (fechaLimite.isBefore(fechaDisponible)) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior a la fecha de disponibilidad");
        }
    }
    
    private void validarPuntaje(Double puntajeTotal) {
        if (puntajeTotal == null || puntajeTotal <= 0) {
            throw new IllegalArgumentException("El puntaje total debe ser mayor a 0");
        }
        
        if (puntajeTotal > 100.0) {
            throw new IllegalArgumentException("El puntaje total no puede exceder 100.0");
        }
    }
    
    private void validarNotasChilenas(Evaluacion evaluacion) {
        if (evaluacion.getNotaMinimaAprobacion() != null) {
            if (evaluacion.getNotaMinimaAprobacion() < 1.0 || evaluacion.getNotaMinimaAprobacion() > 7.0) {
                throw new IllegalArgumentException("La nota mínima debe estar entre 1.0 y 7.0");
            }
        }
        
        if (evaluacion.getNotaMaxima() != null) {
            if (evaluacion.getNotaMaxima() < 1.0 || evaluacion.getNotaMaxima() > 7.0) {
                throw new IllegalArgumentException("La nota máxima debe estar entre 1.0 y 7.0");
            }
            
            if (evaluacion.getNotaMinimaAprobacion() != null && 
                evaluacion.getNotaMaxima() < evaluacion.getNotaMinimaAprobacion()) {
                throw new IllegalArgumentException("La nota máxima no puede ser menor a la nota mínima");
            }
        }
        
        if (evaluacion.getExigenciaPorcentual() != null) {
            if (evaluacion.getExigenciaPorcentual() < 0 || evaluacion.getExigenciaPorcentual() > 100) {
                throw new IllegalArgumentException("La exigencia porcentual debe estar entre 0% y 100%");
            }
        }
    }
}
