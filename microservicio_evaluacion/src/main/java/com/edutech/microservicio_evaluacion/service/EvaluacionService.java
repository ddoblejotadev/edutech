package com.edutech.microservicio_evaluacion.service;

import com.edutech.microservicio_evaluacion.model.Evaluacion;
import com.edutech.microservicio_evaluacion.repository.EvaluacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {
    
    private final EvaluacionRepository evaluacionRepository;
    private final ClienteService clienteService;
    
    public EvaluacionService(EvaluacionRepository evaluacionRepository, ClienteService clienteService) {
        this.evaluacionRepository = evaluacionRepository;
        this.clienteService = clienteService;
    }
    
    public List<Evaluacion> getAllEvaluaciones() {
        return evaluacionRepository.findAll();
    }
    
    public Evaluacion getEvaluacionById(Integer id) {
        return evaluacionRepository.findById(id).orElse(null);
    }
    
    public List<Evaluacion> getEvaluacionesByCurso(Integer idCurso) {
        return evaluacionRepository.findByIdCurso(idCurso);
    }
    
    public List<Evaluacion> getEvaluacionesByEjecucion(Integer idEjecucion) {
        return evaluacionRepository.findByIdEjecucion(idEjecucion);
    }
    
    public List<Evaluacion> getEvaluacionesByTipo(String tipo) {
        return evaluacionRepository.findByTipo(tipo);
    }
    
    public Evaluacion crearEvaluacion(Evaluacion evaluacion) {
        // Verificar que el curso existe
        if (!clienteService.existeCurso(evaluacion.getIdCurso())) {
            return null;
        }
        
        // Verificar que la ejecución existe
        if (!clienteService.existeEjecucion(evaluacion.getIdEjecucion())) {
            return null;
        }
        
        // Establecer la fecha de creación
        evaluacion.setFechaCreacion(LocalDateTime.now());
        
        return evaluacionRepository.save(evaluacion);
    }
    
    public Evaluacion actualizarEvaluacion(Integer id, Evaluacion evaluacionActualizada) {
        Optional<Evaluacion> evaluacionExistente = evaluacionRepository.findById(id);
        if (evaluacionExistente.isPresent()) {
            Evaluacion evaluacion = evaluacionExistente.get();
            
            // Actualizar campos permitidos
            evaluacion.setTitulo(evaluacionActualizada.getTitulo());
            evaluacion.setDescripcion(evaluacionActualizada.getDescripcion());
            evaluacion.setFechaVencimiento(evaluacionActualizada.getFechaVencimiento());
            evaluacion.setPuntajeMaximo(evaluacionActualizada.getPuntajeMaximo());
            evaluacion.setTipo(evaluacionActualizada.getTipo());
            
            return evaluacionRepository.save(evaluacion);
        }
        return null;
    }
    
    public boolean eliminarEvaluacion(Integer id) {
        if (evaluacionRepository.existsById(id)) {
            evaluacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
