package com.edutech.repository;

//Importacion Clase Modelo
import com.edutech.model.TipoPersona;

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
public interface TipoPersonaRepository extends JpaRepository<TipoPersona, Long> {
    
    Optional<TipoPersona> findByNombre(String nombre);
    
    Optional<TipoPersona> findByNombreIgnoreCase(String nombre);
    
    boolean existsByNombre(String nombre);
    
    boolean existsByNombreIgnoreCase(String nombre);
    
    List<TipoPersona> findByActivoTrue();
    
    @Query("SELECT tp FROM TipoPersona tp WHERE tp.activo = true ORDER BY tp.nombre")
    List<TipoPersona> findAllActiveOrderByNombre();
    
    @Query("SELECT COUNT(p) FROM Persona p WHERE p.tipoPersona.id = :tipoPersonaId")
    Long countPersonasByTipoPersonaId(@Param("tipoPersonaId") Long tipoPersonaId);
    
    List<TipoPersona> findAllByOrderByNombreAsc();
    
    @Query("SELECT tp FROM TipoPersona tp WHERE EXISTS (SELECT p FROM Persona p WHERE p.tipoPersona = tp)")
    List<TipoPersona> findTiposConPersonasAsociadas();
    
    List<TipoPersona> findByNombreContainingIgnoreCase(String nombre);
}
