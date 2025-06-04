package com.edutech.repository;

//Importacion Clase Modelo
import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;

//Importaciones para BD con SpringData JPA
import org.springframework.data.jpa.repository.JpaRepository;

//Importaciones personalizaciones JPA
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//Importacion para funcionamiento de repository
import org.springframework.stereotype.Repository;

//Importacion de Java
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EjecucionRepository extends JpaRepository<Ejecucion, Long> {
    
    // Buscar ejecuciones por curso
    List<Ejecucion> findByCurso(Curso curso);
    List<Ejecucion> findByCursoId(Long cursoId);
    List<Ejecucion> findByCursoOrderByFechaInicioAsc(Curso curso);
    List<Ejecucion> findByCursoOrderByFechaInicioDesc(Curso curso);
    
    boolean existsByCursoIdAndSeccionAndPeriodo(Long cursoId, String seccion, String periodo);
    
    @Query("SELECT COUNT(e) > 0 FROM Ejecucion e WHERE e.curso.id = :cursoId AND e.estado = 'ACTIVO'")
    boolean existeEjecucionActivaParaCurso(@Param("cursoId") Long cursoId);
    
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
    @Query("SELECT e FROM Ejecucion e WHERE SIZE(e.inscripciones) < e.capacidadMaxima")
    List<Ejecucion> findEjecucionesConCuposDisponibles();
    
    // Buscar ejecuciones con cupos disponibles para un curso específico
    @Query("SELECT e FROM Ejecucion e WHERE e.curso.id = :cursoId AND SIZE(e.inscripciones) < e.capacidadMaxima")
    List<Ejecucion> findEjecucionesConCuposDisponiblesByCurso(@Param("cursoId") Long cursoId);
    
    // Contar estudiantes inscritos en una ejecución
    @Query("SELECT SIZE(e.inscripciones) FROM Ejecucion e WHERE e.id = :ejecucionId")
    Integer countEstudiantesInscritos(@Param("ejecucionId") Long ejecucionId);
    
    // Buscar ejecuciones por rango de capacidad máxima
    List<Ejecucion> findByCapacidadMaximaBetween(Integer cupoMin, Integer cupoMax);
    
    // Buscar ejecuciones ordenadas por fecha de inicio
    List<Ejecucion> findAllByOrderByFechaInicioAsc();
    List<Ejecucion> findAllByOrderByFechaInicioDesc();
}
