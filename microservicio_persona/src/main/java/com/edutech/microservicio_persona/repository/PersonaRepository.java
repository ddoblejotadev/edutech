package com.edutech.microservicio_persona.repository;

import com.edutech.microservicio_persona.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    
    Optional<Persona> findByCorreo(String correo);
    
    List<Persona> findByTipoPersonaIdTipoPersona(Integer idTipoPersona);
    
    boolean existsByCorreo(String correo);
    
    @Query("SELECT p FROM Persona p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Persona> buscarPorNombre(@Param("nombre") String nombre);

    Optional<Persona> findByRut(String rut);
}
