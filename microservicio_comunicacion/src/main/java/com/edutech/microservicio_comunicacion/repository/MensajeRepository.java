package com.edutech.microservicio_comunicacion.repository;

import com.edutech.microservicio_comunicacion.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findByRemitente(String remitente);
    List<Mensaje> findByDestinatario(String destinatario);
    List<Mensaje> findByDestinatarioAndLeido(String destinatario, Boolean leido);
}
