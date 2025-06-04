package com.edutech.service;

import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.repository.EjecucionRepository;
import com.edutech.repository.CursoRepository;
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
class EjecucionServiceTest {

    @Mock
    private EjecucionRepository ejecucionRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private EjecucionService ejecucionService;

    private Ejecucion ejecucion;
    private Ejecucion ejecucionActualizada;
    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");
        curso.setCodigo("MAT001");

        ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        ejecucion.setFechaInicio(LocalDate.now().plusDays(1));
        ejecucion.setFechaFin(LocalDate.now().plusDays(60));
        ejecucion.setCuposDisponibles(30);
        ejecucion.setCapacidadMaxima(30);

        ejecucionActualizada = new Ejecucion();
        ejecucionActualizada.setCurso(curso);
        ejecucionActualizada.setSeccion("B");
        ejecucionActualizada.setPeriodo("2024-2");
        ejecucionActualizada.setFechaInicio(LocalDate.now().plusDays(10));
        ejecucionActualizada.setFechaFin(LocalDate.now().plusDays(70));
        ejecucionActualizada.setCuposDisponibles(25);
    }

    @Test
    void testObtenerTodas() {
        // Arrange
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findAll()).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerTodas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getSeccion());
        verify(ejecucionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_EjecucionExiste() {
        // Arrange
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));

        // Act
        Optional<Ejecucion> result = ejecucionService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getSeccion());
        assertEquals("2024-1", result.get().getPeriodo());
        verify(ejecucionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_EjecucionNoExiste() {
        // Arrange
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Ejecucion> result = ejecucionService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(ejecucionRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear_EjecucionValida() {
        // Arrange
        when(cursoRepository.existsById(1L)).thenReturn(true);
        when(ejecucionRepository.existsByCursoIdAndSeccionAndPeriodo(1L, "A", "2024-1")).thenReturn(false);
        when(ejecucionRepository.save(any(Ejecucion.class))).thenReturn(ejecucion);

        // Act
        Ejecucion result = ejecucionService.crear(ejecucion);

        // Assert
        assertNotNull(result);
        assertEquals("A", result.getSeccion());
        verify(cursoRepository, times(1)).existsById(1L);
        verify(ejecucionRepository, times(1)).save(ejecucion);
    }

    @Test
    void testCrear_CursoNoExiste() {
        // Arrange
        when(cursoRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("Curso no encontrado con ID: 1", exception.getMessage());
        verify(cursoRepository, times(1)).existsById(1L);
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_EjecucionDuplicada() {
        // Arrange
        when(cursoRepository.existsById(1L)).thenReturn(true);
        when(ejecucionRepository.existsByCursoIdAndSeccionAndPeriodo(1L, "A", "2024-1")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("Ya existe una ejecución del curso con la misma sección en este período", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_FechaInicioAnteriorAHoy() {
        // Arrange
        ejecucion.setFechaInicio(LocalDate.now().minusDays(1));
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("La fecha de inicio no puede ser anterior a la fecha actual", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_FechaInicioMayorQueFin() {
        // Arrange
        ejecucion.setFechaInicio(LocalDate.now().plusDays(60));
        ejecucion.setFechaFin(LocalDate.now().plusDays(30));
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("La fecha de inicio no puede ser posterior a la fecha de fin", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_CupoMaximoInvalido() {
        // Arrange
        ejecucion.setCuposDisponibles(0);
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("El cupo máximo debe ser mayor a 0", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_CupoMaximoExcedeLimite() {
        // Arrange
        ejecucion.setCuposDisponibles(150);
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("El cupo máximo no puede exceder 100 estudiantes", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testActualizar_EjecucionExiste() {
        // Arrange
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.save(any(Ejecucion.class))).thenReturn(ejecucion);

        // Act
        Ejecucion result = ejecucionService.actualizar(1L, ejecucionActualizada);

        // Assert
        assertNotNull(result);
        assertEquals("B", ejecucion.getSeccion());
        assertEquals("2024-2", ejecucion.getPeriodo());
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).save(ejecucion);
    }

    @Test
    void testActualizar_EjecucionNoExiste() {
        // Arrange
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.actualizar(999L, ejecucionActualizada)
        );
        
        assertEquals("Ejecución no encontrada con ID: 999", exception.getMessage());
        verify(ejecucionRepository, times(1)).findById(999L);
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testEliminar_EjecucionExiste() {
        // Arrange
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));

        // Act
        assertDoesNotThrow(() -> ejecucionService.eliminar(1L));

        // Assert
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).delete(ejecucion);
    }

    @Test
    void testEliminar_EjecucionNoExiste() {
        // Arrange
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.eliminar(999L)
        );
        
        assertEquals("Ejecución no encontrada con ID: 999", exception.getMessage());
        verify(ejecucionRepository, times(1)).findById(999L);
        verify(ejecucionRepository, never()).delete(any());
    }

    @Test
    void testObtenerPorCurso() {
        // Arrange
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findByCursoId(1L)).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerPorCurso(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ejecucionRepository, times(1)).findByCursoId(1L);
    }

    @Test
    void testObtenerActivas() {
        // Arrange
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findEjecucionesActivas()).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerActivas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ejecucionRepository, times(1)).findEjecucionesActivas();
    }

    @Test
    void testObtenerFuturas() {
        // Arrange
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findEjecucionesFuturas()).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerFuturas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ejecucionRepository, times(1)).findEjecucionesFuturas();
    }

    @Test
    void testObtenerPasadas() {
        // Arrange
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findEjecucionesPasadas()).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerPasadas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ejecucionRepository, times(1)).findEjecucionesPasadas();
    }

    @Test
    void testObtenerConCuposDisponibles() {
        // Arrange
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findEjecucionesConCuposDisponibles()).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerConCuposDisponibles();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ejecucionRepository, times(1)).findEjecucionesConCuposDisponibles();
    }

    @Test
    void testContarEstudiantesInscritos() {
        // Arrange
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(15);

        // Act
        Integer result = ejecucionService.contarEstudiantesInscritos(1L);

        // Assert
        assertEquals(15, result);
        verify(ejecucionRepository, times(1)).countEstudiantesInscritos(1L);
    }

    @Test
    void testTieneCuposDisponibles_ConCupos() {
        // Arrange
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(20);

        // Act
        boolean result = ejecucionService.tieneCuposDisponibles(1L);

        // Assert
        assertTrue(result);
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).countEstudiantesInscritos(1L);
    }

    @Test
    void testTieneCuposDisponibles_SinCupos() {
        // Arrange
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(30);

        // Act
        boolean result = ejecucionService.tieneCuposDisponibles(1L);

        // Assert
        assertFalse(result);
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).countEstudiantesInscritos(1L);
    }

    @Test
    void testObtenerCuposDisponibles() {
        // Arrange
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(10);

        // Act
        Integer result = ejecucionService.obtenerCuposDisponibles(1L);

        // Assert
        assertEquals(20, result);
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).countEstudiantesInscritos(1L);
    }

    @Test
    void testExisteEjecucionActivaParaCurso() {
        // Arrange
        when(ejecucionRepository.existeEjecucionActivaParaCurso(1L)).thenReturn(true);

        // Act
        boolean result = ejecucionService.existeEjecucionActivaParaCurso(1L);

        // Assert
        assertTrue(result);
        verify(ejecucionRepository, times(1)).existeEjecucionActivaParaCurso(1L);
    }

    @Test
    void testObtenerPorRangoFechas() {
        // Arrange
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(30);
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findByFechaInicioBetween(inicio, fin)).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerPorRangoFechas(inicio, fin);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ejecucionRepository, times(1)).findByFechaInicioBetween(inicio, fin);
    }

    @Test
    void testObtenerPorRangoCupo() {
        // Arrange
        List<Ejecucion> ejecuciones = Arrays.asList(ejecucion);
        when(ejecucionRepository.findByCapacidadMaximaBetween(20, 40)).thenReturn(ejecuciones);

        // Act
        List<Ejecucion> result = ejecucionService.obtenerPorRangoCupo(20, 40);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ejecucionRepository, times(1)).findByCapacidadMaximaBetween(20, 40);
    }
}