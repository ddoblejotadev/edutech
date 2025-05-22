package com.edutech.microservicio_ejecucion.service;

import com.edutech.microservicio_ejecucion.model.Ejecucion;
import com.edutech.microservicio_ejecucion.model.EjecucionPersona;
import com.edutech.microservicio_ejecucion.model.EjecucionPersonaId;
import com.edutech.microservicio_ejecucion.repository.EjecucionPersonaRepository;
import com.edutech.microservicio_ejecucion.repository.EjecucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InscripcionService {
    
    private final EjecucionPersonaRepository ejecucionPersonaRepository;
    private final EjecucionRepository ejecucionRepository;
    private final ClienteService clienteService;
    
    @Autowired
    public InscripcionService(EjecucionPersonaRepository ejecucionPersonaRepository, 
                             EjecucionRepository ejecucionRepository,
                             ClienteService clienteService) {
        this.ejecucionPersonaRepository = ejecucionPersonaRepository;
        this.ejecucionRepository = ejecucionRepository;
        this.clienteService = clienteService;
    }
    
    public boolean inscribirEstudiante(String rutPersona, Integer idEjecucion) {
        // Verificar si la persona existe
        if (clienteService.getPersonaByRut(rutPersona) == null) {
            return false;
        }
        
        // Verificar si la ejecución existe
        Optional<Ejecucion> ejecucion = ejecucionRepository.findById(idEjecucion);
        if (ejecucion.isEmpty()) {
            return false;
        }
        
        // Verificar si ya está inscrito
        EjecucionPersonaId id = new EjecucionPersonaId(rutPersona, idEjecucion);
        if (ejecucionPersonaRepository.existsById(id)) {
            return false;
        }
        
        // Crear la inscripción
        EjecucionPersona inscripcion = new EjecucionPersona();
        inscripcion.setId(id);
        inscripcion.setEjecucion(ejecucion.get());
        ejecucionPersonaRepository.save(inscripcion);
        
        return true;
    }
    
    public boolean cancelarInscripcion(String rutPersona, Integer idEjecucion) {
        EjecucionPersonaId id = new EjecucionPersonaId(rutPersona, idEjecucion);
        if (!ejecucionPersonaRepository.existsById(id)) {
            return false;
        }
        
        ejecucionPersonaRepository.deleteById(id);
        return true;
    }
    
    public List<EjecucionPersona> getInscripcionesByPersona(String rutPersona) {
        return ejecucionPersonaRepository.findByIdRutPersona(rutPersona);
    }
    
    public List<EjecucionPersona> getInscripcionesByEjecucion(Integer idEjecucion) {
        return ejecucionPersonaRepository.findByIdIdEjecucion(idEjecucion);
    }
}
