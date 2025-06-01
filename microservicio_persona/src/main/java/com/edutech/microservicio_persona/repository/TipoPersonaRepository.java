package com.edutech.microservicio_persona.repository;

import com.edutech.microservicio_persona.model.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPersonaRepository extends JpaRepository<TipoPersona, Long> {
}
