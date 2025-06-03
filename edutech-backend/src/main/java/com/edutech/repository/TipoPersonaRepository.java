package com.edutech.repository;

import com.edutech.model.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoPersonaRepository extends JpaRepository<TipoPersona, Long> {
    
    Optional<TipoPersona> findByNombre(String nombre);
    
    List<TipoPersona> findByActivoTrue();
    
    @Query("SELECT tp FROM TipoPersona tp WHERE tp.activo = true ORDER BY tp.nombre")
    List<TipoPersona> findAllActiveOrderByNombre();
    
    boolean existsByNombre(String nombre);
}
