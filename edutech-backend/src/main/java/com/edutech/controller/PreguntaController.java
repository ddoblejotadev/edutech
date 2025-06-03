package com.edutech.controller;

import com.edutech.model.Pregunta;
import com.edutech.service.PreguntaService;
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
@RequestMapping("/api/preguntas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PreguntaController {
    
    private final PreguntaService preguntaService;
    
    /**
     * Obtener todas las preguntas con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Pregunta>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/preguntas - Obteniendo preguntas con paginación: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Pregunta> preguntas = preguntaService.obtenerTodas(pageable);
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Obtener pregunta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pregunta> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/preguntas/{} - Obteniendo pregunta por ID", id);
        return preguntaService.obtenerPorId(id)
                .map(pregunta -> ResponseEntity.ok(pregunta))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crear nueva pregunta
     */
    @PostMapping
    public ResponseEntity<Pregunta> crear(@Valid @RequestBody Pregunta pregunta) {
        log.debug("POST /api/preguntas - Creando nueva pregunta");
        try {
            Pregunta preguntaCreada = preguntaService.crear(pregunta);
            return ResponseEntity.status(HttpStatus.CREATED).body(preguntaCreada);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear pregunta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar pregunta existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pregunta> actualizar(@PathVariable Long id, @Valid @RequestBody Pregunta pregunta) {
        log.debug("PUT /api/preguntas/{} - Actualizando pregunta", id);
        try {
            Pregunta preguntaActualizada = preguntaService.actualizar(id, pregunta);
            return ResponseEntity.ok(preguntaActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar pregunta: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Eliminar pregunta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/preguntas/{} - Eliminando pregunta", id);
        try {
            preguntaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar pregunta: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener preguntas por evaluación
     */
    @GetMapping("/evaluacion/{evaluacionId}")
    public ResponseEntity<List<Pregunta>> obtenerPorEvaluacion(@PathVariable Long evaluacionId) {
        log.debug("GET /api/preguntas/evaluacion/{} - Obteniendo preguntas por evaluación", evaluacionId);
        List<Pregunta> preguntas = preguntaService.obtenerPorEvaluacion(evaluacionId);
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Obtener preguntas por evaluación ordenadas
     */
    @GetMapping("/evaluacion/{evaluacionId}/ordenadas")
    public ResponseEntity<List<Pregunta>> obtenerPorEvaluacionOrdenadas(@PathVariable Long evaluacionId) {
        log.debug("GET /api/preguntas/evaluacion/{}/ordenadas - Obteniendo preguntas ordenadas por evaluación", evaluacionId);
        List<Pregunta> preguntas = preguntaService.obtenerPorEvaluacionOrdenadas(evaluacionId);
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Obtener preguntas por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Pregunta>> obtenerPorTipo(@PathVariable String tipo) {
        log.debug("GET /api/preguntas/tipo/{} - Obteniendo preguntas por tipo", tipo);
        List<Pregunta> preguntas = preguntaService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Buscar preguntas por texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Pregunta>> buscarPorTexto(@RequestParam String texto) {
        log.debug("GET /api/preguntas/buscar?texto={} - Buscando preguntas por texto", texto);
        List<Pregunta> preguntas = preguntaService.buscarPorTexto(texto);
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Obtener preguntas activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<Pregunta>> obtenerActivas() {
        log.debug("GET /api/preguntas/activas - Obteniendo preguntas activas");
        List<Pregunta> preguntas = preguntaService.obtenerActivas();
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Obtener preguntas por puntaje
     */
    @GetMapping("/puntaje/{puntaje}")
    public ResponseEntity<List<Pregunta>> obtenerPorPuntaje(@PathVariable Double puntaje) {
        log.debug("GET /api/preguntas/puntaje/{} - Obteniendo preguntas por puntaje", puntaje);
        List<Pregunta> preguntas = preguntaService.obtenerPorPuntaje(puntaje);
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Obtener preguntas por rango de puntaje
     */
    @GetMapping("/puntaje-rango")
    public ResponseEntity<List<Pregunta>> obtenerPorRangoPuntaje(
            @RequestParam Double minimo,
            @RequestParam Double maximo) {
        log.debug("GET /api/preguntas/puntaje-rango?minimo={}&maximo={} - Obteniendo preguntas por rango de puntaje", minimo, maximo);
        List<Pregunta> preguntas = preguntaService.obtenerPorRangoPuntaje(minimo, maximo);
        return ResponseEntity.ok(preguntas);
    }
    
    /**
     * Contar preguntas por evaluación
     */
    @GetMapping("/count/evaluacion/{evaluacionId}")
    public ResponseEntity<Long> contarPorEvaluacion(@PathVariable Long evaluacionId) {
        log.debug("GET /api/preguntas/count/evaluacion/{} - Contando preguntas por evaluación", evaluacionId);
        Long count = preguntaService.contarPorEvaluacion(evaluacionId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar preguntas por tipo
     */
    @GetMapping("/count/tipo/{tipo}")
    public ResponseEntity<Long> contarPorTipo(@PathVariable String tipo) {
        log.debug("GET /api/preguntas/count/tipo/{} - Contando preguntas por tipo", tipo);
        Long count = preguntaService.contarPorTipo(tipo);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Obtener siguiente orden para nueva pregunta
     */
    @GetMapping("/siguiente-orden/evaluacion/{evaluacionId}")
    public ResponseEntity<Integer> obtenerSiguienteOrden(@PathVariable Long evaluacionId) {
        log.debug("GET /api/preguntas/siguiente-orden/evaluacion/{} - Obteniendo siguiente orden", evaluacionId);
        Integer siguienteOrden = preguntaService.obtenerSiguienteOrden(evaluacionId);
        return ResponseEntity.ok(siguienteOrden);
    }
    
    /**
     * Cambiar orden de pregunta
     */
    @PutMapping("/{id}/orden/{nuevoOrden}")
    public ResponseEntity<Pregunta> cambiarOrden(@PathVariable Long id, @PathVariable Integer nuevoOrden) {
        log.debug("PUT /api/preguntas/{}/orden/{} - Cambiando orden de pregunta", id, nuevoOrden);
        try {
            Pregunta preguntaActualizada = preguntaService.cambiarOrden(id, nuevoOrden);
            return ResponseEntity.ok(preguntaActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar orden de pregunta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mover pregunta hacia arriba en el orden
     */
    @PutMapping("/{id}/mover-arriba")
    public ResponseEntity<Pregunta> moverArriba(@PathVariable Long id) {
        log.debug("PUT /api/preguntas/{}/mover-arriba - Moviendo pregunta hacia arriba", id);
        try {
            Pregunta preguntaMovida = preguntaService.moverArriba(id);
            return ResponseEntity.ok(preguntaMovida);
        } catch (IllegalArgumentException e) {
            log.error("Error al mover pregunta hacia arriba: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mover pregunta hacia abajo en el orden
     */
    @PutMapping("/{id}/mover-abajo")
    public ResponseEntity<Pregunta> moverAbajo(@PathVariable Long id) {
        log.debug("PUT /api/preguntas/{}/mover-abajo - Moviendo pregunta hacia abajo", id);
        try {
            Pregunta preguntaMovida = preguntaService.moverAbajo(id);
            return ResponseEntity.ok(preguntaMovida);
        } catch (IllegalArgumentException e) {
            log.error("Error al mover pregunta hacia abajo: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Reordenar preguntas de una evaluación
     */
    @PutMapping("/reordenar/evaluacion/{evaluacionId}")
    public ResponseEntity<List<Pregunta>> reordenar(@PathVariable Long evaluacionId) {
        log.debug("PUT /api/preguntas/reordenar/evaluacion/{} - Reordenando preguntas", evaluacionId);
        try {
            List<Pregunta> preguntasReordenadas = preguntaService.reordenar(evaluacionId);
            return ResponseEntity.ok(preguntasReordenadas);
        } catch (IllegalArgumentException e) {
            log.error("Error al reordenar preguntas: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Calcular puntaje total de una evaluación
     */
    @GetMapping("/puntaje-total/evaluacion/{evaluacionId}")
    public ResponseEntity<Double> calcularPuntajeTotal(@PathVariable Long evaluacionId) {
        log.debug("GET /api/preguntas/puntaje-total/evaluacion/{} - Calculando puntaje total", evaluacionId);
        Double puntajeTotal = preguntaService.calcularPuntajeTotal(evaluacionId);
        return ResponseEntity.ok(puntajeTotal);
    }
}
