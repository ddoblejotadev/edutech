package com.edutech.repository;

//Importacion Clase Modelo
import com.edutech.model.Curso;

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
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    Optional<Curso> findByCodigo(String codigo);
    
    List<Curso> findByNombreContainingIgnoreCase(String nombre);
    
    List<Curso> findByDescripcionContainingIgnoreCase(String descripcion);
    
    List<Curso> findByArea(String area);
    
    List<Curso> findByModalidad(String modalidad);
    
    List<Curso> findByNivel(Integer nivel);
    
    List<Curso> findByCreditosBetween(Integer creditosMin, Integer creditosMax);
    
    List<Curso> findByHorasTeoricas(Integer horasTeoricas);
    
    List<Curso> findByHorasPracticas(Integer horasPracticas);
    
    List<Curso> findByActivoTrue();
    
    List<Curso> findAllByOrderByNombreAsc();
    
    List<Curso> findAllByOrderByNivelAsc();
    
    boolean existsByNombreIgnoreCase(String nombre);
    
    boolean existsByCodigo(String codigo);
    
    @Query("SELECT c FROM Curso c WHERE c.prerequisitoCursoCodigo IS NULL")
    List<Curso> findByPrerrequisitoIsNull();
    
    @Query("SELECT c FROM Curso c WHERE c.prerequisitoCursoCodigo = :prerequisitoCodigo")
    List<Curso> findByPrerrequisitoId(@Param("prerequisitoCodigo") String prerequisitoCodigo);
    
    @Query("SELECT c FROM Curso c WHERE (c.horasTeoricas + c.horasPracticas) BETWEEN :duracionMin AND :duracionMax")
    List<Curso> findByDuracionHorasBetween(@Param("duracionMin") Integer duracionMin, @Param("duracionMax") Integer duracionMax);
    
    @Query("SELECT c FROM Curso c ORDER BY (c.horasTeoricas + c.horasPracticas) ASC")
    List<Curso> findAllByOrderByDuracionHorasAsc();
    
    @Query("SELECT c FROM Curso c ORDER BY (c.horasTeoricas + c.horasPracticas) DESC")
    List<Curso> findAllByOrderByDuracionHorasDesc();
}
