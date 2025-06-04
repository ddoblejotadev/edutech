package com.edutech.repository;

//Importacion Clase Modelo
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
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    Optional<Persona> findByEmailIgnoreCase(String email);
    
    Optional<Persona> findByRut(String rut);
    
    List<Persona> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);
    
    List<Persona> findByTipoPersonaId(Long tipoPersonaId);
    
    List<Persona> findByTipoPersonaNombreIgnoreCase(String tipoNombre);
    
    boolean existsByRut(String rut);
    
    boolean existsByEmailIgnoreCase(String email);
    
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
}
