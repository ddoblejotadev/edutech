package com.edutech.microservicio_evaluacion.repository;

import com.edutech.microservicio_evaluacion.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Integer> {
    List<Opcion> findByIdPregunta(Integer idPregunta);
}
