package com.edutech.microservicio_curso.repository;

import com.edutech.microservicio_curso.model.TipoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoCursoRepository extends JpaRepository<TipoCurso, Integer> {
    
    Optional<TipoCurso> findByNombreIgnoreCase(String nombre);
    
    boolean existsByNombreIgnoreCase(String nombre);
}