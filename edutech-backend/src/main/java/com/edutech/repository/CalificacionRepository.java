package com.edutech.repository;

import com.edutech.model.Calificacion;
import com.edutech.model.Evaluacion;
import com.edutech.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    
    // Buscar calificaciones por estudiante
    List<Calificacion> findByEstudiante(Persona estudiante);
    List<Calificacion> findByEstudianteId(Long estudianteId);
    
    // Buscar calificaciones por evaluación
    List<Calificacion> findByEvaluacion(Evaluacion evaluacion);
    List<Calificacion> findByEvaluacionId(Long evaluacionId);
    
    // Buscar calificación específica de un estudiante en una evaluación
    Optional<Calificacion> findByEstudianteAndEvaluacion(Persona estudiante, Evaluacion evaluacion);
    Optional<Calificacion> findByEstudianteIdAndEvaluacionId(Long estudianteId, Long evaluacionId);
    
    // Buscar calificaciones por rango de puntaje
    List<Calificacion> findByPuntajeObtenidoBetween(Double puntajeMin, Double puntajeMax);
    List<Calificacion> findByPuntajeObtenidoGreaterThanEqual(Double puntajeMinimo);
    List<Calificacion> findByPuntajeObtenidoLessThanEqual(Double puntajeMaximo);
    
    // Buscar calificaciones por fecha de realización
    List<Calificacion> findByFechaRealizacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Calificacion> findByFechaRealizacionAfter(LocalDateTime fecha);
    List<Calificacion> findByFechaRealizacionBefore(LocalDateTime fecha);
    
    // Buscar calificaciones de un estudiante ordenadas por fecha
    List<Calificacion> findByEstudianteOrderByFechaRealizacionDesc(Persona estudiante);
    List<Calificacion> findByEstudianteIdOrderByFechaRealizacionDesc(Long estudianteId);
    
    // Buscar calificaciones de una evaluación ordenadas por puntaje
    List<Calificacion> findByEvaluacionOrderByPuntajeObtenidoDesc(Evaluacion evaluacion);
    List<Calificacion> findByEvaluacionOrderByPuntajeObtenidoAsc(Evaluacion evaluacion);
    List<Calificacion> findByEvaluacionIdOrderByPuntajeObtenidoDesc(Long evaluacionId);
    
    // Estadísticas de calificaciones por evaluación
    @Query("SELECT AVG(c.puntajeObtenido) FROM Calificacion c WHERE c.evaluacion.id = :evaluacionId")
    Double calcularPromedioByEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    @Query("SELECT MAX(c.puntajeObtenido) FROM Calificacion c WHERE c.evaluacion.id = :evaluacionId")
    Double findMaxPuntajeByEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    @Query("SELECT MIN(c.puntajeObtenido) FROM Calificacion c WHERE c.evaluacion.id = :evaluacionId")
    Double findMinPuntajeByEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    // Estadísticas de calificaciones por estudiante
    @Query("SELECT AVG(c.puntajeObtenido) FROM Calificacion c WHERE c.estudiante.id = :estudianteId")
    Double calcularPromedioByEstudiante(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT MAX(c.puntajeObtenido) FROM Calificacion c WHERE c.estudiante.id = :estudianteId")
    Double findMaxPuntajeByEstudiante(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT MIN(c.puntajeObtenido) FROM Calificacion c WHERE c.estudiante.id = :estudianteId")
    Double findMinPuntajeByEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Contar calificaciones
    Integer countByEvaluacionId(Long evaluacionId);
    Integer countByEstudianteId(Long estudianteId);
    
    // Buscar calificaciones de un curso específico
    @Query("SELECT c FROM Calificacion c WHERE c.evaluacion.ejecucion.curso.id = :cursoId")
    List<Calificacion> findCalificacionesByCurso(@Param("cursoId") Long cursoId);
    
    // Buscar calificaciones de una ejecución específica
    @Query("SELECT c FROM Calificacion c WHERE c.evaluacion.ejecucion.id = :ejecucionId")
    List<Calificacion> findCalificacionesByEjecucion(@Param("ejecucionId") Long ejecucionId);
    
    // Buscar calificaciones por porcentaje de acierto
    @Query("SELECT c FROM Calificacion c WHERE " +
           "(c.puntajeObtenido / c.evaluacion.puntajeMaximo * 100) BETWEEN :porcentajeMin AND :porcentajeMax")
    List<Calificacion> findCalificacionesByPorcentajeRange(@Param("porcentajeMin") Double porcentajeMin, 
                                                          @Param("porcentajeMax") Double porcentajeMax);
    
    // Buscar estudiantes que aprobaron una evaluación (≥60%)
    @Query("SELECT c FROM Calificacion c WHERE " +
           "(c.puntajeObtenido / c.evaluacion.puntajeMaximo * 100) >= 60")
    List<Calificacion> findCalificacionesAprobadas();
    
    // Buscar estudiantes que reprobaron una evaluación (<60%)
    @Query("SELECT c FROM Calificacion c WHERE " +
           "(c.puntajeObtenido / c.evaluacion.puntajeMaximo * 100) < 60")
    List<Calificacion> findCalificacionesReprobadas();
    
    // Buscar mejores calificaciones (top N)
    @Query("SELECT c FROM Calificacion c ORDER BY c.puntajeObtenido DESC")
    List<Calificacion> findTopCalificaciones();
    
    // Verificar si un estudiante ya tiene calificación en una evaluación
    boolean existsByEstudianteIdAndEvaluacionId(Long estudianteId, Long evaluacionId);
    
    // Buscar calificaciones recientes
    @Query("SELECT c FROM Calificacion c ORDER BY c.fechaRealizacion DESC")
    List<Calificacion> findCalificacionesRecientes();
    
    // Calcular porcentaje de aprobación por evaluación
    @Query("SELECT " +
           "COUNT(CASE WHEN (c.puntajeObtenido / c.evaluacion.puntajeMaximo * 100) >= 60 THEN 1 END) * 100.0 / COUNT(c) " +
           "FROM Calificacion c WHERE c.evaluacion.id = :evaluacionId")
    Double calcularPorcentajeAprobacionByEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    // Buscar calificaciones de un estudiante en un curso específico
    @Query("SELECT c FROM Calificacion c WHERE c.estudiante.id = :estudianteId " +
           "AND c.evaluacion.ejecucion.curso.id = :cursoId")
    List<Calificacion> findCalificacionesDeEstudianteEnCurso(@Param("estudianteId") Long estudianteId, 
                                                            @Param("cursoId") Long cursoId);
}
