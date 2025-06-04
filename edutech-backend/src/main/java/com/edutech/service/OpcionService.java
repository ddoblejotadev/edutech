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
        
        // Validaciones
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
