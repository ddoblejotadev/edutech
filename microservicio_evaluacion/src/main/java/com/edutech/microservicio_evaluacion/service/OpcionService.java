package com.edutech.microservicio_evaluacion.service;

import com.edutech.microservicio_evaluacion.model.Opcion;
import com.edutech.microservicio_evaluacion.repository.OpcionRepository;
import com.edutech.microservicio_evaluacion.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpcionService {
    
    private final OpcionRepository opcionRepository;
    private final PreguntaRepository preguntaRepository;
    
    @Autowired
    public OpcionService(OpcionRepository opcionRepository, PreguntaRepository preguntaRepository) {
        this.opcionRepository = opcionRepository;
        this.preguntaRepository = preguntaRepository;
    }
    
    public List<Opcion> getAllOpciones() {
        return opcionRepository.findAll();
    }
    
    public Opcion getOpcionById(Integer id) {
        return opcionRepository.findById(id).orElse(null);
    }
    
    public List<Opcion> getOpcionesByPregunta(Integer idPregunta) {
        return opcionRepository.findByIdPregunta(idPregunta);
    }
    
    public Opcion crearOpcion(Opcion opcion) {
        if (!preguntaRepository.existsById(opcion.getIdPregunta())) {
            return null;
        }
        
        return opcionRepository.save(opcion);
    }
    
    public Opcion actualizarOpcion(Integer id, Opcion opcionActualizada) {
        Optional<Opcion> opcionExistente = opcionRepository.findById(id);
        if (opcionExistente.isPresent()) {
            Opcion opcion = opcionExistente.get();
            
            opcion.setTexto(opcionActualizada.getTexto());
            opcion.setCorrecta(opcionActualizada.getCorrecta());
            
            return opcionRepository.save(opcion);
        }
        return null;
    }
    
    public boolean eliminarOpcion(Integer id) {
        if (opcionRepository.existsById(id)) {
            opcionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
