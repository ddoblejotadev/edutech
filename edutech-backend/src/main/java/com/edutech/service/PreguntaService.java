package com.edutech.service;

import com.edutech.model.Pregunta;
import com.edutech.model.Evaluacion;
import com.edutech.repository.PreguntaRepository;
import com.edutech.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PreguntaService {
    
    private final PreguntaRepository preguntaRepository;
    private final EvaluacionRepository evaluacionRepository;
    
    /**
     * Obtener todas las preguntas
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerTodas() {
        log.debug("Obteniendo todas las preguntas");
        return preguntaRepository.findAll();
    }
    
    /**
     * Obtener pregunta por ID
     */
    @Transactional(readOnly = true)
    public Optional<Pregunta> obtenerPorId(Long id) {
        log.debug("Obteniendo pregunta con ID: {}", id);
        return preguntaRepository.findById(id);
    }
    
    /**
     * Crear nueva pregunta
     */
    public Pregunta crear(Pregunta pregunta) {
        log.debug("Creando nueva pregunta para evaluación: {}", pregunta.getEvaluacion().getId());
        
        // Validaciones de negocio
        validarEvaluacion(pregunta.getEvaluacion().getId());
        validarDatos(pregunta);
        
        // Asignar orden automáticamente si no se especifica
        if (pregunta.getOrden() == null) {
            Integer maxOrden = preguntaRepository.findMaxOrdenByEvaluacionId(pregunta.getEvaluacion().getId());
            pregunta.setOrden(maxOrden != null ? maxOrden + 1 : 1);
        } else {
            // Verificar que el orden no esté ocupado
            if (preguntaRepository.existsByEvaluacionIdAndOrden(pregunta.getEvaluacion().getId(), pregunta.getOrden())) {
                throw new IllegalArgumentException("Ya existe una pregunta con el orden " + pregunta.getOrden() + " en esta evaluación");
            }
        }
        
        return preguntaRepository.save(pregunta);
    }
    
    /**
     * Actualizar pregunta existente
     */
    public Pregunta actualizar(Long id, Pregunta preguntaActualizada) {
        log.debug("Actualizando pregunta con ID: {}", id);
        
        return preguntaRepository.findById(id)
                .map(preguntaExistente -> {
                    // Validaciones
                    validarEvaluacion(preguntaActualizada.getEvaluacion().getId());
                    validarDatos(preguntaActualizada);
                    
                    // Verificar cambio de orden
                    if (!preguntaExistente.getOrden().equals(preguntaActualizada.getOrden())) {
                        if (preguntaRepository.existsByEvaluacionIdAndOrden(
                                preguntaActualizada.getEvaluacion().getId(), preguntaActualizada.getOrden())) {
                            throw new IllegalArgumentException("Ya existe una pregunta con el orden " + preguntaActualizada.getOrden());
                        }
                    }
                    
                    // Actualizar campos
                    preguntaExistente.setTexto(preguntaActualizada.getTexto());
                    preguntaExistente.setPuntaje(preguntaActualizada.getPuntaje());
                    preguntaExistente.setOrden(preguntaActualizada.getOrden());
                    preguntaExistente.setEvaluacion(preguntaActualizada.getEvaluacion());
                    
                    return preguntaRepository.save(preguntaExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar pregunta
     */
    public void eliminar(Long id) {
        log.debug("Eliminando pregunta con ID: {}", id);
        
        Pregunta pregunta = preguntaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada con ID: " + id));
        
        // Verificar que la evaluación no haya comenzado
        // (Esta validación se podría implementar verificando la fecha de la evaluación)
        
        preguntaRepository.delete(pregunta);
    }
    
    /**
     * Obtener preguntas por evaluación
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPorEvaluacion(Long evaluacionId) {
        log.debug("Obteniendo preguntas de la evaluación: {}", evaluacionId);
        return preguntaRepository.findByEvaluacionId(evaluacionId);
    }
    
    /**
     * Obtener preguntas por evaluación ordenadas
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPorEvaluacionOrdenadas(Long evaluacionId) {
        log.debug("Obteniendo preguntas de la evaluación {} ordenadas", evaluacionId);
        return preguntaRepository.findByEvaluacionIdOrderByOrdenAsc(evaluacionId);
    }
    
    /**
     * Buscar preguntas por texto
     */
    @Transactional(readOnly = true)
    public List<Pregunta> buscarPorTexto(String texto) {
        log.debug("Buscando preguntas por texto: {}", texto);
        return preguntaRepository.findByTextoContainingIgnoreCase(texto);
    }
    
    /**
     * Buscar preguntas por palabra clave
     */
    @Transactional(readOnly = true)
    public List<Pregunta> buscarPorPalabraClave(String palabra) {
        log.debug("Buscando preguntas por palabra clave: {}", palabra);
        return preguntaRepository.findPreguntasByPalabraClave(palabra);
    }
    
    /**
     * Obtener preguntas por rango de puntaje
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPorRangoPuntaje(Double puntajeMin, Double puntajeMax) {
        log.debug("Obteniendo preguntas con puntaje entre {} y {}", puntajeMin, puntajeMax);
        return preguntaRepository.findByPuntajeBetween(puntajeMin, puntajeMax);
    }
    
    /**
     * Obtener preguntas por curso
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPorCurso(Long cursoId) {
        log.debug("Obteniendo preguntas del curso: {}", cursoId);
        return preguntaRepository.findPreguntasByCurso(cursoId);
    }
    
    /**
     * Obtener preguntas con opciones
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerConOpciones() {
        log.debug("Obteniendo preguntas que tienen opciones");
        return preguntaRepository.findPreguntasConOpciones();
    }
    
    /**
     * Obtener preguntas sin opciones
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerSinOpciones() {
        log.debug("Obteniendo preguntas sin opciones");
        return preguntaRepository.findPreguntasSinOpciones();
    }
    
    /**
     * Obtener preguntas por número de opciones
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPorNumeroOpciones(Integer numeroOpciones) {
        log.debug("Obteniendo preguntas con {} opciones", numeroOpciones);
        return preguntaRepository.findPreguntasByNumeroOpciones(numeroOpciones);
    }
    
    /**
     * Contar preguntas por evaluación
     */
    @Transactional(readOnly = true)
    public Integer contarPorEvaluacion(Long evaluacionId) {
        return preguntaRepository.countByEvaluacionId(evaluacionId);
    }
    
    /**
     * Contar opciones de una pregunta
     */
    @Transactional(readOnly = true)
    public Integer contarOpciones(Long preguntaId) {
        return preguntaRepository.countOpcionesDePregunta(preguntaId);
    }
    
    /**
     * Calcular puntaje total de una evaluación
     */
    @Transactional(readOnly = true)
    public Double calcularPuntajeTotalEvaluacion(Long evaluacionId) {
        return preguntaRepository.calcularPuntajeTotalEvaluacion(evaluacionId);
    }
    
    /**
     * Obtener pregunta por evaluación y orden
     */
    @Transactional(readOnly = true)
    public Optional<Pregunta> obtenerPorEvaluacionYOrden(Long evaluacionId, Integer orden) {
        return Optional.ofNullable(preguntaRepository.findByEvaluacionIdAndOrden(evaluacionId, orden));
    }
    
    /**
     * Reordenar preguntas de una evaluación
     */
    public void reordenarPreguntas(Long evaluacionId, List<Long> nuevoOrden) {
        log.debug("Reordenando preguntas de la evaluación: {}", evaluacionId);
        
        List<Pregunta> preguntas = preguntaRepository.findByEvaluacionIdOrderByOrdenAsc(evaluacionId);
        
        if (preguntas.size() != nuevoOrden.size()) {
            throw new IllegalArgumentException("El número de preguntas no coincide con el nuevo orden");
        }
        
        for (int i = 0; i < nuevoOrden.size(); i++) {
            Long preguntaId = nuevoOrden.get(i);
            Pregunta pregunta = preguntas.stream()
                    .filter(p -> p.getId().equals(preguntaId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + preguntaId));
            
            pregunta.setOrden(i + 1);
            preguntaRepository.save(pregunta);
        }
    }
    
    /**
     * Mover pregunta a nuevo orden
     */
    public Pregunta moverPregunta(Long preguntaId, Integer nuevoOrden) {
        log.debug("Moviendo pregunta {} al orden {}", preguntaId, nuevoOrden);
        
        Pregunta pregunta = preguntaRepository.findById(preguntaId)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada con ID: " + preguntaId));
        
        Long evaluacionId = pregunta.getEvaluacion().getId();
        Integer ordenActual = pregunta.getOrden();
        
        // Obtener todas las preguntas de la evaluación
        List<Pregunta> todasLasPreguntas = preguntaRepository.findByEvaluacionIdOrderByOrdenAsc(evaluacionId);
        
        // Reordenar
        if (nuevoOrden < ordenActual) {
            // Mover hacia arriba
            for (Pregunta p : todasLasPreguntas) {
                if (p.getOrden() >= nuevoOrden && p.getOrden() < ordenActual) {
                    p.setOrden(p.getOrden() + 1);
                    preguntaRepository.save(p);
                }
            }
        } else if (nuevoOrden > ordenActual) {
            // Mover hacia abajo
            for (Pregunta p : todasLasPreguntas) {
                if (p.getOrden() > ordenActual && p.getOrden() <= nuevoOrden) {
                    p.setOrden(p.getOrden() - 1);
                    preguntaRepository.save(p);
                }
            }
        }
        
        pregunta.setOrden(nuevoOrden);
        return preguntaRepository.save(pregunta);
    }
    
    // Métodos de validación privados
    
    private void validarEvaluacion(Long evaluacionId) {
        if (!evaluacionRepository.existsById(evaluacionId)) {
            throw new IllegalArgumentException("Evaluación no encontrada con ID: " + evaluacionId);
        }
    }
    
    private void validarDatos(Pregunta pregunta) {
        if (pregunta.getTexto() == null || pregunta.getTexto().trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la pregunta es obligatorio");
        }
        
        if (pregunta.getPuntaje() == null || pregunta.getPuntaje() <= 0) {
            throw new IllegalArgumentException("El puntaje debe ser mayor a 0");
        }
        
        if (pregunta.getOrden() != null && pregunta.getOrden() <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }
    }
}
