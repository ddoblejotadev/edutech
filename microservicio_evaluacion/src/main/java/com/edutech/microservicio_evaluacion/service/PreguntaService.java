package com.edutech.microservicio_evaluacion.service;

import com.edutech.microservicio_evaluacion.model.Pregunta;
import com.edutech.microservicio_evaluacion.repository.EvaluacionRepository;
import com.edutech.microservicio_evaluacion.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreguntaService {
    
    private final PreguntaRepository preguntaRepository;
    private final EvaluacionRepository evaluacionRepository;
    
    @Autowired
    public PreguntaService(PreguntaRepository preguntaRepository, EvaluacionRepository evaluacionRepository) {
        this.preguntaRepository = preguntaRepository;
        this.evaluacionRepository = evaluacionRepository;
    }
    
    public List<Pregunta> getAllPreguntas() {
        return preguntaRepository.findAll();
    }
    
    public Pregunta getPreguntaById(Integer id) {
        return preguntaRepository.findById(id).orElse(null);
    }
    
    public List<Pregunta> getPreguntasByEvaluacion(Integer idEvaluacion) {
        return preguntaRepository.findByIdEvaluacion(idEvaluacion);
    }
    
    public Pregunta crearPregunta(Pregunta pregunta) {
        if (!evaluacionRepository.existsById(pregunta.getIdEvaluacion())) {
            return null;
        }
        
        return preguntaRepository.save(pregunta);
    }
    
    public Pregunta actualizarPregunta(Integer id, Pregunta preguntaActualizada) {
        Optional<Pregunta> preguntaExistente = preguntaRepository.findById(id);
        if (preguntaExistente.isPresent()) {
            Pregunta pregunta = preguntaExistente.get();
            
            // Actualizar campos
            pregunta.setEnunciado(preguntaActualizada.getEnunciado());
            pregunta.setDescripcion(preguntaActualizada.getDescripcion());
            pregunta.setTipo(preguntaActualizada.getTipo());
            pregunta.setPuntaje(preguntaActualizada.getPuntaje());
            
            return preguntaRepository.save(pregunta);
        }
        return null;
    }
    
    public boolean eliminarPregunta(Integer id) {
        if (preguntaRepository.existsById(id)) {
            preguntaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
