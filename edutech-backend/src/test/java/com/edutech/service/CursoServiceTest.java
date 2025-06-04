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

import com.edutech.model.Curso;
import com.edutech.repository.CursoRepository;

@SpringBootTest
class CursoServiceTest {

    @MockBean
    private CursoRepository cursoRepository;

    @Autowired
    private CursoService cursoService;

    @Test
    void testObtenerTodos() {
        // Arrange
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("MAT001");
        curso.setNombre("Matemáticas Básica");
        
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
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("MAT001");
        curso.setNombre("Matemáticas Básica");
        
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
        Curso curso = new Curso();
        curso.setCodigo("MAT001");
        curso.setNombre("Matemáticas Básica");
        
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
        Curso cursoExistente = new Curso();
        cursoExistente.setId(1L);
        cursoExistente.setCodigo("MAT001");
        cursoExistente.setNombre("Matemáticas Básica");
        
        Curso cursoActualizado = new Curso();
        cursoActualizado.setNombre("Matemáticas Avanzada");
        cursoActualizado.setDescripcion("Curso actualizado");
        
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoExistente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoExistente);

        // Act
        Optional<Curso> result = cursoService.actualizar(1L, cursoActualizado);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Matemáticas Avanzada", cursoExistente.getNombre());
        assertEquals("Curso actualizado", cursoExistente.getDescripcion());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).save(cursoExistente);
    }

    @Test
    void testActualizar_CursoNoExiste() {
        // Arrange
        Curso cursoActualizado = new Curso();
        cursoActualizado.setNombre("Matemáticas Avanzada");
        
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
        Curso curso = new Curso();
        curso.setNombre("Matemáticas Básica");
        
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