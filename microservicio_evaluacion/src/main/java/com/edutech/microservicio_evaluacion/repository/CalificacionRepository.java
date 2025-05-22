package com.edutech.microservicio_evaluacion.repository;

import com.edutech.microservicio_evaluacion.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    List<Calificacion> findByIdEvaluacion(Integer idEvaluacion);
    List<Calificacion> findByRutEstudiante(String rutEstudiante);
    Optional<Calificacion> findByIdEvaluacionAndRutEstudiante(Integer idEvaluacion, String rutEstudiante);
}
