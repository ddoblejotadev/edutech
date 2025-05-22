package com.edutech.microservicio_comunicacion.controller;

import com.edutech.microservicio_comunicacion.model.Mensaje;
import com.edutech.microservicio_comunicacion.service.MensajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {
    
    private final MensajeService mensajeService;
    
    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }
    
    @GetMapping
    public ResponseEntity<List<Mensaje>> getAllMensajes() {
        return ResponseEntity.ok(mensajeService.getAllMensajes());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Mensaje> getMensajeById(@PathVariable Integer id) {
        Mensaje mensaje = mensajeService.getMensajeById(id);
        if (mensaje == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mensaje);
    }
    
    @GetMapping("/enviados/{remitente}")
    public ResponseEntity<List<Mensaje>> getMensajesByRemitente(@PathVariable String remitente) {
        return ResponseEntity.ok(mensajeService.getMensajesByRemitente(remitente));
    }
    
    @GetMapping("/recibidos/{destinatario}")
    public ResponseEntity<List<Mensaje>> getMensajesByDestinatario(@PathVariable String destinatario) {
        return ResponseEntity.ok(mensajeService.getMensajesByDestinatario(destinatario));
    }
    
    @GetMapping("/noleidos/{destinatario}")
    public ResponseEntity<List<Mensaje>> getMensajesNoLeidosByDestinatario(@PathVariable String destinatario) {
        return ResponseEntity.ok(mensajeService.getMensajesNoLeidosByDestinatario(destinatario));
    }
    
    @PostMapping
    public ResponseEntity<Mensaje> enviarMensaje(@RequestBody Mensaje mensaje) {
        Mensaje nuevoMensaje = mensajeService.enviarMensaje(mensaje);
        if (nuevoMensaje == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(nuevoMensaje, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}/leer")
    public ResponseEntity<Mensaje> marcarComoLeido(@PathVariable Integer id) {
        Mensaje mensaje = mensajeService.marcarComoLeido(id);
        if (mensaje == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mensaje);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Integer id) {
        if (mensajeService.eliminarMensaje(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
