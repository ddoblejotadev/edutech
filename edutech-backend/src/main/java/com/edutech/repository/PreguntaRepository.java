package com.edutech.repository;

import com.edutech.model.Pregunta;
import com.edutech.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    
    // Buscar preguntas por evaluación
    List<Pregunta> findByEvaluacion(Evaluacion evaluacion);
    List<Pregunta> findByEvaluacionId(Long evaluacionId);
    
    // Buscar preguntas por texto
    List<Pregunta> findByTextoContainingIgnoreCase(String texto);
    
    // Buscar preguntas por orden en la evaluación
    List<Pregunta> findByEvaluacionOrderByOrdenAsc(Evaluacion evaluacion);
    List<Pregunta> findByEvaluacionIdOrderByOrdenAsc(Long evaluacionId);
    
    // Buscar preguntas por puntaje
    List<Pregunta> findByPuntajeBetween(Double puntajeMin, Double puntajeMax);
    List<Pregunta> findByPuntajeGreaterThanEqual(Double puntajeMinimo);
    
    // Buscar pregunta por evaluación y orden específico
    Pregunta findByEvaluacionAndOrden(Evaluacion evaluacion, Integer orden);
    Pregunta findByEvaluacionIdAndOrden(Long evaluacionId, Integer orden);
    
    // Contar preguntas por evaluación
    Integer countByEvaluacionId(Long evaluacionId);
    
    // Contar opciones de una pregunta
    @Query("SELECT SIZE(p.opciones) FROM Pregunta p WHERE p.id = :preguntaId")
    Integer countOpcionesDePregunta(@Param("preguntaId") Long preguntaId);
    
    // Buscar preguntas con al menos una opción
    @Query("SELECT p FROM Pregunta p WHERE SIZE(p.opciones) > 0")
    List<Pregunta> findPreguntasConOpciones();
    
    // Buscar preguntas sin opciones
    @Query("SELECT p FROM Pregunta p WHERE SIZE(p.opciones) = 0")
    List<Pregunta> findPreguntasSinOpciones();
    
    // Buscar preguntas por número de opciones
    @Query("SELECT p FROM Pregunta p WHERE SIZE(p.opciones) = :numeroOpciones")
    List<Pregunta> findPreguntasByNumeroOpciones(@Param("numeroOpciones") Integer numeroOpciones);
    
    // Buscar preguntas por rango de número de opciones
    @Query("SELECT p FROM Pregunta p WHERE SIZE(p.opciones) BETWEEN :min AND :max")
    List<Pregunta> findPreguntasByNumeroOpcionesRange(@Param("min") Integer min, @Param("max") Integer max);
    
    // Obtener el máximo orden en una evaluación
    @Query("SELECT MAX(p.orden) FROM Pregunta p WHERE p.evaluacion.id = :evaluacionId")
    Integer findMaxOrdenByEvaluacionId(@Param("evaluacionId") Long evaluacionId);
    
    // Obtener el mínimo orden en una evaluación
    @Query("SELECT MIN(p.orden) FROM Pregunta p WHERE p.evaluacion.id = :evaluacionId")
    Integer findMinOrdenByEvaluacionId(@Param("evaluacionId") Long evaluacionId);
    
    // Buscar preguntas de un curso específico (a través de evaluación)
    @Query("SELECT p FROM Pregunta p WHERE p.evaluacion.ejecucion.curso.id = :cursoId")
    List<Pregunta> findPreguntasByCurso(@Param("cursoId") Long cursoId);
    
    // Calcular puntaje total de una evaluación
    @Query("SELECT SUM(p.puntaje) FROM Pregunta p WHERE p.evaluacion.id = :evaluacionId")
    Double calcularPuntajeTotalEvaluacion(@Param("evaluacionId") Long evaluacionId);
    
    // Buscar preguntas ordenadas por puntaje
    List<Pregunta> findByEvaluacionOrderByPuntajeDesc(Evaluacion evaluacion);
    List<Pregunta> findByEvaluacionOrderByPuntajeAsc(Evaluacion evaluacion);
    
    // Verificar si existe una pregunta en un orden específico
    boolean existsByEvaluacionIdAndOrden(Long evaluacionId, Integer orden);
    
    // Buscar preguntas por texto exacto
    Pregunta findByTextoIgnoreCase(String texto);
    
    // Buscar preguntas que contengan palabras clave
    @Query("SELECT p FROM Pregunta p WHERE " +
           "LOWER(p.texto) LIKE LOWER(CONCAT('%', :palabra, '%'))")
    List<Pregunta> findPreguntasByPalabraClave(@Param("palabra") String palabra);
}
