package com.edutech.service;

import com.edutech.model.Curso;
import com.edutech.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private Curso cursoActualizado;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("MAT001");
        curso.setNombre("Matemáticas Básica");
        curso.setDescripcion("Curso de matemáticas nivel básico");
        curso.setHorasTeoricas(3);
        curso.setHorasPracticas(2);
        curso.setActivo(true);

        cursoActualizado = new Curso();
        cursoActualizado.setNombre("Matemáticas Avanzada");
        cursoActualizado.setDescripcion("Curso actualizado");
        cursoActualizado.setHorasTeoricas(4);
        cursoActualizado.setHorasPracticas(3);
        cursoActualizado.setActivo(true);
    }

    @Test
    void testObtenerTodos() {
        // Arrange
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findAll()).thenReturn(cursos);

        // Act
        List<Curso> result = cursoService.obtenerTodos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MAT001", result.get(0).getCodigo());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_CursoExiste() {
        // Arrange
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        // Act
        Optional<Curso> result = cursoService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("MAT001", result.get().getCodigo());
        assertEquals("Matemáticas Básica", result.get().getNombre());
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_CursoNoExiste() {
        // Arrange
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Curso> result = cursoService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(cursoRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear_CursoValido() {
        // Arrange
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Act
        Curso result = cursoService.crear(curso);

        // Assert
        assertNotNull(result);
        assertEquals("MAT001", result.getCodigo());
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    void testActualizar_CursoExiste() {
        // Arrange
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Act
        Optional<Curso> result = cursoService.actualizar(1L, cursoActualizado);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Matemáticas Avanzada", curso.getNombre());
        assertEquals("Curso actualizado", curso.getDescripcion());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    void testActualizar_CursoNoExiste() {
        // Arrange
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Curso> result = cursoService.actualizar(999L, cursoActualizado);

        // Assert
        assertFalse(result.isPresent());
        verify(cursoRepository, times(1)).findById(999L);
        verify(cursoRepository, never()).save(any());
    }

    @Test
    void testEliminar_CursoExiste() {
        // Arrange
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = cursoService.eliminar(1L);

        // Assert
        assertTrue(result);
        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_CursoNoExiste() {
        // Arrange
        when(cursoRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = cursoService.eliminar(999L);

        // Assert
        assertFalse(result);
        verify(cursoRepository, times(1)).existsById(999L);
        verify(cursoRepository, never()).deleteById(any());
    }

    @Test
    void testBuscarPorNombre() {
        // Arrange
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findByNombreContainingIgnoreCase("matemáticas")).thenReturn(cursos);

        // Act
        List<Curso> result = cursoService.buscarPorNombre("matemáticas");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Matemáticas Básica", result.get(0).getNombre());
        verify(cursoRepository, times(1)).findByNombreContainingIgnoreCase("matemáticas");
    }

    @Test
    void testBuscarPorDescripcion() {
        // Arrange
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findByDescripcionContainingIgnoreCase("básico")).thenReturn(cursos);

        // Act
        List<Curso> result = cursoService.buscarPorDescripcion("básico");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cursoRepository, times(1)).findByDescripcionContainingIgnoreCase("básico");
    }

    @Test
    void testObtenerPorRangoDuracion() {
        // Arrange
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findByDuracionHorasBetween(20, 40)).thenReturn(cursos);

        // Act
        List<Curso> result = cursoService.obtenerPorRangoDuracion(20, 40);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cursoRepository, times(1)).findByDuracionHorasBetween(20, 40);
    }

    @Test
    void testObtenerOrdenadosPorNombre() {
        // Arrange
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findAllByOrderByNombreAsc()).thenReturn(cursos);

        // Act
        List<Curso> result = cursoService.obtenerOrdenadosPorNombre();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cursoRepository, times(1)).findAllByOrderByNombreAsc();
    }

    @Test
    void testObtenerOrdenadosPorDuracion_Ascendente() {
        // Arrange
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findAllByOrderByDuracionHorasAsc()).thenReturn(cursos);

        // Act
        List<Curso> result = cursoService.obtenerOrdenadosPorDuracion(true);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cursoRepository, times(1)).findAllByOrderByDuracionHorasAsc();
    }

    @Test
    void testObtenerOrdenadosPorDuracion_Descendente() {
        // Arrange
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findAllByOrderByDuracionHorasDesc()).thenReturn(cursos);

        // Act
        List<Curso> result = cursoService.obtenerOrdenadosPorDuracion(false);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cursoRepository, times(1)).findAllByOrderByDuracionHorasDesc();
    }

    @Test
    void testExistePorNombre_Existe() {
        // Arrange
        when(cursoRepository.existsByNombreIgnoreCase("Matemáticas Básica")).thenReturn(true);

        // Act
        boolean result = cursoService.existePorNombre("Matemáticas Básica");

        // Assert
        assertTrue(result);
        verify(cursoRepository, times(1)).existsByNombreIgnoreCase("Matemáticas Básica");
    }

    @Test
    void testExistePorNombre_NoExiste() {
        // Arrange
        when(cursoRepository.existsByNombreIgnoreCase("Curso Inexistente")).thenReturn(false);

        // Act
        boolean result = cursoService.existePorNombre("Curso Inexistente");

        // Assert
        assertFalse(result);
        verify(cursoRepository, times(1)).existsByNombreIgnoreCase("Curso Inexistente");
    }
}