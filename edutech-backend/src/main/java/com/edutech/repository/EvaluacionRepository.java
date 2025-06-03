package com.edutech.repository;

import com.edutech.model.Evaluacion;
import com.edutech.model.Ejecucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    
    // Buscar evaluaciones por ejecución
    List<Evaluacion> findByEjecucion(Ejecucion ejecucion);
    List<Evaluacion> findByEjecucionId(Long ejecucionId);
    
    // Buscar evaluaciones por título
    List<Evaluacion> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar evaluaciones por descripción
    List<Evaluacion> findByDescripcionContainingIgnoreCase(String descripcion);
    
    // Buscar evaluaciones por duración
    List<Evaluacion> findByDuracionMinutosBetween(Integer duracionMin, Integer duracionMax);
    List<Evaluacion> findByDuracionMinutosGreaterThanEqual(Integer duracionMinima);
    
    // Buscar evaluaciones por puntaje máximo
    List<Evaluacion> findByPuntajeMaximoBetween(Double puntajeMin, Double puntajeMax);
    List<Evaluacion> findByPuntajeMaximoGreaterThanEqual(Double puntajeMinimo);
    
    // Buscar evaluaciones por fecha y hora de inicio
    List<Evaluacion> findByFechaHoraInicioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Evaluacion> findByFechaHoraInicioAfter(LocalDateTime fecha);
    List<Evaluacion> findByFechaHoraInicioBefore(LocalDateTime fecha);
    
    // Buscar evaluaciones activas (en progreso)
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaHoraInicio <= :ahora " +
           "AND e.fechaHoraInicio + FUNCTION('MINUTE', e.duracionMinutos) >= :ahora")
    List<Evaluacion> findEvaluacionesActivas(@Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones futuras
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaHoraInicio > :ahora")
    List<Evaluacion> findEvaluacionesFuturas(@Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones pasadas
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaHoraInicio + FUNCTION('MINUTE', e.duracionMinutos) < :ahora")
    List<Evaluacion> findEvaluacionesPasadas(@Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones de una ejecución ordenadas por fecha
    List<Evaluacion> findByEjecucionOrderByFechaHoraInicioAsc(Ejecucion ejecucion);
    List<Evaluacion> findByEjecucionOrderByFechaHoraInicioDesc(Ejecucion ejecucion);
    
    // Buscar evaluaciones de un curso específico
    @Query("SELECT e FROM Evaluacion e WHERE e.ejecucion.curso.id = :cursoId")
    List<Evaluacion> findEvaluacionesByCurso(@Param("cursoId") Long cursoId);
    
    // Buscar evaluaciones de un curso ordenadas por fecha
    @Query("SELECT e FROM Evaluacion e WHERE e.ejecucion.curso.id = :cursoId ORDER BY e.fechaHoraInicio ASC")
    List<Evaluacion> findEvaluacionesByCursoOrderByFecha(@Param("cursoId") Long cursoId);
    
    // Contar evaluaciones por ejecución
    Integer countByEjecucionId(Long ejecucionId);
    
    // Contar preguntas en una evaluación
    @Query("SELECT SIZE(e.preguntas) FROM Evaluacion e WHERE e.id = :evaluacionId")
    Integer countPreguntasEnEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    // Buscar evaluaciones con al menos una pregunta
    @Query("SELECT e FROM Evaluacion e WHERE SIZE(e.preguntas) > 0")
    List<Evaluacion> findEvaluacionesConPreguntas();
    
    // Buscar evaluaciones sin preguntas
    @Query("SELECT e FROM Evaluacion e WHERE SIZE(e.preguntas) = 0")
    List<Evaluacion> findEvaluacionesSinPreguntas();
    
    // Buscar evaluaciones por rango de número de preguntas
    @Query("SELECT e FROM Evaluacion e WHERE SIZE(e.preguntas) BETWEEN :min AND :max")
    List<Evaluacion> findEvaluacionesByNumeroPreguntasRange(@Param("min") Integer min, @Param("max") Integer max);
    
    // Verificar si existe una evaluación en una fecha específica para una ejecución
    boolean existsByEjecucionIdAndFechaHoraInicioBetween(Long ejecucionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar evaluaciones ordenadas por fecha de inicio
    List<Evaluacion> findAllByOrderByFechaHoraInicioAsc();
    List<Evaluacion> findAllByOrderByFechaHoraInicioDesc();
    
    // Buscar próximas evaluaciones para una ejecución
    @Query("SELECT e FROM Evaluacion e WHERE e.ejecucion.id = :ejecucionId " +
           "AND e.fechaHoraInicio > :ahora ORDER BY e.fechaHoraInicio ASC")
    List<Evaluacion> findProximasEvaluacionesDeEjecucion(@Param("ejecucionId") Long ejecucionId, 
                                                         @Param("ahora") LocalDateTime ahora);
}
