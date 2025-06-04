package com.edutech.service;

import com.edutech.model.Calificacion;
import com.edutech.model.Evaluacion;
import com.edutech.model.Persona;
import com.edutech.repository.CalificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository calificacionRepository;

    @InjectMocks
    private CalificacionService calificacionService;

    private Calificacion calificacion;
    private Persona persona;
    private Evaluacion evaluacion;

    @BeforeEach
    void setUp() {
        // Configurar persona de prueba
        persona = new Persona();
        persona.setId(1L);
        persona.setNombres("Juan");
        persona.setApellidoPaterno("Pérez");
        persona.setCorreo("juan.perez@test.com");

        // Configurar evaluación de prueba
        evaluacion = new Evaluacion();
        evaluacion.setId(1L);
        evaluacion.setTitulo("Evaluación Test");
        evaluacion.setPuntajeTotal(100.0);

        // Configurar calificación de prueba
        calificacion = new Calificacion();
        calificacion.setId(1L);
        calificacion.setPersona(persona);
        calificacion.setEvaluacion(evaluacion);
        calificacion.setPuntajeObtenido(85.0);
        calificacion.setPuntajeMaximo(100.0);
        calificacion.setNumeroIntento(1);
        calificacion.setFechaRealizacion(LocalDateTime.now());
        calificacion.setTiempoEmpleado(60);
        calificacion.setEstado("COMPLETADA");
        calificacion.setObservaciones("Buen desempeño");
    }

    @Test
    void testRegistrarCalificacion() {
        // Arrange
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);

        // Act
        Calificacion resultado = calificacionService.registrar(calificacion);

        // Assert
        assertNotNull(resultado);
        assertEquals(calificacion.getId(), resultado.getId());
        assertEquals(calificacion.getPuntajeObtenido(), resultado.getPuntajeObtenido());
        verify(calificacionRepository, times(1)).save(any(Calificacion.class));
    }

    @Test
    void testObtenerPorId() {
        // Arrange
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        // Act
        Optional<Calificacion> resultado = calificacionService.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(calificacion.getId(), resultado.get().getId());
        verify(calificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorIdNoEncontrado() {
        // Arrange
        when(calificacionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Calificacion> resultado = calificacionService.obtenerPorId(1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(calificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarCalificacion() {
        // Arrange
        Calificacion calificacionActualizada = new Calificacion();
        calificacionActualizada.setPersona(persona);
        calificacionActualizada.setEvaluacion(evaluacion);
        calificacionActualizada.setPuntajeObtenido(90.0);
        calificacionActualizada.setPuntajeMaximo(100.0);
        calificacionActualizada.setNumeroIntento(1);
        calificacionActualizada.setFechaRealizacion(LocalDateTime.now());
        calificacionActualizada.setTiempoEmpleado(45);
        calificacionActualizada.setEstado("COMPLETADA");
        calificacionActualizada.setObservaciones("Excelente desempeño");

        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacionActualizada);

        // Act
        Calificacion resultado = calificacionService.actualizar(1L, calificacionActualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals(90.0, resultado.getPuntajeObtenido());
        verify(calificacionRepository, times(1)).findById(1L);
        verify(calificacionRepository, times(1)).save(any(Calificacion.class));
    }

    @Test
    void testActualizarCalificacionNoEncontrada() {
        // Arrange
        Calificacion calificacionActualizada = new Calificacion();
        when(calificacionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calificacionService.actualizar(1L, calificacionActualizada);
        });
        verify(calificacionRepository, times(1)).findById(1L);
        verify(calificacionRepository, never()).save(any(Calificacion.class));
    }

    @Test
    void testEliminarCalificacion() {
        // Arrange
        when(calificacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(calificacionRepository).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> calificacionService.eliminar(1L));

        // Assert
        verify(calificacionRepository, times(1)).existsById(1L);
        verify(calificacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarCalificacionNoEncontrada() {
        // Arrange
        when(calificacionRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calificacionService.eliminar(1L);
        });
        verify(calificacionRepository, times(1)).existsById(1L);
        verify(calificacionRepository, never()).deleteById(1L);
    }

    @Test
    void testObtenerTodas() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findAll()).thenReturn(calificaciones);

        // Act
        List<Calificacion> resultado = calificacionService.obtenerTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(calificacion.getId(), resultado.get(0).getId());
        verify(calificacionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorEstudiante() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByPersonaId(1L)).thenReturn(calificaciones);

        // Act
        List<Calificacion> resultado = calificacionService.obtenerPorEstudiante(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(calificacion.getId(), resultado.get(0).getId());
        verify(calificacionRepository, times(1)).findByPersonaId(1L);
    }

    @Test
    void testObtenerPorEvaluacion() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByEvaluacionId(1L)).thenReturn(calificaciones);

        // Act
        List<Calificacion> resultado = calificacionService.obtenerPorEvaluacion(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(calificacion.getId(), resultado.get(0).getId());
        verify(calificacionRepository, times(1)).findByEvaluacionId(1L);
    }

    @Test
    void testValidarCalificacionConEstudianteNulo() {
        // Arrange
        Calificacion calificacionInvalida = new Calificacion();
        calificacionInvalida.setEvaluacion(evaluacion);
        calificacionInvalida.setPuntajeObtenido(85.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calificacionService.registrar(calificacionInvalida);
        });
    }

    @Test
    void testValidarCalificacionConEvaluacionNula() {
        // Arrange
        Calificacion calificacionInvalida = new Calificacion();
        calificacionInvalida.setPersona(persona);
        calificacionInvalida.setPuntajeObtenido(85.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calificacionService.registrar(calificacionInvalida);
        });
    }

    @Test
    void testValidarCalificacionConPuntajeNegativo() {
        // Arrange
        Calificacion calificacionInvalida = new Calificacion();
        calificacionInvalida.setPersona(persona);
        calificacionInvalida.setEvaluacion(evaluacion);
        calificacionInvalida.setPuntajeObtenido(-10.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calificacionService.registrar(calificacionInvalida);
        });
    }

    @Test
    void testExisteCalificacion() {
        // Arrange
        when(calificacionRepository.existsByPersonaIdAndEvaluacionId(1L, 1L)).thenReturn(true);

        // Act
        boolean resultado = calificacionService.existeCalificacion(1L, 1L);

        // Assert
        assertTrue(resultado);
        verify(calificacionRepository, times(1)).existsByPersonaIdAndEvaluacionId(1L, 1L);
    }

    @Test
    void testCalcularPromedioEvaluacion() {
        // Arrange
        when(calificacionRepository.calcularPromedioByEvaluacion(1L)).thenReturn(85.5);

        // Act
        Double resultado = calificacionService.calcularPromedioEvaluacion(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(85.5, resultado);
        verify(calificacionRepository, times(1)).calcularPromedioByEvaluacion(1L);
    }

    @Test
    void testCalcularPromedioEstudiante() {
        // Arrange
        when(calificacionRepository.calcularPromedioByEstudiante(1L)).thenReturn(87.3);

        // Act
        Double resultado = calificacionService.calcularPromedioEstudiante(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(87.3, resultado);
        verify(calificacionRepository, times(1)).calcularPromedioByEstudiante(1L);
    }
}