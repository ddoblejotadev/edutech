package com.edutech.service;

import com.edutech.model.Calificacion;
import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;
import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.repository.CalificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository calificacionRepository;

    @InjectMocks
    private CalificacionService calificacionService;

    private Calificacion calificacion;
    private Calificacion calificacionActualizada;
    private Inscripcion inscripcion;
    private Persona estudiante;
    private Ejecucion ejecucion;
    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");

        estudiante = new Persona();
        estudiante.setId(1L);
        estudiante.setRut("12345678-9");
        estudiante.setNombres("Juan Carlos");

        ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");

        inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setEstudiante(estudiante);
        inscripcion.setEjecucion(ejecucion);

        calificacion = new Calificacion();
        calificacion.setId(1L);
        calificacion.setInscripcion(inscripcion);
        calificacion.setTipoEvaluacion("PARCIAL");
        calificacion.setNota(6.5);
        calificacion.setFechaEvaluacion(LocalDate.now());
        calificacion.setObservaciones("Buen desempeño");

        calificacionActualizada = new Calificacion();
        calificacionActualizada.setInscripcion(inscripcion);
        calificacionActualizada.setTipoEvaluacion("FINAL");
        calificacionActualizada.setNota(7.0);
        calificacionActualizada.setFechaEvaluacion(LocalDate.now());
        calificacionActualizada.setObservaciones("Excelente desempeño");
    }

    @Test
    void testObtenerTodas() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findAll()).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerTodas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(6.5, result.get(0).getNota());
        assertEquals("PARCIAL", result.get(0).getTipoEvaluacion());
        verify(calificacionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_CalificacionExiste() {
        // Arrange
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        // Act
        Optional<Calificacion> result = calificacionService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(6.5, result.get().getNota());
        assertEquals("PARCIAL", result.get().getTipoEvaluacion());
        assertEquals("Buen desempeño", result.get().getObservaciones());
        verify(calificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_CalificacionNoExiste() {
        // Arrange
        when(calificacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Calificacion> result = calificacionService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(calificacionRepository, times(1)).findById(999L);
    }

    @Test
    void testRegistrar_CalificacionValida() {
        // Arrange
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);

        // Act
        Calificacion result = calificacionService.registrar(calificacion);

        // Assert
        assertNotNull(result);
        assertEquals(6.5, result.getNota());
        assertEquals("PARCIAL", result.getTipoEvaluacion());
        verify(calificacionRepository, times(1)).save(calificacion);
    }

    @Test
    void testRegistrar_InscripcionVacia() {
        // Arrange
        calificacion.setInscripcion(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calificacionService.registrar(calificacion)
        );
        
        assertEquals("La inscripción es obligatoria", exception.getMessage());
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void testRegistrar_NotaNula() {
        // Arrange
        calificacion.setNota(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calificacionService.registrar(calificacion)
        );
        
        assertEquals("La nota es obligatoria", exception.getMessage());
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void testRegistrar_NotaFueraDeRango_Menor() {
        // Arrange
        calificacion.setNota(0.5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calificacionService.registrar(calificacion)
        );
        
        assertEquals("La nota debe estar entre 1.0 y 7.0", exception.getMessage());
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void testRegistrar_NotaFueraDeRango_Mayor() {
        // Arrange
        calificacion.setNota(7.5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calificacionService.registrar(calificacion)
        );
        
        assertEquals("La nota debe estar entre 1.0 y 7.0", exception.getMessage());
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void testRegistrar_TipoEvaluacionVacio() {
        // Arrange
        calificacion.setTipoEvaluacion("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calificacionService.registrar(calificacion)
        );
        
        assertEquals("El tipo de evaluación es obligatorio", exception.getMessage());
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void testActualizar_CalificacionExiste() {
        // Arrange
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);

        // Act
        Calificacion result = calificacionService.actualizar(1L, calificacionActualizada);

        // Assert
        assertNotNull(result);
        assertEquals(7.0, calificacion.getNota());
        assertEquals("FINAL", calificacion.getTipoEvaluacion());
        assertEquals("Excelente desempeño", calificacion.getObservaciones());
        verify(calificacionRepository, times(1)).findById(1L);
        verify(calificacionRepository, times(1)).save(calificacion);
    }

    @Test
    void testActualizar_CalificacionNoExiste() {
        // Arrange
        when(calificacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calificacionService.actualizar(999L, calificacionActualizada)
        );
        
        assertEquals("Calificación no encontrada con ID: 999", exception.getMessage());
        verify(calificacionRepository, times(1)).findById(999L);
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void testEliminar_CalificacionExiste() {
        // Arrange
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        // Act
        assertDoesNotThrow(() -> calificacionService.eliminar(1L));

        // Assert
        verify(calificacionRepository, times(1)).findById(1L);
        verify(calificacionRepository, times(1)).delete(calificacion);
    }

    @Test
    void testEliminar_CalificacionNoExiste() {
        // Arrange
        when(calificacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calificacionService.eliminar(999L)
        );
        
        assertEquals("Calificación no encontrada con ID: 999", exception.getMessage());
        verify(calificacionRepository, times(1)).findById(999L);
        verify(calificacionRepository, never()).delete(any());
    }

    @Test
    void testObtenerPorEstudiante() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByInscripcionPersonaId(1L)).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerPorEstudiante(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(calificacionRepository, times(1)).findByInscripcionPersonaId(1L);
    }

    @Test
    void testObtenerPorEjecucion() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByInscripcionEjecucionId(1L)).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerPorEjecucion(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(calificacionRepository, times(1)).findByInscripcionEjecucionId(1L);
    }

    @Test
    void testObtenerPorTipoEvaluacion() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByTipoEvaluacionIgnoreCase("PARCIAL")).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerPorTipoEvaluacion("PARCIAL");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(calificacionRepository, times(1)).findByTipoEvaluacionIgnoreCase("PARCIAL");
    }

    @Test
    void testObtenerPorRangoNotas() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByNotaBetween(6.0, 7.0)).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerPorRangoNotas(6.0, 7.0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(calificacionRepository, times(1)).findByNotaBetween(6.0, 7.0);
    }

    @Test
    void testObtenerAprobadas() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByNotaGreaterThanEqual(4.0)).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerAprobadas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(calificacionRepository, times(1)).findByNotaGreaterThanEqual(4.0);
    }

    @Test
    void testObtenerReprobadas() {
        // Arrange
        calificacion.setNota(3.5);
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findByNotaLessThan(4.0)).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerReprobadas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(calificacionRepository, times(1)).findByNotaLessThan(4.0);
    }

    @Test
    void testCalcularPromedio() {
        // Arrange
        when(calificacionRepository.calcularPromedioEstudiante(1L)).thenReturn(6.2);

        // Act
        Double result = calificacionService.calcularPromedio(1L);

        // Assert
        assertEquals(6.2, result);
        verify(calificacionRepository, times(1)).calcularPromedioEstudiante(1L);
    }

    @Test
    void testCalcularPromedioPorEjecucion() {
        // Arrange
        when(calificacionRepository.calcularPromedioEjecucion(1L)).thenReturn(6.8);

        // Act
        Double result = calificacionService.calcularPromedioPorEjecucion(1L);

        // Assert
        assertEquals(6.8, result);
        verify(calificacionRepository, times(1)).calcularPromedioEjecucion(1L);
    }

    @Test
    void testObtenerMejoresCalificaciones() {
        // Arrange
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findTop10ByOrderByNotaDesc()).thenReturn(calificaciones);

        // Act
        List<Calificacion> result = calificacionService.obtenerMejoresCalificaciones();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(calificacionRepository, times(1)).findTop10ByOrderByNotaDesc();
    }

    @Test
    void testContarAprobados() {
        // Arrange
        when(calificacionRepository.countByNotaGreaterThanEqual(4.0)).thenReturn(25L);

        // Act
        Long result = calificacionService.contarAprobados();

        // Assert
        assertEquals(25L, result);
        verify(calificacionRepository, times(1)).countByNotaGreaterThanEqual(4.0);
    }

    @Test
    void testContarReprobados() {
        // Arrange
        when(calificacionRepository.countByNotaLessThan(4.0)).thenReturn(5L);

        // Act
        Long result = calificacionService.contarReprobados();

        // Assert
        assertEquals(5L, result);
        verify(calificacionRepository, times(1)).countByNotaLessThan(4.0);
    }

    @Test
    void testEstaAprobada_Aprobada() {
        // Arrange
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        // Act
        boolean result = calificacionService.estaAprobada(1L);

        // Assert
        assertTrue(result);
        verify(calificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testEstaAprobada_Reprobada() {
        // Arrange
        calificacion.setNota(3.5);
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        // Act
        boolean result = calificacionService.estaAprobada(1L);

        // Assert
        assertFalse(result);
        verify(calificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testEstaAprobada_CalificacionNoExiste() {
        // Arrange
        when(calificacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        boolean result = calificacionService.estaAprobada(999L);

        // Assert
        assertFalse(result);
        verify(calificacionRepository, times(1)).findById(999L);
    }
}