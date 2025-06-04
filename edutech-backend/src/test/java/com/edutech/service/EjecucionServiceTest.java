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
import java.time.LocalDate;

import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.repository.EjecucionRepository;
import com.edutech.repository.CursoRepository;

@SpringBootTest
class EjecucionServiceTest {

    @MockBean
    private EjecucionRepository ejecucionRepository;

    @MockBean
    private CursoRepository cursoRepository;

    @Autowired
    private EjecucionService ejecucionService;

    @Test
    void testObtenerTodas() {
        // Arrange
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");

        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        
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
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        
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
    void testCrear_EjecucionValida() {
        // Arrange
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        ejecucion.setFechaInicio(LocalDate.now().plusDays(1));
        ejecucion.setFechaFin(LocalDate.now().plusDays(60));
        ejecucion.setCuposDisponibles(30);
        ejecucion.setCapacidadMaxima(30);
        
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
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCurso(curso);
        
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
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        
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
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCurso(curso);
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
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCurso(curso);
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
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCurso(curso);
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
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCurso(curso);
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
        Curso curso = new Curso();
        curso.setId(1L);
        
        Ejecucion ejecucionExistente = new Ejecucion();
        ejecucionExistente.setId(1L);
        ejecucionExistente.setCurso(curso);
        ejecucionExistente.setSeccion("A");
        ejecucionExistente.setPeriodo("2024-1");
        
        Ejecucion ejecucionActualizada = new Ejecucion();
        ejecucionActualizada.setSeccion("B");
        ejecucionActualizada.setPeriodo("2024-2");
        
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucionExistente));
        when(ejecucionRepository.save(any(Ejecucion.class))).thenReturn(ejecucionExistente);

        // Act
        Ejecucion result = ejecucionService.actualizar(1L, ejecucionActualizada);

        // Assert
        assertNotNull(result);
        assertEquals("B", ejecucionExistente.getSeccion());
        assertEquals("2024-2", ejecucionExistente.getPeriodo());
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).save(ejecucionExistente);
    }

    @Test
    void testActualizar_EjecucionNoExiste() {
        // Arrange
        Ejecucion ejecucionActualizada = new Ejecucion();
        ejecucionActualizada.setSeccion("B");
        
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
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
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
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setSeccion("A");
        
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
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setSeccion("A");
        
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
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCapacidadMaxima(30);
        
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
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setCapacidadMaxima(30);
        
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(30);

        // Act
        boolean result = ejecucionService.tieneCuposDisponibles(1L);

        // Assert
        assertFalse(result);
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).countEstudiantesInscritos(1L);
    }
}