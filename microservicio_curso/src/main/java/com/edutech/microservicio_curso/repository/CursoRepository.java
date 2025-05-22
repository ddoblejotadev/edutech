package com.edutech.microservicio_curso.repository;

import com.edutech.microservicio_curso.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    // Ignorar mayúsculas/minúsculas
    List<Curso> findByCategoriaIgnoreCase(String categoria);
    List<Curso> findByNombreContainingIgnoreCase(String nombre);
    List<Curso> findByTipoCursoId(Integer idTipoCurso);
}
