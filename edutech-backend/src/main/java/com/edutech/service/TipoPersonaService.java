package com.edutech.service;

//Importaciones del model y repository
import com.edutech.model.TipoPersona;
import com.edutech.repository.TipoPersonaRepository;

//Importacion para dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Importaciones Java
import java.util.List;
import java.util.Optional;

@Service
public class TipoPersonaService {
    
    @Autowired
    private TipoPersonaRepository tipoPersonaRepository;
    
    /**
     * Obtener todos los tipos de persona
     */
    public List<TipoPersona> obtenerTodos() {
        return tipoPersonaRepository.findAll();
    }
    
    /**
     * Obtener tipo de persona por ID
     */
    public Optional<TipoPersona> obtenerPorId(Long id) {
        return tipoPersonaRepository.findById(id);
    }
    
    /**
     * Obtener tipo de persona por nombre
     */
    public Optional<TipoPersona> obtenerPorNombre(String nombre) {
        return tipoPersonaRepository.findByNombreIgnoreCase(nombre);
    }
    
    /**
     * Crear nuevo tipo de persona
     */
    public TipoPersona crear(TipoPersona tipoPersona) {
        
        // Validaciones
        validarTipoPersona(tipoPersona);
        
        // Verificar que no exista otro tipo con el mismo nombre
        if (tipoPersonaRepository.existsByNombreIgnoreCase(tipoPersona.getNombre())) {
            throw new IllegalArgumentException("Ya existe un tipo de persona con el nombre: " + tipoPersona.getNombre());
        }
        
        return tipoPersonaRepository.save(tipoPersona);
    }
    
    /**
     * Actualizar tipo de persona existente
     */
    public Optional<TipoPersona> actualizar(Long id, TipoPersona tipoPersonaActualizado) {
        
        return tipoPersonaRepository.findById(id)
                .map(tipoExistente -> {
                    // Validaciones
                    validarTipoPersona(tipoPersonaActualizado);
                    
                    // Verificar que no exista otro tipo con el mismo nombre (excluyendo el actual)
                    Optional<TipoPersona> tipoConMismoNombre = tipoPersonaRepository.findByNombreIgnoreCase(tipoPersonaActualizado.getNombre());
                    if (tipoConMismoNombre.isPresent() && !tipoConMismoNombre.get().getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe un tipo de persona con el nombre: " + tipoPersonaActualizado.getNombre());
                    }
                    
                    // Actualizar campos
                    tipoExistente.setNombre(tipoPersonaActualizado.getNombre());
                    tipoExistente.setDescripcion(tipoPersonaActualizado.getDescripcion());
                    tipoExistente.setActivo(tipoPersonaActualizado.getActivo());
                    
                    return tipoPersonaRepository.save(tipoExistente);
                });
    }
    
    /**
     * Eliminar tipo de persona
     */
    public void eliminar(Long id) {
        
        TipoPersona tipoPersona = tipoPersonaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de persona no encontrado con ID: " + id));
        
        // Verificar que no tenga personas asociadas
        Long personasAsociadas = tipoPersonaRepository.countPersonasByTipoPersonaId(id);
        if (personasAsociadas > 0) {
            throw new IllegalStateException("No se puede eliminar el tipo de persona porque tiene " + personasAsociadas + " personas asociadas");
        }
        
        tipoPersonaRepository.delete(tipoPersona);
    }
    
    /**
     * Obtener tipos de persona activos
     */
    public List<TipoPersona> obtenerActivos() {
        return tipoPersonaRepository.findByActivoTrue();
    }
    
    /**
     * Obtener tipos de persona ordenados por nombre
     */
    public List<TipoPersona> obtenerOrdenadosPorNombre() {
        return tipoPersonaRepository.findAllByOrderByNombreAsc();
    }
    
    /**
     * Obtener tipos con personas asociadas
     */
    public List<TipoPersona> obtenerConPersonasAsociadas() {
        return tipoPersonaRepository.findTiposConPersonasAsociadas();
    }
    
    /**
     * Buscar tipos por nombre
     */
    public List<TipoPersona> buscarPorNombre(String nombre) {
        return tipoPersonaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Contar personas por tipo
     */
    public Long contarPersonasPorTipo(Long tipoPersonaId) {
        return tipoPersonaRepository.countPersonasByTipoPersonaId(tipoPersonaId);
    }
    
    /**
     * Verificar si existe tipo por nombre
     */
    public boolean existePorNombre(String nombre) {
        return tipoPersonaRepository.existsByNombreIgnoreCase(nombre);
    }
    
    /**
     * Activar tipo de persona
     */
    public void activar(Long id) {
        tipoPersonaRepository.findById(id).ifPresent(tipo -> {
            tipo.setActivo(true);
            tipoPersonaRepository.save(tipo);
        });
    }
    
    /**
     * Desactivar tipo de persona
     */
    public void desactivar(Long id) {
        tipoPersonaRepository.findById(id).ifPresent(tipo -> {
            tipo.setActivo(false);
            tipoPersonaRepository.save(tipo);
        });
    }
    
    // Métodos de validación privados
    
    private void validarTipoPersona(TipoPersona tipoPersona) {
        if (tipoPersona.getNombre() == null || tipoPersona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de persona es obligatorio");
        }
        
        if (tipoPersona.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre del tipo de persona no puede exceder 50 caracteres");
        }
        
        if (tipoPersona.getDescripcion() != null && tipoPersona.getDescripcion().length() > 200) {
            throw new IllegalArgumentException("La descripción no puede exceder 200 caracteres");
        }
    }
}
