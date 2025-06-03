package com.edutech.service;

import com.edutech.model.Curso;
import com.edutech.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CursoService {
    
    private final CursoRepository cursoRepository;
    
    /**
     * Obtener todos los cursos
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerTodos() {
        log.debug("Obteniendo todos los cursos");
        return cursoRepository.findAll();
    }
    
    /**
     * Obtener curso por ID
     */
    @Transactional(readOnly = true)
    public Optional<Curso> obtenerPorId(Long id) {
        log.debug("Obteniendo curso con ID: {}", id);
        return cursoRepository.findById(id);
    }
    
    /**
     * Crear nuevo curso
     */
    public Curso crear(Curso curso) {
        log.debug("Creando nuevo curso: {}", curso.getNombre());
        
        // Validar que no exista un curso con el mismo nombre
        if (cursoRepository.existsByNombreIgnoreCase(curso.getNombre())) {
            throw new IllegalArgumentException("Ya existe un curso con el nombre: " + curso.getNombre());
        }
        
        // Validar prerequisitos
        validarPrerequisitos(curso.getPrerequisitos());
        
        return cursoRepository.save(curso);
    }
    
    /**
     * Actualizar curso existente
     */
    public Curso actualizar(Long id, Curso cursoActualizado) {
        log.debug("Actualizando curso con ID: {}", id);
        
        return cursoRepository.findById(id)
                .map(cursoExistente -> {
                    // Verificar nombre único si cambió
                    if (!cursoExistente.getNombre().equalsIgnoreCase(cursoActualizado.getNombre()) &&
                        cursoRepository.existsByNombreIgnoreCase(cursoActualizado.getNombre())) {
                        throw new IllegalArgumentException("Ya existe un curso con el nombre: " + cursoActualizado.getNombre());
                    }
                    
                    // Validar prerequisitos
                    validarPrerequisitos(cursoActualizado.getPrerequisitos());
                    
                    // Actualizar campos
                    cursoExistente.setNombre(cursoActualizado.getNombre());
                    cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
                    cursoExistente.setDuracionHoras(cursoActualizado.getDuracionHoras());
                    cursoExistente.setPrerequisitos(cursoActualizado.getPrerequisitos());
                    
                    return cursoRepository.save(cursoExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + id));
    }
    
    /**
     * Eliminar curso
     */
    public void eliminar(Long id) {
        log.debug("Eliminando curso con ID: {}", id);
        
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + id));
        
        // Verificar que no sea prerequisito de otros cursos
        List<Curso> cursosConEstePrerequisito = cursoRepository.findCursosConPrerequisito(id);
        if (!cursosConEstePrerequisito.isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el curso porque es prerequisito de otros cursos");
        }
        
        cursoRepository.delete(curso);
    }
    
    /**
     * Buscar cursos por nombre
     */
    @Transactional(readOnly = true)
    public List<Curso> buscarPorNombre(String nombre) {
        log.debug("Buscando cursos por nombre: {}", nombre);
        return cursoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Buscar cursos por descripción
     */
    @Transactional(readOnly = true)
    public List<Curso> buscarPorDescripcion(String descripcion) {
        log.debug("Buscando cursos por descripción: {}", descripcion);
        return cursoRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }
    
    /**
     * Obtener cursos por rango de duración
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerPorRangoDuracion(Integer duracionMin, Integer duracionMax) {
        log.debug("Obteniendo cursos con duración entre {} y {} horas", duracionMin, duracionMax);
        return cursoRepository.findByDuracionHorasBetween(duracionMin, duracionMax);
    }
    
    /**
     * Obtener cursos sin prerequisitos
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerSinPrerequisitos() {
        log.debug("Obteniendo cursos sin prerequisitos");
        return cursoRepository.findCursosSinPrerequisitos();
    }
    
    /**
     * Obtener cursos que requieren un prerequisito específico
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerConPrerequisito(Long prerequisitoId) {
        log.debug("Obteniendo cursos que requieren el prerequisito: {}", prerequisitoId);
        return cursoRepository.findCursosConPrerequisito(prerequisitoId);
    }
    
    /**
     * Obtener cursos disponibles después de completar uno específico
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerDisponiblesDespuesDe(Long cursoCompletadoId) {
        log.debug("Obteniendo cursos disponibles después de completar: {}", cursoCompletadoId);
        
        return cursoRepository.findById(cursoCompletadoId)
                .map(curso -> cursoRepository.findCursosDisponiblesDespuesDe(curso))
                .orElse(List.of());
    }
    
    /**
     * Obtener cursos ordenados por duración
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerOrdenadosPorDuracion(boolean ascendente) {
        log.debug("Obteniendo cursos ordenados por duración: {}", ascendente ? "ascendente" : "descendente");
        return ascendente ? cursoRepository.findAllByOrderByDuracionHorasAsc() : cursoRepository.findAllByOrderByDuracionHorasDesc();
    }
    
    /**
     * Obtener cursos ordenados por nombre
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerOrdenadosPorNombre() {
        log.debug("Obteniendo cursos ordenados por nombre");
        return cursoRepository.findAllByOrderByNombreAsc();
    }
    
    /**
     * Contar cursos por rango de duración
     */
    @Transactional(readOnly = true)
    public Long contarPorRangoDuracion(Integer duracionMin, Integer duracionMax) {
        return cursoRepository.countCursosByDuracionRange(duracionMin, duracionMax);
    }
    
    /**
     * Verificar si existe curso por nombre
     */
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return cursoRepository.existsByNombreIgnoreCase(nombre);
    }
    
    /**
     * Agregar prerequisito a un curso
     */
    public Curso agregarPrerequisito(Long cursoId, Long prerequisitoId) {
        log.debug("Agregando prerequisito {} al curso {}", prerequisitoId, cursoId);
        
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + cursoId));
        
        Curso prerequisito = cursoRepository.findById(prerequisitoId)
                .orElseThrow(() -> new IllegalArgumentException("Prerequisito no encontrado con ID: " + prerequisitoId));
        
        // Verificar que no sea el mismo curso
        if (cursoId.equals(prerequisitoId)) {
            throw new IllegalArgumentException("Un curso no puede ser prerequisito de sí mismo");
        }
        
        // Verificar que no cause dependencia circular
        if (tieneDependendiaCircular(prerequisito, curso)) {
            throw new IllegalArgumentException("No se puede agregar el prerequisito porque causaría una dependencia circular");
        }
        
        curso.getPrerequisitos().add(prerequisito);
        return cursoRepository.save(curso);
    }
    
    /**
     * Remover prerequisito de un curso
     */
    public Curso removerPrerequisito(Long cursoId, Long prerequisitoId) {
        log.debug("Removiendo prerequisito {} del curso {}", prerequisitoId, cursoId);
        
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + cursoId));
        
        Curso prerequisito = cursoRepository.findById(prerequisitoId)
                .orElseThrow(() -> new IllegalArgumentException("Prerequisito no encontrado con ID: " + prerequisitoId));
        
        curso.getPrerequisitos().remove(prerequisito);
        return cursoRepository.save(curso);
    }
    
    // Métodos de validación privados
    
    private void validarPrerequisitos(Set<Curso> prerequisitos) {
        if (prerequisitos != null) {
            for (Curso prerequisito : prerequisitos) {
                if (!cursoRepository.existsById(prerequisito.getId())) {
                    throw new IllegalArgumentException("Prerequisito no encontrado con ID: " + prerequisito.getId());
                }
            }
        }
    }
    
    private boolean tieneDependendiaCircular(Curso prerequisito, Curso curso) {
        // Verificar si el curso ya es prerequisito del prerequisito (directa o indirectamente)
        return verificarDependenciaRecursiva(prerequisito, curso, Set.of());
    }
    
    private boolean verificarDependenciaRecursiva(Curso cursoActual, Curso cursoObjetivo, Set<Long> visitados) {
        if (visitados.contains(cursoActual.getId())) {
            return true; // Ciclo detectado
        }
        
        if (cursoActual.getId().equals(cursoObjetivo.getId())) {
            return true; // Dependencia circular encontrada
        }
        
        visitados.add(cursoActual.getId());
        
        for (Curso prerequisito : cursoActual.getPrerequisitos()) {
            if (verificarDependenciaRecursiva(prerequisito, cursoObjetivo, visitados)) {
                return true;
            }
        }
        
        visitados.remove(cursoActual.getId());
        return false;
    }
}
