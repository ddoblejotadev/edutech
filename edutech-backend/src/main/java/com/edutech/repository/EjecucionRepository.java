package com.edutech.repository;

import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EjecucionRepository extends JpaRepository<Ejecucion, Long> {
    
    // Buscar ejecuciones por curso
    List<Ejecucion> findByCurso(Curso curso);
    List<Ejecucion> findByCursoId(Long cursoId);
    
    // Buscar ejecuciones por fechas
    List<Ejecucion> findByFechaInicioBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<Ejecucion> findByFechaFinBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Buscar ejecuciones activas (que están en progreso)
    @Query("SELECT e FROM Ejecucion e WHERE e.fechaInicio <= :fecha AND e.fechaFin >= :fecha")
    List<Ejecucion> findEjecucionesActivas(@Param("fecha") LocalDate fecha);
    
    // Buscar ejecuciones activas (sobrecarga para fecha actual)
    @Query("SELECT e FROM Ejecucion e WHERE e.fechaInicio <= CURRENT_DATE AND e.fechaFin >= CURRENT_DATE")
    List<Ejecucion> findEjecucionesActivas();
    
    // Buscar ejecuciones futuras
    @Query("SELECT e FROM Ejecucion e WHERE e.fechaInicio > CURRENT_DATE")
    List<Ejecucion> findEjecucionesFuturas();
    
    // Buscar ejecuciones pasadas
    @Query("SELECT e FROM Ejecucion e WHERE e.fechaFin < CURRENT_DATE")
    List<Ejecucion> findEjecucionesPasadas();
    
    // Buscar ejecuciones con cupos disponibles
    @Query("SELECT e FROM Ejecucion e WHERE SIZE(e.inscripciones) < e.cupoMaximo")
    List<Ejecucion> findEjecucionesConCuposDisponibles();
    
    // Buscar ejecuciones con cupos disponibles para un curso específico
    @Query("SELECT e FROM Ejecucion e WHERE e.curso.id = :cursoId AND SIZE(e.inscripciones) < e.cupoMaximo")
    List<Ejecucion> findEjecucionesConCuposDisponiblesByCurso(@Param("cursoId") Long cursoId);
    
    // Contar estudiantes inscritos en una ejecución
    @Query("SELECT SIZE(e.inscripciones) FROM Ejecucion e WHERE e.id = :ejecucionId")
    Integer countEstudiantesInscritos(@Param("ejecucionId") Long ejecucionId);
    
    // Buscar ejecuciones por rango de cupo máximo
    List<Ejecucion> findByCupoMaximoBetween(Integer cupoMin, Integer cupoMax);
    
    // Buscar ejecuciones ordenadas por fecha de inicio
    List<Ejecucion> findAllByOrderByFechaInicioAsc();
    List<Ejecucion> findAllByOrderByFechaInicioDesc();
    
    // Buscar ejecuciones de un curso ordenadas por fecha
    List<Ejecucion> findByCursoOrderByFechaInicioAsc(Curso curso);
    List<Ejecucion> findByCursoOrderByFechaInicioDesc(Curso curso);
    
    // Verificar si hay ejecuciones activas para un curso
    @Query("SELECT COUNT(e) > 0 FROM Ejecucion e WHERE e.curso.id = :cursoId AND e.fechaInicio <= CURRENT_DATE AND e.fechaFin >= CURRENT_DATE")
    boolean existeEjecucionActivaParaCurso(@Param("cursoId") Long cursoId);
}
