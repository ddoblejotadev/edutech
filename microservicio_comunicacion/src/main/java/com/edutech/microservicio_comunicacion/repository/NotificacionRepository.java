package com.edutech.microservicio_comunicacion.repository;

import com.edutech.microservicio_comunicacion.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByIdPersona(String idPersona);
    List<Notificacion> findByIdPersonaAndLeida(String idPersona, Boolean leida);
    List<Notificacion> findByTipo(String tipo);
}
