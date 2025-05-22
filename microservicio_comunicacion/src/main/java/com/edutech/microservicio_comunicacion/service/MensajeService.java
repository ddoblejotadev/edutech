package com.edutech.microservicio_comunicacion.service;

import com.edutech.microservicio_comunicacion.model.Mensaje;
import com.edutech.microservicio_comunicacion.repository.MensajeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {
    
    private final MensajeRepository mensajeRepository;
    private final ClienteService clienteService;
    
    public MensajeService(MensajeRepository mensajeRepository, ClienteService clienteService) {
        this.mensajeRepository = mensajeRepository;
        this.clienteService = clienteService;
    }
    
    public List<Mensaje> getAllMensajes() {
        return mensajeRepository.findAll();
    }
    
    public Mensaje getMensajeById(Integer id) {
        return mensajeRepository.findById(id).orElse(null);
    }
    
    public List<Mensaje> getMensajesByRemitente(String remitente) {
        return mensajeRepository.findByRemitente(remitente);
    }
    
    public List<Mensaje> getMensajesByDestinatario(String destinatario) {
        return mensajeRepository.findByDestinatario(destinatario);
    }
    
    public List<Mensaje> getMensajesNoLeidosByDestinatario(String destinatario) {
        return mensajeRepository.findByDestinatarioAndLeido(destinatario, false);
    }
    
    public Mensaje enviarMensaje(Mensaje mensaje) {
        // Verificar que remitente y destinatario existen
        if (!clienteService.existePersona(mensaje.getRemitente()) || 
            !clienteService.existePersona(mensaje.getDestinatario())) {
            return null;
        }
        
        // Configurar valores por defecto
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setLeido(false);
        mensaje.setFechaLectura(null);
        
        return mensajeRepository.save(mensaje);
    }
    
    public Mensaje marcarComoLeido(Integer idMensaje) {
        Optional<Mensaje> optionalMensaje = mensajeRepository.findById(idMensaje);
        if (optionalMensaje.isPresent()) {
            Mensaje mensaje = optionalMensaje.get();
            mensaje.setLeido(true);
            mensaje.setFechaLectura(LocalDateTime.now());
            return mensajeRepository.save(mensaje);
        }
        return null;
    }
    
    public boolean eliminarMensaje(Integer idMensaje) {
        if (mensajeRepository.existsById(idMensaje)) {
            mensajeRepository.deleteById(idMensaje);
            return true;
        }
        return false;
    }
}
