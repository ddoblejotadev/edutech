package com.edutech.repository;

import com.edutech.model.Inscripcion;
import com.edutech.model.Ejecucion;
import com.edutech.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    // Buscar inscripciones por estudiante
    List<Inscripcion> findByEstudiante(Persona estudiante);
    List<Inscripcion> findByEstudianteId(Long estudianteId);
    
    // Buscar inscripciones por ejecución
    List<Inscripcion> findByEjecucion(Ejecucion ejecucion);
    List<Inscripcion> findByEjecucionId(Long ejecucionId);
    
    // Buscar inscripción específica de un estudiante en una ejecución
    Optional<Inscripcion> findByEstudianteAndEjecucion(Persona estudiante, Ejecucion ejecucion);
    Optional<Inscripcion> findByEstudianteIdAndEjecucionId(Long estudianteId, Long ejecucionId);
    
    // Buscar inscripciones por fecha
    List<Inscripcion> findByFechaInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<Inscripcion> findByFechaInscripcionAfter(LocalDate fecha);
    List<Inscripcion> findByFechaInscripcionBefore(LocalDate fecha);
    
    // Buscar inscripciones de un estudiante en ejecuciones activas
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId " +
           "AND i.ejecucion.fechaInicio <= CURRENT_DATE AND i.ejecucion.fechaFin >= CURRENT_DATE")
    List<Inscripcion> findInscripcionesActivasDeEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Buscar inscripciones de un estudiante en ejecuciones futuras
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId " +
           "AND i.ejecucion.fechaInicio > CURRENT_DATE")
    List<Inscripcion> findInscripcionesFuturasDeEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Buscar inscripciones de un estudiante en ejecuciones pasadas
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId " +
           "AND i.ejecucion.fechaFin < CURRENT_DATE")
    List<Inscripcion> findInscripcionesPasadasDeEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Contar inscripciones por ejecución
    Integer countByEjecucionId(Long ejecucionId);
    
    // Contar inscripciones por estudiante
    Integer countByEstudianteId(Long estudianteId);
    
    // Buscar inscripciones por curso (a través de ejecución)
    @Query("SELECT i FROM Inscripcion i WHERE i.ejecucion.curso.id = :cursoId")
    List<Inscripcion> findInscripcionesByCurso(@Param("cursoId") Long cursoId);
    
    // Buscar estudiantes inscritos en una ejecución específica
    @Query("SELECT i.estudiante FROM Inscripcion i WHERE i.ejecucion.id = :ejecucionId")
    List<Persona> findEstudiantesInscritosEnEjecucion(@Param("ejecucionId") Long ejecucionId);
    
    // Buscar cursos en los que está inscrito un estudiante
    @Query("SELECT DISTINCT i.ejecucion.curso FROM Inscripcion i WHERE i.estudiante.id = :estudianteId")
    List<Object> findCursosDeEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Verificar si un estudiante está inscrito en una ejecución
    boolean existsByEstudianteIdAndEjecucionId(Long estudianteId, Long ejecucionId);
    
    // Verificar si un estudiante está inscrito en alguna ejecución de un curso
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.estudiante.id = :estudianteId " +
           "AND i.ejecucion.curso.id = :cursoId")
    boolean existeInscripcionEnCurso(@Param("estudianteId") Long estudianteId, @Param("cursoId") Long cursoId);
    
    // Buscar inscripciones ordenadas por fecha
    List<Inscripcion> findAllByOrderByFechaInscripcionDesc();
    List<Inscripcion> findByEstudianteIdOrderByFechaInscripcionDesc(Long estudianteId);
    
    // Buscar últimas inscripciones
    @Query("SELECT i FROM Inscripcion i ORDER BY i.fechaInscripcion DESC")
    List<Inscripcion> findUltimasInscripciones();
}
