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
                    // Verificar que no exista ya otro tipo con ese nombre
                    if (!tipoPersonaExistente.getNombre().equalsIgnoreCase(tipoPersonaActualizado.getNombre()) &&
                        tipoPersonaRepository.existsByNombreIgnoreCase(tipoPersonaActualizado.getNombre())) {
                        throw new IllegalArgumentException("Ya existe un tipo de persona con el nombre: " + tipoPersonaActualizado.getNombre());
                    }
                    
                    tipoPersonaExistente.setNombre(tipoPersonaActualizado.getNombre());
                    tipoPersonaExistente.setDescripcion(tipoPersonaActualizado.getDescripcion());
                    tipoPersonaExistente.setActivo(tipoPersonaActualizado.getActivo());
                    
                    return tipoPersonaRepository.save(tipoPersonaExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Tipo de persona no encontrado con ID: " + id));
    }
    
    /**
     * Eliminar tipo de persona
     */
    public void eliminar(Long id) {
        log.debug("Eliminando tipo de persona con ID: {}", id);
        
        if (!tipoPersonaRepository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de persona no encontrado con ID: " + id);
        }
        
        // Verificar que no esté siendo usado por ninguna persona
        Long personasConTipo = tipoPersonaRepository.countPersonasByTipoPersonaId(id);
        if (personasConTipo > 0) {
            throw new IllegalStateException("No se puede eliminar el tipo de persona porque está siendo usado por " + personasConTipo + " personas");
        }
        
        tipoPersonaRepository.deleteById(id);
    }
    
    /**
     * Activar tipo de persona
     */
    public void activar(Long id) {
        log.debug("Activando tipo de persona con ID: {}", id);
        tipoPersonaRepository.findById(id).ifPresent(tipoPersona -> {
            tipoPersona.setActivo(true);
            tipoPersonaRepository.save(tipoPersona);
        });
    }
    
    /**
     * Desactivar tipo de persona
     */
    public void desactivar(Long id) {
        log.debug("Desactivando tipo de persona con ID: {}", id);
        tipoPersonaRepository.findById(id).ifPresent(tipoPersona -> {
            tipoPersona.setActivo(false);
            tipoPersonaRepository.save(tipoPersona);
        });
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
     * Verificar si el tipo de persona está siendo usado
     */
    @Transactional(readOnly = true)
    public boolean tienePersonasAsociadas(Long id) {
        log.debug("Verificando si el tipo de persona {} tiene personas asociadas", id);
        Long count = tipoPersonaRepository.countPersonasByTipoPersonaId(id);
        return count > 0;
    }
    
    /**
     * Contar personas asociadas a un tipo
     */
    @Transactional(readOnly = true)
    public Long contarPersonasAsociadas(Long id) {
        log.debug("Contando personas asociadas al tipo de persona {}", id);
        return tipoPersonaRepository.countPersonasByTipoPersonaId(id);
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
