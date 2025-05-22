package com.edutech.microservicio_ejecucion.service;

import com.edutech.microservicio_ejecucion.model.Ejecucion;
import com.edutech.microservicio_ejecucion.repository.EjecucionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EjecucionService {
    
    private final EjecucionRepository ejecucionRepository;
    
    public EjecucionService(EjecucionRepository ejecucionRepository) {
        this.ejecucionRepository = ejecucionRepository;
    }
    
    public List<Ejecucion> getAllEjecuciones() {
        return ejecucionRepository.findAll();
    }
    
    public Ejecucion getEjecucionById(Integer id) {
        return ejecucionRepository.findById(id).orElse(null);
    }
    
    public List<Ejecucion> getEjecucionesByCurso(Integer idCurso) {
        return ejecucionRepository.findByIdCurso(idCurso);
    }
    
    public List<Ejecucion> getEjecucionesByEstado(String estado) {
        return ejecucionRepository.findByEstado(estado);
    }
    
    public Ejecucion createEjecucion(Ejecucion ejecucion) {
        return ejecucionRepository.save(ejecucion);
    }
    
    public Ejecucion updateEjecucion(Integer id, Ejecucion ejecucion) {
        if (!ejecucionRepository.existsById(id)) {
            return null;
        }
        
        ejecucion.setIdEjecucion(id);
        return ejecucionRepository.save(ejecucion);
    }
    
    public boolean deleteEjecucion(Integer id) {
        if (!ejecucionRepository.existsById(id)) {
            return false;
        }
        ejecucionRepository.deleteById(id);
        return true;
    }
}
