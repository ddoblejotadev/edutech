package com.edutech.microservicio_ejecucion.repository;

import com.edutech.microservicio_ejecucion.model.Ejecucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjecucionRepository extends JpaRepository<Ejecucion, Integer> {
    List<Ejecucion> findByIdCurso(Integer idCurso);
    List<Ejecucion> findByEstado(String estado);
}
