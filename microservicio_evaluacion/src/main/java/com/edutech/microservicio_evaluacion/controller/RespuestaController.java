package com.edutech.microservicio_evaluacion.controller;

import com.edutech.microservicio_evaluacion.model.Respuesta;
import com.edutech.microservicio_evaluacion.service.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/respuestas")
public class RespuestaController {
    
    private final RespuestaService respuestaService;
    
    @Autowired
    public RespuestaController(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }
    
    @GetMapping
    public ResponseEntity<List<Respuesta>> getAllRespuestas() {
        return ResponseEntity.ok(respuestaService.getAllRespuestas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Respuesta> getRespuestaById(@PathVariable Integer id) {
        Respuesta respuesta = respuestaService.getRespuestaById(id);
        if (respuesta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuesta);
    }
    
    @GetMapping("/estudiante/{rutEstudiante}")
    public ResponseEntity<List<Respuesta>> getRespuestasByEstudiante(@PathVariable String rutEstudiante) {
        return ResponseEntity.ok(respuestaService.getRespuestasByEstudiante(rutEstudiante));
    }
    
    @GetMapping("/pregunta/{idPregunta}")
    public ResponseEntity<List<Respuesta>> getRespuestasByPregunta(@PathVariable Integer idPregunta) {
        return ResponseEntity.ok(respuestaService.getRespuestasByPregunta(idPregunta));
    }
    
    @PostMapping
    public ResponseEntity<Respuesta> registrarRespuesta(@RequestBody Respuesta respuesta) {
        Respuesta nuevaRespuesta = respuestaService.registrarRespuesta(respuesta);
        if (nuevaRespuesta == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(nuevaRespuesta, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}/calificar")
    public ResponseEntity<Respuesta> calificarRespuestaDesarrollo(
            @PathVariable Integer id, 
            @RequestBody Map<String, Object> calificacion) {
        
        Boolean esCorrecta = (Boolean) calificacion.get("correcta");
        Double puntaje = Double.valueOf(calificacion.get("puntaje").toString());
        
        Respuesta respuestaCalificada = respuestaService.calificarRespuestaDesarrollo(id, esCorrecta, puntaje);
        if (respuestaCalificada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuestaCalificada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable Integer id) {
        if (respuestaService.eliminarRespuesta(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
