package com.edutech.microservicio_evaluacion.service;

import com.edutech.microservicio_evaluacion.model.Calificacion;
import com.edutech.microservicio_evaluacion.repository.CalificacionRepository;
import com.edutech.microservicio_evaluacion.repository.EvaluacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CalificacionService {
    
    private final CalificacionRepository calificacionRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final ClienteService clienteService;
    
    public CalificacionService(CalificacionRepository calificacionRepository, 
                             EvaluacionRepository evaluacionRepository,
                             ClienteService clienteService) {
        this.calificacionRepository = calificacionRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.clienteService = clienteService;
    }
    
    public List<Calificacion> getAllCalificaciones() {
        return calificacionRepository.findAll();
    }
    
    public Calificacion getCalificacionById(Integer id) {
        return calificacionRepository.findById(id).orElse(null);
    }
    
    public List<Calificacion> getCalificacionesByEvaluacion(Integer idEvaluacion) {
        return calificacionRepository.findByIdEvaluacion(idEvaluacion);
    }
    
    public List<Calificacion> getCalificacionesByEstudiante(String rutEstudiante) {
        return calificacionRepository.findByRutEstudiante(rutEstudiante);
    }
    
    public Calificacion getCalificacionByEvaluacionAndEstudiante(Integer idEvaluacion, String rutEstudiante) {
        return calificacionRepository.findByIdEvaluacionAndRutEstudiante(idEvaluacion, rutEstudiante).orElse(null);
    }
    
    public Calificacion registrarCalificacion(Calificacion calificacion) {
        // Verificar que la evaluación existe
        if (!evaluacionRepository.existsById(calificacion.getIdEvaluacion())) {
            return null;
        }
        
        // Verificar que el estudiante existe
        if (!clienteService.existePersona(calificacion.getRutEstudiante())) {
            return null;
        }
        
        // Establecer la fecha de calificación
        calificacion.setFechaCalificacion(LocalDateTime.now());
        
        return calificacionRepository.save(calificacion);
    }
    
    public Calificacion actualizarCalificacion(Integer id, Calificacion calificacionActualizada) {
        Optional<Calificacion> calificacionExistente = calificacionRepository.findById(id);
        if (calificacionExistente.isPresent()) {
            Calificacion calificacion = calificacionExistente.get();
            
            calificacion.setPuntajeObtenido(calificacionActualizada.getPuntajeObtenido());
            calificacion.setComentario(calificacionActualizada.getComentario());
            calificacion.setFechaCalificacion(LocalDateTime.now());
            
            return calificacionRepository.save(calificacion);
        }
        return null;
    }
    
    public boolean eliminarCalificacion(Integer id) {
        if (calificacionRepository.existsById(id)) {
            calificacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public double calcularPromedioPorEvaluacion(Integer idEvaluacion) {
        List<Calificacion> calificaciones = calificacionRepository.findByIdEvaluacion(idEvaluacion);
        if (calificaciones.isEmpty()) {
            return 0.0;
        }
        
        double suma = calificaciones.stream()
                .mapToDouble(Calificacion::getPuntajeObtenido)
                .sum();
        
        return suma / calificaciones.size();
    }
    
    public double calcularPromedioPorEstudiante(String rutEstudiante) {
        List<Calificacion> calificaciones = calificacionRepository.findByRutEstudiante(rutEstudiante);
        if (calificaciones.isEmpty()) {
            return 0.0;
        }
        
        double suma = calificaciones.stream()
                .mapToDouble(Calificacion::getPuntajeObtenido)
                .sum();
        
        return suma / calificaciones.size();
    }
}
