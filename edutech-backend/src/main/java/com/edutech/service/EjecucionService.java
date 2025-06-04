package com.edutech.service;

//Importaciones del model y repository
import com.edutech.model.Ejecucion;
import com.edutech.repository.EjecucionRepository;
import com.edutech.repository.CursoRepository;

//Importacion para dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Importaciones Java
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EjecucionService {
    
    @Autowired
    private EjecucionRepository ejecucionRepository;
    @Autowired
    private CursoRepository cursoRepository;
    
    /**
     * Obtener todas las ejecuciones
     */
    public List<Ejecucion> obtenerTodas() {
        return ejecucionRepository.findAll();
    }
    
    /**
     * Obtener ejecución por ID
     */
    public Optional<Ejecucion> obtenerPorId(Long id) {
        return ejecucionRepository.findById(id);
    }
    
    /**
     * Crear nueva ejecución
     */
    public Ejecucion crear(Ejecucion ejecucion) {
        
        // Validaciones
        validarCurso(ejecucion.getCurso().getId());
        validarFechas(ejecucion.getFechaInicio(), ejecucion.getFechaFin());
        validarCupoMaximo(ejecucion.getCuposDisponibles());
        
        // Verificar que no exista otra ejecución del mismo curso con la misma sección en el mismo período
        if (ejecucionRepository.existsByCursoIdAndSeccionAndPeriodo(
                ejecucion.getCurso().getId(), ejecucion.getSeccion(), ejecucion.getPeriodo())) {
            throw new IllegalArgumentException("Ya existe una ejecución del curso con la misma sección en este período");
        }
        
        return ejecucionRepository.save(ejecucion);
    }

    /**
     * Actualizar ejecución existente
     */
    public Ejecucion actualizar(Long id, Ejecucion ejecucionActualizada) {
        
        return ejecucionRepository.findById(id)
                .map(ejecucionExistente -> {
                    // Validaciones
                    validarCurso(ejecucionActualizada.getCurso().getId());
                    validarFechas(ejecucionActualizada.getFechaInicio(), ejecucionActualizada.getFechaFin());
                    validarCupoMaximo(ejecucionActualizada.getCuposDisponibles());
                    
                    // Verificar conflictos si cambió curso, sección o período
                    if (!ejecucionExistente.getCurso().getId().equals(ejecucionActualizada.getCurso().getId()) ||
                        !ejecucionExistente.getSeccion().equals(ejecucionActualizada.getSeccion()) ||
                        !ejecucionExistente.getPeriodo().equals(ejecucionActualizada.getPeriodo())) {
                        
                        if (ejecucionRepository.existsByCursoIdAndSeccionAndPeriodo(
                                ejecucionActualizada.getCurso().getId(), 
                                ejecucionActualizada.getSeccion(), 
                                ejecucionActualizada.getPeriodo())) {
                            throw new IllegalArgumentException("Ya existe una ejecución del curso con esa sección en ese período");
                        }
                    }
                    
                    // Actualizar campos
                    ejecucionExistente.setCurso(ejecucionActualizada.getCurso());
                    ejecucionExistente.setPeriodo(ejecucionActualizada.getPeriodo());
                    ejecucionExistente.setSeccion(ejecucionActualizada.getSeccion());
                    ejecucionExistente.setFechaInicio(ejecucionActualizada.getFechaInicio());
                    ejecucionExistente.setFechaFin(ejecucionActualizada.getFechaFin());
                    ejecucionExistente.setProfesor(ejecucionActualizada.getProfesor());
                    ejecucionExistente.setSala(ejecucionActualizada.getSala());
                    ejecucionExistente.setHorario(ejecucionActualizada.getHorario());
                    ejecucionExistente.setCuposDisponibles(ejecucionActualizada.getCuposDisponibles());
                    ejecucionExistente.setEstado(ejecucionActualizada.getEstado());
                    
                    return ejecucionRepository.save(ejecucionExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Ejecución no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar ejecución
     */
    public void eliminar(Long id) {
        
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
    public List<Ejecucion> obtenerPorCurso(Long cursoId) {
        return ejecucionRepository.findByCursoId(cursoId);
    }
    
    /**
     * Obtener ejecuciones activas
     */
    public List<Ejecucion> obtenerActivas() {
        return ejecucionRepository.findEjecucionesActivas();
    }
    
    /**
     * Obtener ejecuciones futuras
     */
    public List<Ejecucion> obtenerFuturas() {
        return ejecucionRepository.findEjecucionesFuturas();
    }
    
    /**
     * Obtener ejecuciones pasadas
     */
    public List<Ejecucion> obtenerPasadas() {
        return ejecucionRepository.findEjecucionesPasadas();
    }
    
    /**
     * Obtener ejecuciones con cupos disponibles
     */
    public List<Ejecucion> obtenerConCuposDisponibles() {
        return ejecucionRepository.findEjecucionesConCuposDisponibles();
    }
    
    /**
     * Obtener ejecuciones con cupos disponibles para un curso específico
     */
    public List<Ejecucion> obtenerConCuposDisponiblesPorCurso(Long cursoId) {
        return ejecucionRepository.findEjecucionesConCuposDisponiblesByCurso(cursoId);
    }
    
    /**
     * Obtener ejecuciones por rango de fechas
     */
    public List<Ejecucion> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ejecucionRepository.findByFechaInicioBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener ejecuciones por rango de cupo
     */
    public List<Ejecucion> obtenerPorRangoCupo(Integer cupoMin, Integer cupoMax) {
        return ejecucionRepository.findByCupoMaximoBetween(cupoMin, cupoMax);
    }
    
    /**
     * Obtener ejecuciones ordenadas por fecha
     */
    public List<Ejecucion> obtenerOrdenadasPorFecha(boolean ascendente) {
        return ascendente ? ejecucionRepository.findAllByOrderByFechaInicioAsc() : ejecucionRepository.findAllByOrderByFechaInicioDesc();
    }
    
    /**
     * Obtener ejecuciones de un curso ordenadas por fecha
     */
    public List<Ejecucion> obtenerPorCursoOrdenadasPorFecha(Long cursoId, boolean ascendente) {
        
        return cursoRepository.findById(cursoId)
                .map(curso -> ascendente ? 
                     ejecucionRepository.findByCursoOrderByFechaInicioAsc(curso) : 
                     ejecucionRepository.findByCursoOrderByFechaInicioDesc(curso))
                .orElse(List.of());
    }
    
    /**
     * Contar estudiantes inscritos en una ejecución
     */
    public Integer contarEstudiantesInscritos(Long ejecucionId) {
        return ejecucionRepository.countEstudiantesInscritos(ejecucionId);
    }
    
    /**
     * Verificar si hay cupos disponibles
     */
    public boolean tieneCuposDisponibles(Long ejecucionId) {
        return ejecucionRepository.findById(ejecucionId)
                .map(ejecucion -> {
                    Integer inscritos = ejecucionRepository.countEstudiantesInscritos(ejecucionId);
                    return inscritos < ejecucion.getCuposDisponibles();
                })
                .orElse(false);
    }
    
    /**
     * Obtener número de cupos disponibles
     */
    public Integer obtenerCuposDisponibles(Long ejecucionId) {
        return ejecucionRepository.findById(ejecucionId)
                .map(ejecucion -> {
                    Integer inscritos = ejecucionRepository.countEstudiantesInscritos(ejecucionId);
                    return ejecucion.getCuposDisponibles() - inscritos;
                })
                .orElse(0);
    }
    
    /**
     * Verificar si existe ejecución activa para un curso
     */
    public boolean existeEjecucionActivaParaCurso(Long cursoId) {
        return ejecucionRepository.existeEjecucionActivaParaCurso(cursoId);
    }
    
    /**
     * Verificar si una ejecución está activa
     */
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
