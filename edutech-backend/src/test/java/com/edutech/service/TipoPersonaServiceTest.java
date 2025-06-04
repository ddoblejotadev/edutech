package com.edutech.service;

import com.edutech.model.TipoPersona;
import com.edutech.repository.TipoPersonaRepository;
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
class TipoPersonaServiceTest {

    @Mock
    private TipoPersonaRepository tipoPersonaRepository;

    @InjectMocks
    private TipoPersonaService tipoPersonaService;

    private TipoPersona tipoPersona;
    private TipoPersona tipoPersonaActualizado;

    @BeforeEach
    void setUp() {
        tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        tipoPersona.setDescripcion("Tipo persona para estudiantes");

        tipoPersonaActualizado = new TipoPersona();
        tipoPersonaActualizado.setNombre("PROFESOR");
        tipoPersonaActualizado.setDescripcion("Tipo persona actualizado");
    }

    @Test
    void testObtenerTodos() {
        // Arrange
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
        when(tipoPersonaRepository.findByNombreIgnoreCase("ESTUDIANTE")).thenReturn(Optional.of(tipoPersona));

        // Act
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorNombre("ESTUDIANTE");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ESTUDIANTE", result.get().getNombre());
        verify(tipoPersonaRepository, times(1)).findByNombreIgnoreCase("ESTUDIANTE");
    }

    @Test
    void testObtenerPorNombre_TipoNoExiste() {
        // Arrange
        when(tipoPersonaRepository.findByNombreIgnoreCase("INEXISTENTE")).thenReturn(Optional.empty());

        // Act
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorNombre("INEXISTENTE");

        // Assert
        assertFalse(result.isPresent());
        verify(tipoPersonaRepository, times(1)).findByNombreIgnoreCase("INEXISTENTE");
    }

    @Test
    void testCrear_TipoValido() {
        // Arrange
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
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoPersona));
        when(tipoPersonaRepository.save(any(TipoPersona.class))).thenReturn(tipoPersona);

        // Act
        Optional<TipoPersona> result = tipoPersonaService.actualizar(1L, tipoPersonaActualizado);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("PROFESOR", tipoPersona.getNombre());
        assertEquals("Tipo persona actualizado", tipoPersona.getDescripcion());
        verify(tipoPersonaRepository, times(1)).findById(1L);
        verify(tipoPersonaRepository, times(1)).save(tipoPersona);
    }

    @Test
    void testActualizar_TipoNoExiste() {
        // Arrange
        when(tipoPersonaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<TipoPersona> result = tipoPersonaService.actualizar(999L, tipoPersonaActualizado);

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