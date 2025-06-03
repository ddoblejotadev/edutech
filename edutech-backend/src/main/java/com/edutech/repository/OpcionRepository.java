package com.edutech.repository;

import com.edutech.model.Opcion;
import com.edutech.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Long> {
    
    // Buscar opciones por pregunta
    List<Opcion> findByPregunta(Pregunta pregunta);
    List<Opcion> findByPreguntaId(Long preguntaId);
    
    // Buscar opciones ordenadas por orden
    List<Opcion> findByPreguntaOrderByOrdenAsc(Pregunta pregunta);
    List<Opcion> findByPreguntaIdOrderByOrdenAsc(Long preguntaId);
    
    // Buscar opciones por texto
    List<Opcion> findByTextoContainingIgnoreCase(String texto);
    
    // Buscar opciones correctas
    List<Opcion> findByEsCorrectaTrue();
    List<Opcion> findByEsCorrectaFalse();
    
    // Buscar opciones correctas de una pregunta
    List<Opcion> findByPreguntaAndEsCorrectaTrue(Pregunta pregunta);
    List<Opcion> findByPreguntaIdAndEsCorrectaTrue(Long preguntaId);
    
    // Buscar opciones incorrectas de una pregunta
    List<Opcion> findByPreguntaAndEsCorrectaFalse(Pregunta pregunta);
    List<Opcion> findByPreguntaIdAndEsCorrectaFalse(Long preguntaId);
    
    // Buscar opción por pregunta y orden específico
    Optional<Opcion> findByPreguntaAndOrden(Pregunta pregunta, Integer orden);
    Optional<Opcion> findByPreguntaIdAndOrden(Long preguntaId, Integer orden);
    
    // Contar opciones por pregunta
    Integer countByPreguntaId(Long preguntaId);
    
    // Contar opciones correctas por pregunta
    Integer countByPreguntaIdAndEsCorrectaTrue(Long preguntaId);
    
    // Contar opciones incorrectas por pregunta
    Integer countByPreguntaIdAndEsCorrectaFalse(Long preguntaId);
    
    // Obtener el máximo orden en una pregunta
    @Query("SELECT MAX(o.orden) FROM Opcion o WHERE o.pregunta.id = :preguntaId")
    Integer findMaxOrdenByPreguntaId(@Param("preguntaId") Long preguntaId);
    
    // Obtener el mínimo orden en una pregunta
    @Query("SELECT MIN(o.orden) FROM Opcion o WHERE o.pregunta.id = :preguntaId")
    Integer findMinOrdenByPreguntaId(@Param("preguntaId") Long preguntaId);
    
    // Verificar si existe una opción en un orden específico
    boolean existsByPreguntaIdAndOrden(Long preguntaId, Integer orden);
    
    // Verificar si una pregunta tiene opciones correctas
    boolean existsByPreguntaIdAndEsCorrectaTrue(Long preguntaId);
    
    // Buscar opciones de una evaluación específica
    @Query("SELECT o FROM Opcion o WHERE o.pregunta.evaluacion.id = :evaluacionId")
    List<Opcion> findOpcionesByEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    // Buscar opciones correctas de una evaluación
    @Query("SELECT o FROM Opcion o WHERE o.pregunta.evaluacion.id = :evaluacionId AND o.esCorrecta = true")
    List<Opcion> findOpcionesCorrectasByEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    // Buscar opciones de un curso específico
    @Query("SELECT o FROM Opcion o WHERE o.pregunta.evaluacion.ejecucion.curso.id = :cursoId")
    List<Opcion> findOpcionesByCurso(@Param("cursoId") Long cursoId);
    
    // Buscar opciones por texto exacto
    Optional<Opcion> findByTextoIgnoreCase(String texto);
    
    // Buscar opciones que contengan palabras clave
    @Query("SELECT o FROM Opcion o WHERE " +
           "LOWER(o.texto) LIKE LOWER(CONCAT('%', :palabra, '%'))")
    List<Opcion> findOpcionesByPalabraClave(@Param("palabra") String palabra);
    
    // Buscar preguntas que tienen múltiples opciones correctas
    @Query("SELECT DISTINCT o.pregunta FROM Opcion o WHERE o.esCorrecta = true " +
           "GROUP BY o.pregunta HAVING COUNT(o) > 1")
    List<Pregunta> findPreguntasConMultiplesOpcionesCorrectas();
    
    // Buscar preguntas que no tienen opciones correctas
    @Query("SELECT p FROM Pregunta p WHERE p.id NOT IN " +
           "(SELECT DISTINCT o.pregunta.id FROM Opcion o WHERE o.esCorrecta = true)")
    List<Pregunta> findPreguntasSinOpcionesCorrectas();
    
    // Contar total de opciones correctas en una evaluación
    @Query("SELECT COUNT(o) FROM Opcion o WHERE o.pregunta.evaluacion.id = :evaluacionId AND o.esCorrecta = true")
    Long countOpcionesCorrectasEnEvaluacion(@Param("evaluacionId") Long evaluacionId);
}
