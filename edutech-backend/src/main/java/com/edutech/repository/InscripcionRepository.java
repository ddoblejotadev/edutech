package com.edutech.repository;

//Importacion Clase Modelo
import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;

//Importaciones para BD con SpringData JPA
import org.springframework.data.jpa.repository.JpaRepository;

//Importaciones personalizaciones JPA
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//Importacion para funcionamiento de repository
import org.springframework.stereotype.Repository;

//Importacion de Java
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    List<Inscripcion> findByPersonaId(Long estudianteId);
    
    List<Inscripcion> findByEjecucionId(Long ejecucionId);
    
    List<Inscripcion> findByPersonaIdAndActivoTrue(Long estudianteId);
    
    Optional<Inscripcion> findByPersonaIdAndEjecucionId(Long estudianteId, Long ejecucionId);
    
    boolean existsByPersonaIdAndEjecucionId(Long estudianteId, Long ejecucionId);
    
    boolean existsByPersonaIdAndEjecucionIdAndActivoTrue(Long estudianteId, Long ejecucionId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.persona.id = :estudianteId AND i.ejecucion.fechaInicio > CURRENT_DATE")
    List<Inscripcion> findInscripcionesFuturasByEstudiante(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.persona.id = :estudianteId AND i.ejecucion.fechaFin < CURRENT_DATE")
    List<Inscripcion> findInscripcionesPasadasByEstudiante(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT i.persona FROM Inscripcion i WHERE i.ejecucion.id = :ejecucionId")
    List<Persona> findEstudiantesByEjecucionId(@Param("ejecucionId") Long ejecucionId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.ejecucion.curso.id = :cursoId")
    List<Inscripcion> findByCursoId(@Param("cursoId") Long cursoId);
    
    Integer countByEjecucionId(Long ejecucionId);
    
    Integer countByPersonaId(Long estudianteId);
    
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.persona.id = :estudianteId AND i.ejecucion.curso.id = :cursoId AND i.activo = true")
    boolean existsByPersonaIdAndCursoId(@Param("estudianteId") Long estudianteId, @Param("cursoId") Long cursoId);
    
    List<Inscripcion> findTop10ByOrderByFechaInscripcionDesc();
}
