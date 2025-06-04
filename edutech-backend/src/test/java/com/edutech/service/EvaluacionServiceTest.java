package com.edutech.service;

import com.edutech.model.Evaluacion;
import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.repository.EvaluacionRepository;
import com.edutech.repository.EjecucionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @Mock
    private EjecucionRepository ejecucionRepository;

    @InjectMocks
    private EvaluacionService evaluacionService;

    private Evaluacion evaluacion;
    private Evaluacion evaluacionActualizada;
    private Ejecucion ejecucion;
    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Avanzadas");

        ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");

        evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setTitulo("Examen Parcial");
        evaluacion.setDescripcion("Evaluación de conocimientos adquiridos");
        evaluacion.setTipo("PARCIAL");
        evaluacion.setEjecucion(ejecucion);
        evaluacion.setFechaInicio(LocalDateTime.now().plusDays(1));
        evaluacion.setFechaFin(LocalDateTime.now().plusDays(2));
        evaluacion.setDuracionMinutos(120);
        evaluacion.setPuntajeTotal(100.0);
        evaluacion.setPonderacion(30.0);
        evaluacion.setIntentosPermitidos(2);
        evaluacion.setNotaMinimaAprobacion(4.0);
        evaluacion.setNotaMaxima(7.0);
        evaluacion.setExigenciaPorcentual(60.0);
        evaluacion.setActivo(true);
        evaluacion.setPublicada(true);

        evaluacionActualizada = new Evaluacion();
        evaluacionActualizada.setTitulo("Examen Final");
        evaluacionActualizada.setDescripcion("Evaluación final del curso");
        evaluacionActualizada.setTipo("FINAL");
        evaluacionActualizada.setEjecucion(ejecucion);
        evaluacionActualizada.setFechaInicio(LocalDateTime.now().plusDays(5));
        evaluacionActualizada.setFechaFin(LocalDateTime.now().plusDays(6));
        evaluacionActualizada.setDuracionMinutos(180);
        evaluacionActualizada.setPuntajeTotal(100.0);
        evaluacionActualizada.setPonderacion(40.0);
        evaluacionActualizada.setIntentosPermitidos(1);
        evaluacionActualizada.setNotaMinimaAprobacion(4.0);
        evaluacionActualizada.setNotaMaxima(7.0);
        evaluacionActualizada.setExigenciaPorcentual(60.0);
        evaluacionActualizada.setActivo(true);
        evaluacionActualizada.setPublicada(false);
    }

    @Test
    void testObtenerTodas() {
        // Arrange
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
        evaluacion.setFechaInicio(LocalDateTime.now().plusDays(2));
        evaluacion.setFechaFin(LocalDateTime.now().plusDays(1)); // Fecha fin antes que inicio
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
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(ejecucionRepository.existsById(1L)).thenReturn(true);
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacion);

        // Act
        Evaluacion result = evaluacionService.actualizar(1L, evaluacionActualizada);

        // Assert
        assertNotNull(result);
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacion);
    }

    @Test
    void testActualizar_EvaluacionNoExiste() {
        // Arrange
        when(evaluacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> evaluacionService.actualizar(999L, evaluacionActualizada)
        );
        
        assertEquals("Evaluación no encontrada con ID: 999", exception.getMessage());
        verify(evaluacionRepository, times(1)).findById(999L);
        verify(evaluacionRepository, never()).save(any());
    }

    @Test
    void testEliminar_EvaluacionExiste() {
        // Arrange
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
        evaluacion.setFechaInicio(LocalDateTime.now().minusDays(1)); // Ya comenzó
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
    void testObtenerPorTipo() {
        // Arrange
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion);
        when(evaluacionRepository.findByTipoIgnoreCase("PARCIAL")).thenReturn(evaluaciones);

        // Act
        List<Evaluacion> result = evaluacionService.obtenerPorTipo("PARCIAL");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(evaluacionRepository, times(1)).findByTipoIgnoreCase("PARCIAL");
    }

    @Test
    void testObtenerActivas() {
        // Arrange
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
        LocalDateTime ahora = LocalDateTime.now();
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
        evaluacion.setFechaFin(LocalDateTime.now().minusDays(1));
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
        LocalDateTime limite = LocalDateTime.now().plusHours(24);
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