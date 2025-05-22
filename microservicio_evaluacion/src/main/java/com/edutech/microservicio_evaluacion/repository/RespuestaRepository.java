package com.edutech.microservicio_evaluacion.repository;

import com.edutech.microservicio_evaluacion.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    List<Respuesta> findByRutEstudiante(String rutEstudiante);
    List<Respuesta> findByIdPregunta(Integer idPregunta);
    List<Respuesta> findByIdPreguntaAndRutEstudiante(Integer idPregunta, String rutEstudiante);
}
