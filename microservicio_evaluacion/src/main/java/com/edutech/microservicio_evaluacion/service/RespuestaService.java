package com.edutech.microservicio_evaluacion.service;

import com.edutech.microservicio_evaluacion.model.Respuesta;
import com.edutech.microservicio_evaluacion.repository.OpcionRepository;
import com.edutech.microservicio_evaluacion.repository.PreguntaRepository;
import com.edutech.microservicio_evaluacion.repository.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RespuestaService {
    
    private final RespuestaRepository respuestaRepository;
    private final PreguntaRepository preguntaRepository;
    private final OpcionRepository opcionRepository;
    private final ClienteService clienteService;
    
    @Autowired
    public RespuestaService(RespuestaRepository respuestaRepository, 
                          PreguntaRepository preguntaRepository,
                          OpcionRepository opcionRepository,
                          ClienteService clienteService) {
        this.respuestaRepository = respuestaRepository;
        this.preguntaRepository = preguntaRepository;
        this.opcionRepository = opcionRepository;
        this.clienteService = clienteService;
    }
    
    public List<Respuesta> getAllRespuestas() {
        return respuestaRepository.findAll();
    }
    
    public Respuesta getRespuestaById(Integer id) {
        return respuestaRepository.findById(id).orElse(null);
    }
    
    public List<Respuesta> getRespuestasByEstudiante(String rutEstudiante) {
        return respuestaRepository.findByRutEstudiante(rutEstudiante);
    }
    
    public List<Respuesta> getRespuestasByPregunta(Integer idPregunta) {
        return respuestaRepository.findByIdPregunta(idPregunta);
    }
    
    public Respuesta registrarRespuesta(Respuesta respuesta) {
        // Verificar que la pregunta existe
        if (!preguntaRepository.existsById(respuesta.getIdPregunta())) {
            return null;
        }
        
        // Verificar que el estudiante existe
        if (!clienteService.existePersona(respuesta.getRutEstudiante())) {
            return null;
        }
        
        // Si hay una opción, verificar que existe y si es correcta
        if (respuesta.getIdOpcion() != null) {
            opcionRepository.findById(respuesta.getIdOpcion()).ifPresent(opcion -> {
                respuesta.setCorrecta(opcion.getCorrecta());
            });
        }
        
        return respuestaRepository.save(respuesta);
    }
    
    public Respuesta calificarRespuestaDesarrollo(Integer id, Boolean esCorrecta, Double puntaje) {
        Optional<Respuesta> respuestaOpt = respuestaRepository.findById(id);
        if (respuestaOpt.isEmpty()) {
            return null;
        }
        
        Respuesta respuesta = respuestaOpt.get();
        respuesta.setCorrecta(esCorrecta);
        respuesta.setPuntajeObtenido(puntaje);
        
        return respuestaRepository.save(respuesta);
    }
    
    public boolean eliminarRespuesta(Integer id) {
        if (respuestaRepository.existsById(id)) {
            respuestaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
