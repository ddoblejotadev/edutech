package com.edutech.microservicio_persona.service;

import com.edutech.microservicio_persona.model.TipoPersona;
import com.edutech.microservicio_persona.repository.TipoPersonaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoPersonaService {

    private final TipoPersonaRepository tipoPersonaRepository;

    public TipoPersonaService(TipoPersonaRepository tipoPersonaRepository) {
        this.tipoPersonaRepository = tipoPersonaRepository;
    }

    public List<TipoPersona> findAll() {
        return tipoPersonaRepository.findAll();
    }

    public Optional<TipoPersona> findById(Long id) {
        return tipoPersonaRepository.findById(id);
    }

    @Transactional
    public TipoPersona save(TipoPersona tipoPersona) {
        return tipoPersonaRepository.save(tipoPersona);
    }

    @Transactional
    public void deleteById(Long id) {
        tipoPersonaRepository.deleteById(id);
    }
}
