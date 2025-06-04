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
    
    List<Inscripcion> findByEstudianteId(Long estudianteId);
    
    List<Inscripcion> findByEjecucionId(Long ejecucionId);
    
    List<Inscripcion> findByEstudianteIdAndActivoTrue(Long estudianteId);
    
    Optional<Inscripcion> findByEstudianteIdAndEjecucionId(Long estudianteId, Long ejecucionId);
    
    boolean existsByEstudianteIdAndEjecucionId(Long estudianteId, Long ejecucionId);
    
    boolean existsByEstudianteIdAndEjecucionIdAndActivoTrue(Long estudianteId, Long ejecucionId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.ejecucion.fechaInicio > CURRENT_DATE")
    List<Inscripcion> findInscripcionesFuturasByEstudiante(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.ejecucion.fechaFin < CURRENT_DATE")
    List<Inscripcion> findInscripcionesPasadasByEstudiante(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT i.persona FROM Inscripcion i WHERE i.ejecucion.id = :ejecucionId")
    List<Persona> findEstudiantesByEjecucionId(@Param("ejecucionId") Long ejecucionId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.ejecucion.curso.id = :cursoId")
    List<Inscripcion> findByCursoId(@Param("cursoId") Long cursoId);
    
    Integer countByEjecucionId(Long ejecucionId);
    
    Integer countByEstudianteId(Long estudianteId);
    
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.ejecucion.curso.id = :cursoId AND i.activo = true")
    boolean existsByEstudianteIdAndCursoId(@Param("estudianteId") Long estudianteId, @Param("cursoId") Long cursoId);
    
    List<Inscripcion> findTop10ByOrderByFechaInscripcionDesc();
}
