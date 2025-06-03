package com.edutech.repository;

import com.edutech.model.Curso;
import com.edutech.model.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    // Buscar cursos por nombre (case insensitive)
    List<Curso> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar cursos por descripción
    List<Curso> findByDescripcionContainingIgnoreCase(String descripcion);
    
    // Buscar cursos por duración mínima
    List<Curso> findByDuracionHorasGreaterThanEqual(Integer duracionMinima);
    
    // Buscar cursos por duración en un rango
    List<Curso> findByDuracionHorasBetween(Integer duracionMin, Integer duracionMax);
    
    // Buscar cursos sin prerequisitos
    @Query("SELECT c FROM Curso c WHERE SIZE(c.prerequisitos) = 0")
    List<Curso> findCursosSinPrerequisitos();
    
    // Buscar cursos con prerequisitos específicos
    @Query("SELECT c FROM Curso c JOIN c.prerequisitos p WHERE p.id = :prerequisitoId")
    List<Curso> findCursosConPrerequisito(@Param("prerequisitoId") Long prerequisitoId);
    
    // Buscar cursos que pueden ser tomados después de completar un curso específico
    @Query("SELECT c FROM Curso c WHERE c NOT MEMBER OF :cursoCompletado.prerequisitos")
    List<Curso> findCursosDisponiblesDespuesDe(@Param("cursoCompletado") Curso cursoCompletado);
    
    // Contar cursos por rango de duración
    @Query("SELECT COUNT(c) FROM Curso c WHERE c.duracionHoras BETWEEN :min AND :max")
    Long countCursosByDuracionRange(@Param("min") Integer min, @Param("max") Integer max);
    
    // Buscar cursos ordenados por duración
    List<Curso> findAllByOrderByDuracionHorasAsc();
    List<Curso> findAllByOrderByDuracionHorasDesc();
    
    // Buscar cursos ordenados por nombre
    List<Curso> findAllByOrderByNombreAsc();
    
    // Verificar si un curso existe por nombre
    boolean existsByNombreIgnoreCase(String nombre);
}
