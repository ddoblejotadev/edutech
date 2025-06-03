package com.edutech.service;

import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.repository.EjecucionRepository;
import com.edutech.repository.CursoRepository;
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
public class EjecucionService {
    
    private final EjecucionRepository ejecucionRepository;
    private final CursoRepository cursoRepository;
    
    /**
     * Obtener todas las ejecuciones
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerTodas() {
        log.debug("Obteniendo todas las ejecuciones");
        return ejecucionRepository.findAll();
    }
    
    /**
     * Obtener ejecución por ID
     */
    @Transactional(readOnly = true)
    public Optional<Ejecucion> obtenerPorId(Long id) {
        log.debug("Obteniendo ejecución con ID: {}", id);
        return ejecucionRepository.findById(id);
    }
    
    /**
     * Crear nueva ejecución
     */
    public Ejecucion crear(Ejecucion ejecucion) {
        log.debug("Creando nueva ejecución para curso: {}", ejecucion.getCurso().getId());
        
        // Validaciones de negocio
        validarFechas(ejecucion.getFechaInicio(), ejecucion.getFechaFin());
        validarCurso(ejecucion.getCurso().getId());
        validarCupoMaximo(ejecucion.getCupoMaximo());
        
        return ejecucionRepository.save(ejecucion);
    }
    
    /**
     * Actualizar ejecución existente
     */
    public Ejecucion actualizar(Long id, Ejecucion ejecucionActualizada) {
        log.debug("Actualizando ejecución con ID: {}", id);
        
        return ejecucionRepository.findById(id)
                .map(ejecucionExistente -> {
                    // Validaciones
                    validarFechas(ejecucionActualizada.getFechaInicio(), ejecucionActualizada.getFechaFin());
                    validarCurso(ejecucionActualizada.getCurso().getId());
                    validarCupoMaximo(ejecucionActualizada.getCupoMaximo());
                    
                    // Verificar que el nuevo cupo no sea menor a las inscripciones actuales
                    Integer inscripcionesActuales = ejecucionRepository.countEstudiantesInscritos(id);
                    if (ejecucionActualizada.getCupoMaximo() < inscripcionesActuales) {
                        throw new IllegalArgumentException("El cupo máximo no puede ser menor a las inscripciones actuales: " + inscripcionesActuales);
                    }
                    
                    // Actualizar campos
                    ejecucionExistente.setCurso(ejecucionActualizada.getCurso());
                    ejecucionExistente.setFechaInicio(ejecucionActualizada.getFechaInicio());
                    ejecucionExistente.setFechaFin(ejecucionActualizada.getFechaFin());
                    ejecucionExistente.setCupoMaximo(ejecucionActualizada.getCupoMaximo());
                    
                    return ejecucionRepository.save(ejecucionExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Ejecución no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar ejecución
     */
    public void eliminar(Long id) {
        log.debug("Eliminando ejecución con ID: {}", id);
        
        Ejecucion ejecucion = ejecucionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ejecución no encontrada con ID: " + id));
        
        // Verificar que no tenga inscripciones
        Integer inscripciones = ejecucionRepository.countEstudiantesInscritos(id);
        if (inscripciones > 0) {
            throw new IllegalStateException("No se puede eliminar la ejecución porque tiene " + inscripciones + " inscripciones");
        }
        
        ejecucionRepository.delete(ejecucion);
    }
    
    /**
     * Obtener ejecuciones por curso
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerPorCurso(Long cursoId) {
        log.debug("Obteniendo ejecuciones del curso: {}", cursoId);
        return ejecucionRepository.findByCursoId(cursoId);
    }
    
    /**
     * Obtener ejecuciones activas
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerActivas() {
        log.debug("Obteniendo ejecuciones activas");
        return ejecucionRepository.findEjecucionesActivas();
    }
    
    /**
     * Obtener ejecuciones futuras
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerFuturas() {
        log.debug("Obteniendo ejecuciones futuras");
        return ejecucionRepository.findEjecucionesFuturas();
    }
    
    /**
     * Obtener ejecuciones pasadas
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerPasadas() {
        log.debug("Obteniendo ejecuciones pasadas");
        return ejecucionRepository.findEjecucionesPasadas();
    }
    
    /**
     * Obtener ejecuciones con cupos disponibles
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerConCuposDisponibles() {
        log.debug("Obteniendo ejecuciones con cupos disponibles");
        return ejecucionRepository.findEjecucionesConCuposDisponibles();
    }
    
    /**
     * Obtener ejecuciones con cupos disponibles para un curso específico
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerConCuposDisponiblesPorCurso(Long cursoId) {
        log.debug("Obteniendo ejecuciones con cupos disponibles para curso: {}", cursoId);
        return ejecucionRepository.findEjecucionesConCuposDisponiblesByCurso(cursoId);
    }
    
    /**
     * Obtener ejecuciones por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        log.debug("Obteniendo ejecuciones entre {} y {}", fechaInicio, fechaFin);
        return ejecucionRepository.findByFechaInicioBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener ejecuciones por rango de cupo
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerPorRangoCupo(Integer cupoMin, Integer cupoMax) {
        log.debug("Obteniendo ejecuciones con cupo entre {} y {}", cupoMin, cupoMax);
        return ejecucionRepository.findByCupoMaximoBetween(cupoMin, cupoMax);
    }
    
    /**
     * Obtener ejecuciones ordenadas por fecha
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerOrdenadasPorFecha(boolean ascendente) {
        log.debug("Obteniendo ejecuciones ordenadas por fecha: {}", ascendente ? "ascendente" : "descendente");
        return ascendente ? ejecucionRepository.findAllByOrderByFechaInicioAsc() : ejecucionRepository.findAllByOrderByFechaInicioDesc();
    }
    
    /**
     * Obtener ejecuciones de un curso ordenadas por fecha
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> obtenerPorCursoOrdenadasPorFecha(Long cursoId, boolean ascendente) {
        log.debug("Obteniendo ejecuciones del curso {} ordenadas por fecha: {}", cursoId, ascendente ? "ascendente" : "descendente");
        
        return cursoRepository.findById(cursoId)
                .map(curso -> ascendente ? 
                     ejecucionRepository.findByCursoOrderByFechaInicioAsc(curso) : 
                     ejecucionRepository.findByCursoOrderByFechaInicioDesc(curso))
                .orElse(List.of());
    }
    
    /**
     * Contar estudiantes inscritos en una ejecución
     */
    @Transactional(readOnly = true)
    public Integer contarEstudiantesInscritos(Long ejecucionId) {
        return ejecucionRepository.countEstudiantesInscritos(ejecucionId);
    }
    
    /**
     * Verificar si hay cupos disponibles
     */
    @Transactional(readOnly = true)
    public boolean tieneCuposDisponibles(Long ejecucionId) {
        return ejecucionRepository.findById(ejecucionId)
                .map(ejecucion -> {
                    Integer inscritos = ejecucionRepository.countEstudiantesInscritos(ejecucionId);
                    return inscritos < ejecucion.getCupoMaximo();
                })
                .orElse(false);
    }
    
    /**
     * Obtener número de cupos disponibles
     */
    @Transactional(readOnly = true)
    public Integer obtenerCuposDisponibles(Long ejecucionId) {
        return ejecucionRepository.findById(ejecucionId)
                .map(ejecucion -> {
                    Integer inscritos = ejecucionRepository.countEstudiantesInscritos(ejecucionId);
                    return ejecucion.getCupoMaximo() - inscritos;
                })
                .orElse(0);
    }
    
    /**
     * Verificar si existe ejecución activa para un curso
     */
    @Transactional(readOnly = true)
    public boolean existeEjecucionActivaParaCurso(Long cursoId) {
        return ejecucionRepository.existeEjecucionActivaParaCurso(cursoId);
    }
    
    /**
     * Verificar si una ejecución está activa
     */
    @Transactional(readOnly = true)
    public boolean estaActiva(Long ejecucionId) {
        return ejecucionRepository.findById(ejecucionId)
                .map(ejecucion -> {
                    LocalDate hoy = LocalDate.now();
                    return !hoy.isBefore(ejecucion.getFechaInicio()) && !hoy.isAfter(ejecucion.getFechaFin());
                })
                .orElse(false);
    }
    
    // Métodos de validación privados
    
    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual");
        }
    }
    
    private void validarCurso(Long cursoId) {
        if (!cursoRepository.existsById(cursoId)) {
            throw new IllegalArgumentException("Curso no encontrado con ID: " + cursoId);
        }
    }
    
    private void validarCupoMaximo(Integer cupoMaximo) {
        if (cupoMaximo == null || cupoMaximo <= 0) {
            throw new IllegalArgumentException("El cupo máximo debe ser mayor a 0");
        }
        
        if (cupoMaximo > 100) {
            throw new IllegalArgumentException("El cupo máximo no puede exceder 100 estudiantes");
        }
    }
}
