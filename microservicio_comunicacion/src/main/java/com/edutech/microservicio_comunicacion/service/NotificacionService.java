package com.edutech.microservicio_comunicacion.service;

import com.edutech.microservicio_comunicacion.model.Notificacion;
import com.edutech.microservicio_comunicacion.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {
    
    private final NotificacionRepository notificacionRepository;
    private final ClienteService clienteService;
    
    public NotificacionService(NotificacionRepository notificacionRepository, ClienteService clienteService) {
        this.notificacionRepository = notificacionRepository;
        this.clienteService = clienteService;
    }
    
    public List<Notificacion> getAllNotificaciones() {
        return notificacionRepository.findAll();
    }
    
    public Notificacion getNotificacionById(Integer id) {
        return notificacionRepository.findById(id).orElse(null);
    }
    
    public List<Notificacion> getNotificacionesByPersona(Long idPersona) {
        return notificacionRepository.findByIdPersona(idPersona);
    }
    
    public List<Notificacion> getNotificacionesNoLeidasByPersona(Long idPersona) {
        return notificacionRepository.findByIdPersonaAndLeida(idPersona, false);
    }
    
    public List<Notificacion> getNotificacionesByTipo(String tipo) {
        return notificacionRepository.findByTipo(tipo);
    }
    
    public Notificacion crearNotificacion(Notificacion notificacion) {
        // Verificar que la persona existe
        if (!clienteService.existePersona(String.valueOf(notificacion.getIdPersona()))) {
            return null;
        }
        
        // Configurar valores por defecto
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setLeida(false);
        
        return notificacionRepository.save(notificacion);
    }
    
    public Notificacion marcarComoLeida(Integer idNotificacion) {
        Optional<Notificacion> optionalNotificacion = notificacionRepository.findById(idNotificacion);
        if (optionalNotificacion.isPresent()) {
            Notificacion notificacion = optionalNotificacion.get();
            notificacion.setLeida(true);
            return notificacionRepository.save(notificacion);
        }
        return null;
    }
    
    public boolean eliminarNotificacion(Integer idNotificacion) {
        if (notificacionRepository.existsById(idNotificacion)) {
            notificacionRepository.deleteById(idNotificacion);
            return true;
        }
        return false;
    }
}
