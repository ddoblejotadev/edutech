package com.edutech.repository;

//Importacion Clase Modelo
import com.edutech.model.Evaluacion;
import com.edutech.model.Ejecucion;

//Importaciones para BD con SpringData JPA
import org.springframework.data.jpa.repository.JpaRepository;

//Importaciones personalizaciones JPA
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//Importacion para funcionamiento de repository
import org.springframework.stereotype.Repository;

//Importacion de Java
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
    
    // Buscar evaluaciones por puntaje total
    List<Evaluacion> findByPuntajeTotalBetween(Double puntajeMin, Double puntajeMax);
    List<Evaluacion> findByPuntajeTotalGreaterThanEqual(Double puntajeMinimo);
    
    // Buscar evaluaciones por fecha de inicio
    List<Evaluacion> findByFechaInicioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Evaluacion> findByFechaInicioAfter(LocalDateTime fecha);
    List<Evaluacion> findByFechaInicioBefore(LocalDateTime fecha);
    
    // Buscar evaluaciones activas (en progreso)
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaInicio <= :ahora AND e.fechaFin >= :ahora")
    List<Evaluacion> findEvaluacionesActivas(@Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones futuras
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaInicio > :ahora")
    List<Evaluacion> findEvaluacionesFuturas(@Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones pasadas
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaFin < :ahora")
    List<Evaluacion> findEvaluacionesPasadas(@Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones de una ejecución ordenadas por fecha
    List<Evaluacion> findByEjecucionOrderByFechaInicioAsc(Ejecucion ejecucion);
    List<Evaluacion> findByEjecucionOrderByFechaInicioDesc(Ejecucion ejecucion);
    
    // Buscar evaluaciones de un curso específico
    @Query("SELECT e FROM Evaluacion e WHERE e.ejecucion.curso.id = :cursoId")
    List<Evaluacion> findEvaluacionesByCurso(@Param("cursoId") Long cursoId);
    
    // Buscar evaluaciones de un curso ordenadas por fecha
    @Query("SELECT e FROM Evaluacion e WHERE e.ejecucion.curso.id = :cursoId ORDER BY e.fechaInicio ASC")
    List<Evaluacion> findEvaluacionesByCursoOrderByFecha(@Param("cursoId") Long cursoId);
    
    // Contar evaluaciones por ejecución
    Integer countByEjecucionId(Long ejecucionId);
    
    // Verificar si existe una evaluación en una fecha específica para una ejecución
    boolean existsByEjecucionIdAndFechaInicioBetween(Long ejecucionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar evaluaciones ordenadas por fecha de inicio
    List<Evaluacion> findAllByOrderByFechaInicioAsc();
    List<Evaluacion> findAllByOrderByFechaInicioDesc();
    
    // Buscar próximas evaluaciones para una ejecución
    @Query("SELECT e FROM Evaluacion e WHERE e.ejecucion.id = :ejecucionId " +
           "AND e.fechaInicio > :ahora ORDER BY e.fechaInicio ASC")
    List<Evaluacion> findProximasEvaluacionesDeEjecucion(@Param("ejecucionId") Long ejecucionId, 
                                                         @Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones por tipo (case insensitive)
    List<Evaluacion> findByTipoIgnoreCase(String tipo);
    
    // Buscar evaluaciones activas
    List<Evaluacion> findByActivoTrue();
    
    // Buscar evaluaciones publicadas
    List<Evaluacion> findByPublicadaTrue();
    
    // Buscar evaluaciones disponibles (activas, publicadas y en rango de fechas)
    @Query("SELECT e FROM Evaluacion e WHERE e.activo = true AND e.publicada = true " +
           "AND e.fechaInicio <= :ahora AND e.fechaFin > :ahora")
    List<Evaluacion> findByActivoTrueAndPublicadaTrueAndFechaInicioBeforeAndFechaFinAfter(
            @Param("ahora") LocalDateTime ahora);
    
    // Buscar evaluaciones vencidas por fecha fin
    List<Evaluacion> findByFechaFinBefore(LocalDateTime fecha);
    
    // Buscar evaluaciones por rango de fechas fin
    List<Evaluacion> findByFechaFinBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
