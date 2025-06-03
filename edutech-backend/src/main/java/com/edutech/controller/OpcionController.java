package com.edutech.controller;

import com.edutech.model.Opcion;
import com.edutech.service.OpcionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/opciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OpcionController {
    
    private final OpcionService opcionService;
    
    /**
     * Obtener todas las opciones con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Opcion>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/opciones - Obteniendo opciones con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Opcion> opciones = opcionService.obtenerTodas(pageable);
        return ResponseEntity.ok(opciones);
    }
    
    /**
     * Obtener opción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Opcion> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/opciones/{} - Obteniendo opción por ID", id);
        return opcionService.obtenerPorId(id)
                .map(opcion -> ResponseEntity.ok(opcion))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva opción
     */
    @PostMapping
    public ResponseEntity<Opcion> crear(@Valid @RequestBody Opcion opcion) {
        log.debug("POST /api/opciones - Creando nueva opción para pregunta: {}", opcion.getPregunta().getId());
        try {
            Opcion opcionCreada = opcionService.crear(opcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(opcionCreada);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear opción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar opción existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Opcion> actualizar(@PathVariable Long id, @Valid @RequestBody Opcion opcion) {
        log.debug("PUT /api/opciones/{} - Actualizando opción", id);
        try {
            Opcion opcionActualizada = opcionService.actualizar(id, opcion);
            return ResponseEntity.ok(opcionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar opción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar opción
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/opciones/{} - Eliminando opción", id);
        try {
            opcionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar opción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener opciones por pregunta
     */
    @GetMapping("/pregunta/{preguntaId}")
    public ResponseEntity<List<Opcion>> obtenerPorPregunta(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/pregunta/{} - Obteniendo opciones por pregunta", preguntaId);
        List<Opcion> opciones = opcionService.obtenerPorPregunta(preguntaId);
        return ResponseEntity.ok(opciones);
    }
    
    /**
     * Obtener opciones por pregunta ordenadas
     */
    @GetMapping("/pregunta/{preguntaId}/ordenadas")
    public ResponseEntity<List<Opcion>> obtenerPorPreguntaOrdenadas(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/pregunta/{}/ordenadas - Obteniendo opciones ordenadas por pregunta", preguntaId);
        List<Opcion> opciones = opcionService.obtenerPorPreguntaOrdenadas(preguntaId);
        return ResponseEntity.ok(opciones);
    }
    
    /**
     * Obtener opciones correctas por pregunta
     */
    @GetMapping("/pregunta/{preguntaId}/correctas")
    public ResponseEntity<List<Opcion>> obtenerCorrectasPorPregunta(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/pregunta/{}/correctas - Obteniendo opciones correctas por pregunta", preguntaId);
        List<Opcion> opciones = opcionService.obtenerCorrectasPorPregunta(preguntaId);
        return ResponseEntity.ok(opciones);
    }
    
    /**
     * Obtener opciones incorrectas por pregunta
     */
    @GetMapping("/pregunta/{preguntaId}/incorrectas")
    public ResponseEntity<List<Opcion>> obtenerIncorrectasPorPregunta(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/pregunta/{}/incorrectas - Obteniendo opciones incorrectas por pregunta", preguntaId);
        List<Opcion> opciones = opcionService.obtenerIncorrectasPorPregunta(preguntaId);
        return ResponseEntity.ok(opciones);
    }
    
    /**
     * Buscar opciones por texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Opcion>> buscarPorTexto(@RequestParam String texto) {
        log.debug("GET /api/opciones/buscar?texto={} - Buscando opciones por texto", texto);
        List<Opcion> opciones = opcionService.buscarPorTexto(texto);
        return ResponseEntity.ok(opciones);
    }
    
    /**
     * Contar opciones por pregunta
     */
    @GetMapping("/count/pregunta/{preguntaId}")
    public ResponseEntity<Long> contarPorPregunta(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/count/pregunta/{} - Contando opciones por pregunta", preguntaId);
        Long count = opcionService.contarPorPregunta(preguntaId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar opciones correctas por pregunta
     */
    @GetMapping("/count/pregunta/{preguntaId}/correctas")
    public ResponseEntity<Long> contarCorrectasPorPregunta(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/count/pregunta/{}/correctas - Contando opciones correctas por pregunta", preguntaId);
        Long count = opcionService.contarCorrectasPorPregunta(preguntaId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Verificar si una pregunta tiene opciones correctas
     */
    @GetMapping("/pregunta/{preguntaId}/tiene-correctas")
    public ResponseEntity<Boolean> tieneOpcionesCorrectas(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/pregunta/{}/tiene-correctas - Verificando opciones correctas", preguntaId);
        boolean tieneCorrectas = opcionService.tieneOpcionesCorrectas(preguntaId);
        return ResponseEntity.ok(tieneCorrectas);
    }
    
    /**
     * Obtener siguiente orden para nueva opción
     */
    @GetMapping("/siguiente-orden/pregunta/{preguntaId}")
    public ResponseEntity<Integer> obtenerSiguienteOrden(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/siguiente-orden/pregunta/{} - Obteniendo siguiente orden", preguntaId);
        Integer siguienteOrden = opcionService.obtenerSiguienteOrden(preguntaId);
        return ResponseEntity.ok(siguienteOrden);
    }
    
    /**
     * Cambiar orden de opción
     */
    @PutMapping("/{id}/orden/{nuevoOrden}")
    public ResponseEntity<Opcion> cambiarOrden(@PathVariable Long id, @PathVariable Integer nuevoOrden) {
        log.debug("PUT /api/opciones/{}/orden/{} - Cambiando orden de opción", id, nuevoOrden);
        try {
            Opcion opcionActualizada = opcionService.cambiarOrden(id, nuevoOrden);
            return ResponseEntity.ok(opcionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar orden de opción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mover opción hacia arriba en el orden
     */
    @PutMapping("/{id}/mover-arriba")
    public ResponseEntity<Opcion> moverArriba(@PathVariable Long id) {
        log.debug("PUT /api/opciones/{}/mover-arriba - Moviendo opción hacia arriba", id);
        try {
            Opcion opcionMovida = opcionService.moverArriba(id);
            return ResponseEntity.ok(opcionMovida);
        } catch (IllegalArgumentException e) {
            log.error("Error al mover opción hacia arriba: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mover opción hacia abajo en el orden
     */
    @PutMapping("/{id}/mover-abajo")
    public ResponseEntity<Opcion> moverAbajo(@PathVariable Long id) {
        log.debug("PUT /api/opciones/{}/mover-abajo - Moviendo opción hacia abajo", id);
        try {
            Opcion opcionMovida = opcionService.moverAbajo(id);
            return ResponseEntity.ok(opcionMovida);
        } catch (IllegalArgumentException e) {
            log.error("Error al mover opción hacia abajo: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Reordenar opciones de una pregunta
     */
    @PutMapping("/reordenar/pregunta/{preguntaId}")
    public ResponseEntity<List<Opcion>> reordenar(@PathVariable Long preguntaId) {
        log.debug("PUT /api/opciones/reordenar/pregunta/{} - Reordenando opciones", preguntaId);
        try {
            List<Opcion> opcionesReordenadas = opcionService.reordenar(preguntaId);
            return ResponseEntity.ok(opcionesReordenadas);
        } catch (IllegalArgumentException e) {
            log.error("Error al reordenar opciones: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Marcar opción como correcta
     */
    @PutMapping("/{id}/marcar-correcta")
    public ResponseEntity<Opcion> marcarComoCorrecta(@PathVariable Long id) {
        log.debug("PUT /api/opciones/{}/marcar-correcta - Marcando opción como correcta", id);
        try {
            Opcion opcionActualizada = opcionService.marcarComoCorrecta(id);
            return ResponseEntity.ok(opcionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al marcar opción como correcta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Marcar opción como incorrecta
     */
    @PutMapping("/{id}/marcar-incorrecta")
    public ResponseEntity<Opcion> marcarComoIncorrecta(@PathVariable Long id) {
        log.debug("PUT /api/opciones/{}/marcar-incorrecta - Marcando opción como incorrecta", id);
        try {
            Opcion opcionActualizada = opcionService.marcarComoIncorrecta(id);
            return ResponseEntity.ok(opcionActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al marcar opción como incorrecta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Validar configuración de opciones de una pregunta
     */
    @GetMapping("/validar/pregunta/{preguntaId}")
    public ResponseEntity<Boolean> validarConfiguracion(@PathVariable Long preguntaId) {
        log.debug("GET /api/opciones/validar/pregunta/{} - Validando configuración de opciones", preguntaId);
        boolean esValida = opcionService.validarConfiguracion(preguntaId);
        return ResponseEntity.ok(esValida);
    }
}
