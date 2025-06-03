package com.edutech.service;

import com.edutech.model.TipoPersona;
import com.edutech.repository.TipoPersonaRepository;
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
public class TipoPersonaService {
    
    private final TipoPersonaRepository tipoPersonaRepository;
    
    /**
     * Obtener todos los tipos de persona
     */
    @Transactional(readOnly = true)
    public List<TipoPersona> obtenerTodos() {
        log.debug("Obteniendo todos los tipos de persona");
        return tipoPersonaRepository.findAll();
    }
    
    /**
     * Obtener tipo de persona por ID
     */
    @Transactional(readOnly = true)
    public Optional<TipoPersona> obtenerPorId(Long id) {
        log.debug("Obteniendo tipo de persona con ID: {}", id);
        return tipoPersonaRepository.findById(id);
    }
    
    /**
     * Obtener tipo de persona por nombre
     */
    @Transactional(readOnly = true)
    public Optional<TipoPersona> obtenerPorNombre(String nombre) {
        log.debug("Obteniendo tipo de persona con nombre: {}", nombre);
        return tipoPersonaRepository.findByNombreIgnoreCase(nombre);
    }
    
    /**
     * Crear nuevo tipo de persona
     */
    public TipoPersona crear(TipoPersona tipoPersona) {
        log.debug("Creando nuevo tipo de persona: {}", tipoPersona.getNombre());
        
        // Verificar que no exista ya un tipo con ese nombre
        if (tipoPersonaRepository.existsByNombreIgnoreCase(tipoPersona.getNombre())) {
            throw new IllegalArgumentException("Ya existe un tipo de persona con el nombre: " + tipoPersona.getNombre());
        }
        
        return tipoPersonaRepository.save(tipoPersona);
    }
    
    /**
     * Actualizar tipo de persona existente
     */
    public TipoPersona actualizar(Long id, TipoPersona tipoPersonaActualizado) {
        log.debug("Actualizando tipo de persona con ID: {}", id);
        
        return tipoPersonaRepository.findById(id)
                .map(tipoPersonaExistente -> {
                    // Verificar que el nuevo nombre no esté en uso por otro tipo
                    if (!tipoPersonaExistente.getNombre().equalsIgnoreCase(tipoPersonaActualizado.getNombre()) &&
                        tipoPersonaRepository.existsByNombreIgnoreCase(tipoPersonaActualizado.getNombre())) {
                        throw new IllegalArgumentException("Ya existe un tipo de persona con el nombre: " + tipoPersonaActualizado.getNombre());
                    }
                    
                    tipoPersonaExistente.setNombre(tipoPersonaActualizado.getNombre());
                    tipoPersonaExistente.setDescripcion(tipoPersonaActualizado.getDescripcion());
                    
                    return tipoPersonaRepository.save(tipoPersonaExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Tipo de persona no encontrado con ID: " + id));
    }
    
    /**
     * Eliminar tipo de persona
     */
    public void eliminar(Long id) {
        log.debug("Eliminando tipo de persona con ID: {}", id);
        
        TipoPersona tipoPersona = tipoPersonaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de persona no encontrado con ID: " + id));
        
        // Verificar que no tenga personas asociadas
        if (tipoPersonaRepository.countPersonasAsociadas(id) > 0) {
            throw new IllegalStateException("No se puede eliminar el tipo de persona porque tiene personas asociadas");
        }
        
        tipoPersonaRepository.delete(tipoPersona);
    }
    
    /**
     * Verificar si existe tipo de persona por nombre
     */
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return tipoPersonaRepository.existsByNombreIgnoreCase(nombre);
    }
    
    /**
     * Obtener tipos de persona ordenados por nombre
     */
    @Transactional(readOnly = true)
    public List<TipoPersona> obtenerOrdenadosPorNombre() {
        log.debug("Obteniendo tipos de persona ordenados por nombre");
        return tipoPersonaRepository.findAllByOrderByNombreAsc();
    }
    
    /**
     * Contar personas asociadas a un tipo
     */
    @Transactional(readOnly = true)
    public Long contarPersonasAsociadas(Long tipoPersonaId) {
        return tipoPersonaRepository.countPersonasAsociadas(tipoPersonaId);
    }
    
    /**
     * Obtener tipos de persona con personas asociadas
     */
    @Transactional(readOnly = true)
    public List<TipoPersona> obtenerConPersonasAsociadas() {
        return tipoPersonaRepository.findTiposConPersonasAsociadas();
    }
    
    /**
     * Buscar tipos de persona por nombre parcial
     */
    @Transactional(readOnly = true)
    public List<TipoPersona> buscarPorNombre(String nombreParcial) {
        log.debug("Buscando tipos de persona que contengan: {}", nombreParcial);
        return tipoPersonaRepository.findByNombreContainingIgnoreCase(nombreParcial);
    }
}
