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

import com.edutech.model.TipoPersona;
import com.edutech.repository.TipoPersonaRepository;

@SpringBootTest
class TipoPersonaServiceTest {

    @MockBean
    private TipoPersonaRepository tipoPersonaRepository;

    @Autowired
    private TipoPersonaService tipoPersonaService;

    @Test
    void testObtenerTodos() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        tipoPersona.setDescripcion("Tipo persona para estudiantes");
        
        List<TipoPersona> tipos = Arrays.asList(tipoPersona);
        when(tipoPersonaRepository.findAll()).thenReturn(tipos);

        // Act
        List<TipoPersona> result = tipoPersonaService.obtenerTodos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ESTUDIANTE", result.get(0).getNombre());
        verify(tipoPersonaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_TipoExiste() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        tipoPersona.setDescripcion("Tipo persona para estudiantes");
        
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoPersona));

        // Act
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ESTUDIANTE", result.get().getNombre());
        assertEquals("Tipo persona para estudiantes", result.get().getDescripcion());
        verify(tipoPersonaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_TipoNoExiste() {
        // Arrange
        when(tipoPersonaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(tipoPersonaRepository, times(1)).findById(999L);
    }

    @Test
    void testObtenerPorNombre_TipoExiste() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setNombre("ESTUDIANTE");
        
        when(tipoPersonaRepository.findByNombreIgnoreCase("ESTUDIANTE")).thenReturn(Optional.of(tipoPersona));

        // Act
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorNombre("ESTUDIANTE");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ESTUDIANTE", result.get().getNombre());
        verify(tipoPersonaRepository, times(1)).findByNombreIgnoreCase("ESTUDIANTE");
    }

    @Test
    void testCrear_TipoValido() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setNombre("ESTUDIANTE");
        tipoPersona.setDescripcion("Tipo persona para estudiantes");
        
        when(tipoPersonaRepository.save(any(TipoPersona.class))).thenReturn(tipoPersona);

        // Act
        TipoPersona result = tipoPersonaService.crear(tipoPersona);

        // Assert
        assertNotNull(result);
        assertEquals("ESTUDIANTE", result.getNombre());
        verify(tipoPersonaRepository, times(1)).save(tipoPersona);
    }

    @Test
    void testActualizar_TipoExiste() {
        // Arrange
        TipoPersona tipoExistente = new TipoPersona();
        tipoExistente.setId(1L);
        tipoExistente.setNombre("ESTUDIANTE");
        tipoExistente.setDescripcion("Tipo persona para estudiantes");
        
        TipoPersona tipoActualizado = new TipoPersona();
        tipoActualizado.setNombre("PROFESOR");
        tipoActualizado.setDescripcion("Tipo persona actualizado");
        
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoExistente));
        when(tipoPersonaRepository.save(any(TipoPersona.class))).thenReturn(tipoExistente);

        // Act
        Optional<TipoPersona> result = tipoPersonaService.actualizar(1L, tipoActualizado);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("PROFESOR", tipoExistente.getNombre());
        assertEquals("Tipo persona actualizado", tipoExistente.getDescripcion());
        verify(tipoPersonaRepository, times(1)).findById(1L);
        verify(tipoPersonaRepository, times(1)).save(tipoExistente);
    }

    @Test
    void testActualizar_TipoNoExiste() {
        // Arrange
        TipoPersona tipoActualizado = new TipoPersona();
        tipoActualizado.setNombre("PROFESOR");
        
        when(tipoPersonaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<TipoPersona> result = tipoPersonaService.actualizar(999L, tipoActualizado);

        // Assert
        assertFalse(result.isPresent());
        verify(tipoPersonaRepository, times(1)).findById(999L);
        verify(tipoPersonaRepository, never()).save(any());
    }

    @Test
    void testEliminar_TipoExiste() {
        // Arrange
        when(tipoPersonaRepository.existsById(1L)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> tipoPersonaService.eliminar(1L));

        // Assert
        verify(tipoPersonaRepository, times(1)).existsById(1L);
        verify(tipoPersonaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_TipoNoExiste() {
        // Arrange
        when(tipoPersonaRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tipoPersonaService.eliminar(999L)
        );
        
        assertEquals("Tipo de persona no encontrado con ID: 999", exception.getMessage());
        verify(tipoPersonaRepository, times(1)).existsById(999L);
        verify(tipoPersonaRepository, never()).deleteById(any());
    }
}