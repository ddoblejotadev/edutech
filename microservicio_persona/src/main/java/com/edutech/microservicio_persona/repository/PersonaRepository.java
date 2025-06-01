package com.edutech.microservicio_persona.repository;

import com.edutech.microservicio_persona.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    
    Optional<Persona> findByRut(String rut);
    
    Optional<Persona> findByCorreo(String correo);
    
    List<Persona> findByTipoPersonaIdTipoPersona(Integer idTipoPersona);
    
    List<Persona> findByNombreContainingIgnoreCase(String nombre);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Persona p WHERE p.rut = :rut")
    void deleteByRut(@Param("rut") String rut);
}
