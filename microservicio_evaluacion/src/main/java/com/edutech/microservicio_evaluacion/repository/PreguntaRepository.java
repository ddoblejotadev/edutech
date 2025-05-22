package com.edutech.microservicio_evaluacion.repository;

import com.edutech.microservicio_evaluacion.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {
    List<Pregunta> findByIdEvaluacion(Integer idEvaluacion);
}
