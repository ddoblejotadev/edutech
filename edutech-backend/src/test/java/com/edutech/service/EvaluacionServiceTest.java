package com.edutech.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.time.LocalDateTime;

import com.edutech.model.Evaluacion;
import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.repository.EvaluacionRepository;
import com.edutech.repository.EjecucionRepository;

@SpringBootTest
class EvaluacionServiceTest {

    @MockBean
    private EvaluacionRepository evaluacionRepository;

    @MockBean
    private EjecucionRepository ejecucionRepository;

    @Autowired
    private EvaluacionService evaluacionService;

    @Test
    void testObtenerTodas() {
        // Arrange
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Avanzadas");

        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setTitulo("Examen Parcial");
        evaluacion.setEjecucion(ejecucion);
        evaluacion.setTipo("PARCIAL");
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findAll()).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerTodas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Examen Parcial", result.get(0).getTitulo());
        verify(evaluacionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_EvaluacionExiste() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setTitulo("Examen Parcial");
        evaluacion.setTipo("PARCIAL");
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act
        Optional<Evaluacion> result = evaluacionService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Examen Parcial", result.get().getTitulo());
        assertEquals("PARCIAL", result.get().getTipo());
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_EvaluacionNoExiste() {
        // Arrange
        when(evaluacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Evaluacion> result = evaluacionService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(evaluacionRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear_EvaluacionValida() {
        // Arrange
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTitulo("Examen Parcial");
        evaluacion.setEjecucion(ejecucion);
        evaluacion.setFechaDisponible(LocalDateTime.now().plusDays(1));
        evaluacion.setFechaLimite(LocalDateTime.now().plusDays(2));
        evaluacion.setPuntajeTotal(100.0);
        
        when(ejecucionRepository.existsById(1L)).thenReturn(true);
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacion);

        // Act
        Evaluacion result = evaluacionService.crear(evaluacion);

        // Assert
        assertNotNull(result);
        assertEquals("Examen Parcial", result.getTitulo());
        verify(ejecucionRepository, times(1)).existsById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacion);
    }

    @Test
    void testCrear_EjecucionNoExiste() {
        // Arrange
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEjecucion(ejecucion);
        
        when(ejecucionRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> evaluacionService.crear(evaluacion)
        );
        
        assertEquals("Ejecución no encontrada con ID: 1", exception.getMessage());
        verify(ejecucionRepository, times(1)).existsById(1L);
        verify(evaluacionRepository, never()).save(any());
    }

    @Test
    void testCrear_FechasInvalidas() {
        // Arrange
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEjecucion(ejecucion);
        evaluacion.setFechaDisponible(LocalDateTime.now().plusDays(2));
        evaluacion.setFechaLimite(LocalDateTime.now().plusDays(1)); // Fecha fin antes que inicio
        evaluacion.setPuntajeTotal(100.0);
        
        when(ejecucionRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> evaluacionService.crear(evaluacion)
        );
        
        assertEquals("La fecha límite no puede ser anterior a la fecha de disponibilidad", exception.getMessage());
        verify(evaluacionRepository, never()).save(any());
    }

    @Test
    void testCrear_PuntajeInvalido() {
        // Arrange
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEjecucion(ejecucion);
        evaluacion.setFechaDisponible(LocalDateTime.now().plusDays(1));
        evaluacion.setFechaLimite(LocalDateTime.now().plusDays(2));
        evaluacion.setPuntajeTotal(-10.0);
        
        when(ejecucionRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> evaluacionService.crear(evaluacion)
        );
        
        assertEquals("El puntaje total debe ser mayor a 0", exception.getMessage());
        verify(evaluacionRepository, never()).save(any());
    }

    @Test
    void testActualizar_EvaluacionExiste() {
        // Arrange
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        Evaluacion evaluacionExistente = new Evaluacion();
        evaluacionExistente.setId(1L);
        evaluacionExistente.setTitulo("Examen Parcial");
        evaluacionExistente.setEjecucion(ejecucion);
        
        Evaluacion evaluacionActualizada = new Evaluacion();
        evaluacionActualizada.setTitulo("Examen Final");
        evaluacionActualizada.setEjecucion(ejecucion);
        evaluacionActualizada.setFechaDisponible(LocalDateTime.now().plusDays(5));
        evaluacionActualizada.setFechaLimite(LocalDateTime.now().plusDays(6));
        evaluacionActualizada.setPuntajeTotal(100.0);
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacionExistente));
        when(ejecucionRepository.existsById(1L)).thenReturn(true);
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacionExistente);

        // Act
        Evaluacion result = evaluacionService.actualizar(1L, evaluacionActualizada);

        // Assert
        assertNotNull(result);
        assertEquals("Examen Final", evaluacionExistente.getTitulo());
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacionExistente);
    }

    @Test
    void testEliminar_EvaluacionExiste() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setFechaDisponible(LocalDateTime.now().plusDays(1)); // No ha comenzado
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act
        assertDoesNotThrow(() -> evaluacionService.eliminar(1L));

        // Assert
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).delete(evaluacion);
    }

    @Test
    void testEliminar_EvaluacionNoExiste() {
        // Arrange
        when(evaluacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> evaluacionService.eliminar(999L)
        );
        
        assertEquals("Evaluación no encontrada con ID: 999", exception.getMessage());
        verify(evaluacionRepository, times(1)).findById(999L);
        verify(evaluacionRepository, never()).delete(any());
    }

    @Test
    void testEliminar_EvaluacionYaComenzada() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setFechaDisponible(LocalDateTime.now().minusDays(1)); // Ya comenzó
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> evaluacionService.eliminar(1L)
        );
        
        assertEquals("No se puede eliminar una evaluación que ya comenzó", exception.getMessage());
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, never()).delete(any());
    }

    @Test
    void testObtenerPorEjecucion() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTitulo("Examen Parcial");
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByEjecucionId(1L)).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerPorEjecucion(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByEjecucionId(1L);
    }

    @Test
    void testBuscarPorTitulo() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTitulo("Examen Parcial");
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByTituloContainingIgnoreCase("Parcial")).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.buscarPorTitulo("Parcial");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByTituloContainingIgnoreCase("Parcial");
    }

    @Test
    void testObtenerActivas() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setActivo(true);
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByActivoTrue()).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerActivas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByActivoTrue();
    }

    @Test
    void testObtenerPublicadas() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setPublicada(true);
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByPublicadaTrue()).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerPublicadas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByPublicadaTrue();
    }

    @Test
    void testObtenerDisponibles() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setActivo(true);
        evaluacion.setPublicada(true);
        evaluacion.setFechaDisponible(LocalDateTime.now().minusHours(1));
        evaluacion.setFechaLimite(LocalDateTime.now().plusHours(1));
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByActivoTrueAndPublicadaTrueAndFechaInicioBeforeAndFechaFinAfter(any(LocalDateTime.class)))
            .thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerDisponibles();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1))
            .findByActivoTrueAndPublicadaTrueAndFechaInicioBeforeAndFechaFinAfter(any(LocalDateTime.class));
    }

    @Test
    void testObtenerFuturas() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setFechaDisponible(LocalDateTime.now().plusDays(1));
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByFechaInicioAfter(any(LocalDateTime.class))).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerFuturas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByFechaInicioAfter(any(LocalDateTime.class));
    }

    @Test
    void testObtenerVencidas() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setFechaLimite(LocalDateTime.now().minusDays(1));
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByFechaFinBefore(any(LocalDateTime.class))).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerVencidas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByFechaFinBefore(any(LocalDateTime.class));
    }

    @Test
    void testObtenerPorRangoFechasInicio() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setFechaInicio(LocalDateTime.now().plusDays(3));
        
        LocalDateTime fechaInicio = LocalDateTime.now();
        LocalDateTime fechaFin = LocalDateTime.now().plusDays(7);
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByFechaInicioBetween(fechaInicio, fechaFin)).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerPorRangoFechasInicio(fechaInicio, fechaFin);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByFechaInicioBetween(fechaInicio, fechaFin);
    }

    @Test
    void testObtenerPorRangoPuntaje() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setPuntajeTotal(75.0);
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByPuntajeTotalBetween(50.0, 100.0)).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerPorRangoPuntaje(50.0, 100.0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByPuntajeTotalBetween(50.0, 100.0);
    }

    @Test
    void testEstaDisponible_Disponible() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setActivo(true);
        evaluacion.setPublicada(true);
        evaluacion.setFechaDisponible(LocalDateTime.now().minusHours(1));
        evaluacion.setFechaLimite(LocalDateTime.now().plusHours(1));
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act
        boolean result = evaluacionService.estaDisponible(1L);

        // Assert
        assertTrue(result);
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testEstaDisponible_NoDisponible() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setActivo(false);
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act
        boolean result = evaluacionService.estaDisponible(1L);

        // Assert
        assertFalse(result);
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testEstaVencida_Vencida() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setFechaLimite(LocalDateTime.now().minusDays(1));
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act
        boolean result = evaluacionService.estaVencida(1L);

        // Assert
        assertTrue(result);
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testEstaVencida_NoVencida() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setFechaLimite(LocalDateTime.now().plusDays(1));
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act
        boolean result = evaluacionService.estaVencida(1L);

        // Assert
        assertFalse(result);
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerTiempoRestante() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setFechaLimite(LocalDateTime.now().plusHours(2));
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        // Act
        Optional<Long> result = evaluacionService.obtenerTiempoRestante(1L);

        // Assert
        assertTrue(result.isPresent());
        assertTrue(result.get() > 0);
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testContarPorEjecucion() {
        // Arrange
        when(evaluacionRepository.countByEjecucionId(1L)).thenReturn(5);

        // Act
        Integer result = evaluacionService.contarPorEjecucion(1L);

        // Assert
        assertEquals(5, result);
        verify(evaluacionRepository, times(1)).countByEjecucionId(1L);
    }

    @Test
    void testObtenerProximasAVencer() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setFechaLimite(LocalDateTime.now().plusHours(12));
        
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByFechaFinBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerProximasAVencer(24);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1))
            .findByFechaFinBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testActivar() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setActivo(false);
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(evaluacionRepository.save(evaluacion)).thenReturn(evaluacion);

        // Act
        evaluacionService.activar(1L);

        // Assert
        assertTrue(evaluacion.getActivo());
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacion);
    }

    @Test
    void testDesactivar() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setActivo(true);
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(evaluacionRepository.save(evaluacion)).thenReturn(evaluacion);

        // Act
        evaluacionService.desactivar(1L);

        // Assert
        assertFalse(evaluacion.getActivo());
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacion);
    }

    @Test
    void testPublicar() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setPublicada(false);
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(evaluacionRepository.save(evaluacion)).thenReturn(evaluacion);

        // Act
        evaluacionService.publicar(1L);

        // Assert
        assertTrue(evaluacion.getPublicada());
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacion);
    }

    @Test
    void testDespublicar() {
        // Arrange
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setPublicada(true);
        
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(evaluacionRepository.save(evaluacion)).thenReturn(evaluacion);

        // Act
        evaluacionService.despublicar(1L);

        // Assert
        assertFalse(evaluacion.getPublicada());
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacion);
    }
}