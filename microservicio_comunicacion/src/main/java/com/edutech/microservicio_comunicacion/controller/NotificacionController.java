package com.edutech.microservicio_comunicacion.controller;

import com.edutech.microservicio_comunicacion.model.Notificacion;
import com.edutech.microservicio_comunicacion.service.NotificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    
    private final NotificacionService notificacionService;
    
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    @GetMapping
    public ResponseEntity<List<Notificacion>> getAllNotificaciones() {
        return ResponseEntity.ok(notificacionService.getAllNotificaciones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> getNotificacionById(@PathVariable Integer id) {
        Notificacion notificacion = notificacionService.getNotificacionById(id);
        if (notificacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notificacion);
    }
    
    @GetMapping("/persona/{idPersona}")
    public ResponseEntity<List<Notificacion>> getNotificacionesByPersona(@PathVariable Long idPersona) {
        return ResponseEntity.ok(notificacionService.getNotificacionesByPersona(idPersona));
    }
    
    @GetMapping("/noleidas/{idPersona}")
    public ResponseEntity<List<Notificacion>> getNotificacionesNoLeidasByPersona(@PathVariable Long idPersona) {
        return ResponseEntity.ok(notificacionService.getNotificacionesNoLeidasByPersona(idPersona));
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Notificacion>> getNotificacionesByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(notificacionService.getNotificacionesByTipo(tipo));
    }
    
    @PostMapping
    public ResponseEntity<Notificacion> crearNotificacion(@RequestBody Notificacion notificacion) {
        Notificacion nuevaNotificacion = notificacionService.crearNotificacion(notificacion);
        if (nuevaNotificacion == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Integer id) {
        Notificacion notificacion = notificacionService.marcarComoLeida(id);
        if (notificacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notificacion);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Integer id) {
        if (notificacionService.eliminarNotificacion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
