package com.edutech.microservicio_ejecucion.repository;

import com.edutech.microservicio_ejecucion.model.EjecucionPersona;
import com.edutech.microservicio_ejecucion.model.EjecucionPersonaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjecucionPersonaRepository extends JpaRepository<EjecucionPersona, EjecucionPersonaId> {
    List<EjecucionPersona> findByIdRutPersona(String rutPersona);
    List<EjecucionPersona> findByIdIdEjecucion(Integer idEjecucion);
}
