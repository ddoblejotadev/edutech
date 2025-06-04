package com.edutech.service;

import com.edutech.model.Persona;
import com.edutech.model.TipoPersona;
import com.edutech.repository.PersonaRepository;
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
class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    private Persona persona;
    private Persona personaActualizada;
    private TipoPersona tipoPersona;

    @BeforeEach
    void setUp() {
        tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");

        persona = new Persona();
        persona.setId(1L);
        persona.setRut("12345678-9");
        persona.setNombres("Juan Carlos");
        persona.setApellidoPaterno("Pérez");
        persona.setApellidoMaterno("González");
        persona.setApellidos("Pérez González");
        persona.setCorreo("juan.perez@duocuc.cl");
        persona.setTelefono("+56912345678");
        persona.setTipoPersona(tipoPersona);

        personaActualizada = new Persona();
        personaActualizada.setNombres("Juan Carlos Actualizado");
        personaActualizada.setApellidoPaterno("Pérez");
        personaActualizada.setApellidoMaterno("González");
        personaActualizada.setApellidos("Pérez González");
        personaActualizada.setCorreo("juan.actualizado@duocuc.cl");
        personaActualizada.setTelefono("+56987654321");
        personaActualizada.setTipoPersona(tipoPersona);
    }

    @Test
    void testObtenerTodas() {
        // Arrange
        List<Persona> personas = Arrays.asList(persona);
        when(personaRepository.findAll()).thenReturn(personas);

        // Act
        List<Persona> result = personaService.obtenerTodas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("12345678-9", result.get(0).getRut());
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_PersonaExiste() {
        // Arrange
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        // Act
        Optional<Persona> result = personaService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("12345678-9", result.get().getRut());
        assertEquals("Juan Carlos", result.get().getNombres());
        verify(personaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_PersonaNoExiste() {
        // Arrange
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Persona> result = personaService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(personaRepository, times(1)).findById(999L);
    }

    @Test
    void testObtenerPorRut_PersonaExiste() {
        // Arrange
        when(personaRepository.findByRut("12345678-9")).thenReturn(Optional.of(persona));

        // Act
        Optional<Persona> result = personaService.obtenerPorRut("12345678-9");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Carlos", result.get().getNombres());
        verify(personaRepository, times(1)).findByRut("12345678-9");
    }

    @Test
    void testObtenerPorEmail_PersonaExiste() {
        // Arrange
        when(personaRepository.findByCorreoIgnoreCase("juan.perez@duocuc.cl")).thenReturn(Optional.of(persona));

        // Act
        Optional<Persona> result = personaService.obtenerPorEmail("juan.perez@duocuc.cl");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("12345678-9", result.get().getRut());
        verify(personaRepository, times(1)).findByCorreoIgnoreCase("juan.perez@duocuc.cl");
    }

    @Test
    void testCrear_PersonaValida() {
        // Arrange
        when(personaRepository.existsByRut("12345678-9")).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        // Act
        Persona result = personaService.crear(persona);

        // Assert
        assertNotNull(result);
        assertEquals("12345678-9", result.getRut());
        verify(personaRepository, times(1)).existsByRut("12345678-9");
        verify(personaRepository, times(1)).save(persona);
    }

    @Test
    void testCrear_RutDuplicado() {
        // Arrange
        when(personaRepository.existsByRut("12345678-9")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.crear(persona)
        );
        
        assertEquals("Ya existe una persona con el RUT: 12345678-9", exception.getMessage());
        verify(personaRepository, times(1)).existsByRut("12345678-9");
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCrear_RutVacio() {
        // Arrange
        persona.setRut("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.crear(persona)
        );
        
        assertEquals("El RUT es obligatorio", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCrear_NombreVacio() {
        // Arrange
        persona.setNombres("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.crear(persona)
        );
        
        assertEquals("El nombre es obligatorio", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCrear_EmailVacio() {
        // Arrange
        persona.setCorreo("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.crear(persona)
        );
        
        assertEquals("El email es obligatorio", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCrear_TipoPersonaVacio() {
        // Arrange
        persona.setTipoPersona(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.crear(persona)
        );
        
        assertEquals("El tipo de persona es obligatorio", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testActualizar_PersonaExiste() {
        // Arrange
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        // Act
        Optional<Persona> result = personaService.actualizar(1L, personaActualizada);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Carlos Actualizado", persona.getNombres());
        assertEquals("juan.actualizado@duocuc.cl", persona.getCorreo());
        verify(personaRepository, times(1)).findById(1L);
        verify(personaRepository, times(1)).save(persona);
    }

    @Test
    void testActualizar_PersonaNoExiste() {
        // Arrange
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Persona> result = personaService.actualizar(999L, personaActualizada);

        // Assert
        assertFalse(result.isPresent());
        verify(personaRepository, times(1)).findById(999L);
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testEliminar_PersonaExiste() {
        // Arrange
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        // Act
        assertDoesNotThrow(() -> personaService.eliminar(1L));

        // Assert
        verify(personaRepository, times(1)).findById(1L);
        verify(personaRepository, times(1)).delete(persona);
    }

    @Test
    void testEliminar_PersonaNoExiste() {
        // Arrange
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.eliminar(999L)
        );
        
        assertEquals("Persona no encontrada con ID: 999", exception.getMessage());
        verify(personaRepository, times(1)).findById(999L);
        verify(personaRepository, never()).delete(any());
    }

    @Test
    void testBuscarPorNombreOApellido() {
        // Arrange
        List<Persona> personas = Arrays.asList(persona);
        when(personaRepository.buscarPersonas("Juan")).thenReturn(personas);

        // Act
        List<Persona> result = personaService.buscarPorNombreOApellido("Juan");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Carlos", result.get(0).getNombres());
        verify(personaRepository, times(1)).buscarPersonas("Juan");
    }

    @Test
    void testObtenerPorTipo() {
        // Arrange
        List<Persona> personas = Arrays.asList(persona);
        when(personaRepository.findByTipoPersonaId(1L)).thenReturn(personas);

        // Act
        List<Persona> result = personaService.obtenerPorTipo(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(personaRepository, times(1)).findByTipoPersonaId(1L);
    }

    @Test
    void testObtenerEstudiantes() {
        // Arrange
        List<Persona> estudiantes = Arrays.asList(persona);
        when(personaRepository.findEstudiantes()).thenReturn(estudiantes);

        // Act
        List<Persona> result = personaService.obtenerEstudiantes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(personaRepository, times(1)).findEstudiantes();
    }

    @Test
    void testObtenerProfesores() {
        // Arrange
        List<Persona> profesores = Arrays.asList(persona);
        when(personaRepository.findProfesores()).thenReturn(profesores);

        // Act
        List<Persona> result = personaService.obtenerProfesores();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(personaRepository, times(1)).findProfesores();
    }

    @Test
    void testExistePorRut_Existe() {
        // Arrange
        when(personaRepository.existsByRut("12345678-9")).thenReturn(true);

        // Act
        boolean result = personaService.existePorRut("12345678-9");

        // Assert
        assertTrue(result);
        verify(personaRepository, times(1)).existsByRut("12345678-9");
    }

    @Test
    void testExistePorRut_NoExiste() {
        // Arrange
        when(personaRepository.existsByRut("99999999-9")).thenReturn(false);

        // Act
        boolean result = personaService.existePorRut("99999999-9");

        // Assert
        assertFalse(result);
        verify(personaRepository, times(1)).existsByRut("99999999-9");
    }

    @Test
    void testExistePorEmail_Existe() {
        // Arrange
        when(personaRepository.existsByCorreoIgnoreCase("juan.perez@duocuc.cl")).thenReturn(true);

        // Act
        boolean result = personaService.existePorEmail("juan.perez@duocuc.cl");

        // Assert
        assertTrue(result);
        verify(personaRepository, times(1)).existsByCorreoIgnoreCase("juan.perez@duocuc.cl");
    }

    @Test
    void testExistePorEmail_NoExiste() {
        // Arrange
        when(personaRepository.existsByCorreoIgnoreCase("inexistente@duocuc.cl")).thenReturn(false);

        // Act
        boolean result = personaService.existePorEmail("inexistente@duocuc.cl");

        // Assert
        assertFalse(result);
        verify(personaRepository, times(1)).existsByCorreoIgnoreCase("inexistente@duocuc.cl");
    }
}