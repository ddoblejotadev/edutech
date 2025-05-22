package com.edutech.microservicio_evaluacion.repository;

import com.edutech.microservicio_evaluacion.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {
    List<Evaluacion> findByIdCurso(Integer idCurso);
    List<Evaluacion> findByIdEjecucion(Integer idEjecucion);
    List<Evaluacion> findByTipo(String tipo);
}
