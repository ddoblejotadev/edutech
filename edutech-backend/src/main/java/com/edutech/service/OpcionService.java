package com.edutech.service;

import com.edutech.model.Opcion;
import com.edutech.model.Pregunta;
import com.edutech.repository.OpcionRepository;
import com.edutech.repository.PreguntaRepository;
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
public class OpcionService {
    
    private final OpcionRepository opcionRepository;
    private final PreguntaRepository preguntaRepository;
    
    /**
     * Obtener todas las opciones
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerTodas() {
        log.debug("Obteniendo todas las opciones");
        return opcionRepository.findAll();
    }
    
    /**
     * Obtener opción por ID
     */
    @Transactional(readOnly = true)
    public Optional<Opcion> obtenerPorId(Long id) {
        log.debug("Obteniendo opción con ID: {}", id);
        return opcionRepository.findById(id);
    }
    
    /**
     * Crear nueva opción
     */
    public Opcion crear(Opcion opcion) {
        log.debug("Creando nueva opción para pregunta: {}", opcion.getPregunta().getId());
        
        // Validaciones de negocio
        validarPregunta(opcion.getPregunta().getId());
        validarDatos(opcion);
        
        // Asignar orden automáticamente si no se especifica
        if (opcion.getOrden() == null) {
            Integer maxOrden = opcionRepository.findMaxOrdenByPreguntaId(opcion.getPregunta().getId());
            opcion.setOrden(maxOrden != null ? maxOrden + 1 : 1);
        } else {
            // Verificar que el orden no esté ocupado
            if (opcionRepository.existsByPreguntaIdAndOrden(opcion.getPregunta().getId(), opcion.getOrden())) {
                throw new IllegalArgumentException("Ya existe una opción con el orden " + opcion.getOrden() + " en esta pregunta");
            }
        }
        
        return opcionRepository.save(opcion);
    }
    
    /**
     * Actualizar opción existente
     */
    public Opcion actualizar(Long id, Opcion opcionActualizada) {
        log.debug("Actualizando opción con ID: {}", id);
        
        return opcionRepository.findById(id)
                .map(opcionExistente -> {
                    // Validaciones
                    validarPregunta(opcionActualizada.getPregunta().getId());
                    validarDatos(opcionActualizada);
                    
                    // Verificar cambio de orden
                    if (!opcionExistente.getOrden().equals(opcionActualizada.getOrden())) {
                        if (opcionRepository.existsByPreguntaIdAndOrden(
                                opcionActualizada.getPregunta().getId(), opcionActualizada.getOrden())) {
                            throw new IllegalArgumentException("Ya existe una opción con el orden " + opcionActualizada.getOrden());
                        }
                    }
                    
                    // Actualizar campos
                    opcionExistente.setTexto(opcionActualizada.getTexto());
                    opcionExistente.setEsCorrecta(opcionActualizada.getEsCorrecta());
                    opcionExistente.setOrden(opcionActualizada.getOrden());
                    opcionExistente.setPregunta(opcionActualizada.getPregunta());
                    
                    return opcionRepository.save(opcionExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Opción no encontrada con ID: " + id));
    }
    
    /**
     * Eliminar opción
     */
    public void eliminar(Long id) {
        log.debug("Eliminando opción con ID: {}", id);
        
        Opcion opcion = opcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Opción no encontrada con ID: " + id));
        
        opcionRepository.delete(opcion);
    }
    
    /**
     * Obtener opciones por pregunta
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerPorPregunta(Long preguntaId) {
        log.debug("Obteniendo opciones de la pregunta: {}", preguntaId);
        return opcionRepository.findByPreguntaId(preguntaId);
    }
    
    /**
     * Obtener opciones por pregunta ordenadas
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerPorPreguntaOrdenadas(Long preguntaId) {
        log.debug("Obteniendo opciones de la pregunta {} ordenadas", preguntaId);
        return opcionRepository.findByPreguntaIdOrderByOrdenAsc(preguntaId);
    }
    
    /**
     * Obtener opciones correctas de una pregunta
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerOpcionesCorrectas(Long preguntaId) {
        log.debug("Obteniendo opciones correctas de la pregunta: {}", preguntaId);
        return opcionRepository.findByPreguntaIdAndEsCorrectaTrue(preguntaId);
    }
    
    /**
     * Obtener opciones incorrectas de una pregunta
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerOpcionesIncorrectas(Long preguntaId) {
        log.debug("Obteniendo opciones incorrectas de la pregunta: {}", preguntaId);
        return opcionRepository.findByPreguntaIdAndEsCorrectaFalse(preguntaId);
    }
    
    /**
     * Buscar opciones por texto
     */
    @Transactional(readOnly = true)
    public List<Opcion> buscarPorTexto(String texto) {
        log.debug("Buscando opciones por texto: {}", texto);
        return opcionRepository.findByTextoContainingIgnoreCase(texto);
    }
    
    /**
     * Buscar opciones por palabra clave
     */
    @Transactional(readOnly = true)
    public List<Opcion> buscarPorPalabraClave(String palabra) {
        log.debug("Buscando opciones por palabra clave: {}", palabra);
        return opcionRepository.findOpcionesByPalabraClave(palabra);
    }
    
    /**
     * Obtener opción por pregunta y orden
     */
    @Transactional(readOnly = true)
    public Optional<Opcion> obtenerPorPreguntaYOrden(Long preguntaId, Integer orden) {
        return opcionRepository.findByPreguntaIdAndOrden(preguntaId, orden);
    }
    
    /**
     * Obtener opciones por evaluación
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerPorEvaluacion(Long evaluacionId) {
        log.debug("Obteniendo opciones de la evaluación: {}", evaluacionId);
        return opcionRepository.findOpcionesByEvaluacion(evaluacionId);
    }
    
    /**
     * Obtener opciones correctas por evaluación
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerOpcionesCorrectasPorEvaluacion(Long evaluacionId) {
        log.debug("Obteniendo opciones correctas de la evaluación: {}", evaluacionId);
        return opcionRepository.findOpcionesCorrectasByEvaluacion(evaluacionId);
    }
    
    /**
     * Obtener opciones por curso
     */
    @Transactional(readOnly = true)
    public List<Opcion> obtenerPorCurso(Long cursoId) {
        log.debug("Obteniendo opciones del curso: {}", cursoId);
        return opcionRepository.findOpcionesByCurso(cursoId);
    }
    
    /**
     * Contar opciones por pregunta
     */
    @Transactional(readOnly = true)
    public Integer contarPorPregunta(Long preguntaId) {
        return opcionRepository.countByPreguntaId(preguntaId);
    }
    
    /**
     * Contar opciones correctas por pregunta
     */
    @Transactional(readOnly = true)
    public Integer contarOpcionesCorrectas(Long preguntaId) {
        return opcionRepository.countByPreguntaIdAndEsCorrectaTrue(preguntaId);
    }
    
    /**
     * Contar opciones incorrectas por pregunta
     */
    @Transactional(readOnly = true)
    public Integer contarOpcionesIncorrectas(Long preguntaId) {
        return opcionRepository.countByPreguntaIdAndEsCorrectaFalse(preguntaId);
    }
    
    /**
     * Verificar si una pregunta tiene opciones correctas
     */
    @Transactional(readOnly = true)
    public boolean tieneOpcionesCorrectas(Long preguntaId) {
        return opcionRepository.existsByPreguntaIdAndEsCorrectaTrue(preguntaId);
    }
    
    /**
     * Obtener preguntas con múltiples opciones correctas
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPreguntasConMultiplesOpcionesCorrectas() {
        log.debug("Obteniendo preguntas con múltiples opciones correctas");
        return opcionRepository.findPreguntasConMultiplesOpcionesCorrectas();
    }
    
    /**
     * Obtener preguntas sin opciones correctas
     */
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPreguntasSinOpcionesCorrectas() {
        log.debug("Obteniendo preguntas sin opciones correctas");
        return opcionRepository.findPreguntasSinOpcionesCorrectas();
    }
    
    /**
     * Contar total de opciones correctas en una evaluación
     */
    @Transactional(readOnly = true)
    public Long contarOpcionesCorrectasEnEvaluacion(Long evaluacionId) {
        return opcionRepository.countOpcionesCorrectasEnEvaluacion(evaluacionId);
    }
    
    /**
     * Reordenar opciones de una pregunta
     */
    public void reordenarOpciones(Long preguntaId, List<Long> nuevoOrden) {
        log.debug("Reordenando opciones de la pregunta: {}", preguntaId);
        
        List<Opcion> opciones = opcionRepository.findByPreguntaIdOrderByOrdenAsc(preguntaId);
        
        if (opciones.size() != nuevoOrden.size()) {
            throw new IllegalArgumentException("El número de opciones no coincide con el nuevo orden");
        }
        
        for (int i = 0; i < nuevoOrden.size(); i++) {
            Long opcionId = nuevoOrden.get(i);
            Opcion opcion = opciones.stream()
                    .filter(o -> o.getId().equals(opcionId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Opción no encontrada: " + opcionId));
            
            opcion.setOrden(i + 1);
            opcionRepository.save(opcion);
        }
    }
    
    /**
     * Marcar opción como correcta
     */
    public Opcion marcarComoCorrecta(Long opcionId) {
        log.debug("Marcando opción {} como correcta", opcionId);
        
        return opcionRepository.findById(opcionId)
                .map(opcion -> {
                    opcion.setEsCorrecta(true);
                    return opcionRepository.save(opcion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Opción no encontrada con ID: " + opcionId));
    }
    
    /**
     * Marcar opción como incorrecta
     */
    public Opcion marcarComoIncorrecta(Long opcionId) {
        log.debug("Marcando opción {} como incorrecta", opcionId);
        
        return opcionRepository.findById(opcionId)
                .map(opcion -> {
                    opcion.setEsCorrecta(false);
                    return opcionRepository.save(opcion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Opción no encontrada con ID: " + opcionId));
    }
    
    // Métodos de validación privados
    
    private void validarPregunta(Long preguntaId) {
        if (!preguntaRepository.existsById(preguntaId)) {
            throw new IllegalArgumentException("Pregunta no encontrada con ID: " + preguntaId);
        }
    }
    
    private void validarDatos(Opcion opcion) {
        if (opcion.getTexto() == null || opcion.getTexto().trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la opción es obligatorio");
        }
        
        if (opcion.getEsCorrecta() == null) {
            throw new IllegalArgumentException("Debe especificar si la opción es correcta o no");
        }
        
        if (opcion.getOrden() != null && opcion.getOrden() <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }
    }
}
