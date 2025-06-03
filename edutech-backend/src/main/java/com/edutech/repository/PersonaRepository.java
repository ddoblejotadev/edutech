package com.edutech.repository;

import com.edutech.model.Persona;
import com.edutech.model.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    Optional<Persona> findByRut(String rut);
    
    Optional<Persona> findByCorreo(String correo);
    
    List<Persona> findByTipoPersona(TipoPersona tipoPersona);
    
    List<Persona> findByActivoTrue();
    
    @Query("SELECT p FROM Persona p WHERE p.tipoPersona.nombre = :tipoPersona AND p.activo = true")
    List<Persona> findByTipoPersonaNombre(@Param("tipoPersona") String tipoPersona);
    
    @Query("SELECT p FROM Persona p WHERE " +
           "(LOWER(p.nombres) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.apellidoPaterno) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.apellidoMaterno) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "p.rut LIKE CONCAT('%', :busqueda, '%') OR " +
           "LOWER(p.correo) LIKE LOWER(CONCAT('%', :busqueda, '%'))) " +
           "AND p.activo = true")
    List<Persona> buscarPersonas(@Param("busqueda") String busqueda);
    
    @Query("SELECT p FROM Persona p WHERE p.tipoPersona.nombre = 'ESTUDIANTE' AND p.activo = true ORDER BY p.apellidoPaterno, p.nombres")
    List<Persona> findEstudiantes();
    
    @Query("SELECT p FROM Persona p WHERE p.tipoPersona.nombre = 'PROFESOR' AND p.activo = true ORDER BY p.apellidoPaterno, p.nombres")
    List<Persona> findProfesores();
    
    boolean existsByRut(String rut);
    
    boolean existsByCorreo(String correo);
}
