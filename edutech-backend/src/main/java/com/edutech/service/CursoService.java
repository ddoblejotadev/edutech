package com.edutech.service;

import com.edutech.model.Curso;
import com.edutech.repository.CursoRepository;
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
        
        return cursoRepository.save(curso);
    }
    
    /**
     * Actualizar curso existente
     */
    public Optional<Curso> actualizar(Long id, Curso cursoActualizado) {
        log.debug("Actualizando curso con ID: {}", id);
        
        return cursoRepository.findById(id)
                .map(cursoExistente -> {
                    cursoExistente.setNombre(cursoActualizado.getNombre());
                    cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
                    cursoExistente.setHorasTeoricas(cursoActualizado.getHorasTeoricas());
                    cursoExistente.setHorasPracticas(cursoActualizado.getHorasPracticas());
                    cursoExistente.setActivo(cursoActualizado.getActivo());
                    return cursoRepository.save(cursoExistente);
                });
    }
    
    /**
     * Eliminar curso
     */
    public boolean eliminar(Long id) {
        log.debug("Eliminando curso con ID: {}", id);
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
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
     * Buscar cursos por nombre
     */
    @Transactional(readOnly = true)
    public List<Curso> buscarPorNombre(String nombre) {
        log.debug("Buscando cursos por nombre: {}", nombre);
        return cursoRepository.findByNombreContainingIgnoreCase(nombre);
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
        return cursoRepository.findByPrerrequisitoIsNull();
    }
    
    /**
     * Obtener cursos con un prerequisito específico
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerConPrerequisito(String prerequisitoCodigo) {
        log.debug("Obteniendo cursos con prerequisito: {}", prerequisitoCodigo);
        return cursoRepository.findByPrerrequisitoId(prerequisitoCodigo);
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
     * Obtener cursos ordenados por duración
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerOrdenadosPorDuracion(boolean ascendente) {
        log.debug("Obteniendo cursos ordenados por duración: {}", ascendente ? "ascendente" : "descendente");
        return ascendente ? cursoRepository.findAllByOrderByDuracionHorasAsc() : cursoRepository.findAllByOrderByDuracionHorasDesc();
    }
    
    /**
     * Verificar si existe curso por nombre
     */
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return cursoRepository.existsByNombreIgnoreCase(nombre);
    }
}
