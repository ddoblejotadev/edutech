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

import com.edutech.model.Persona;
import com.edutech.model.TipoPersona;
import com.edutech.repository.PersonaRepository;

@SpringBootTest
class PersonaServiceTest {

    @MockBean
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaService personaService;

    @Test
    void testObtenerTodas() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");

        Persona persona = new Persona();
        persona.setId(1L);
        persona.setRut("12345678-9");
        persona.setNombres("Juan Carlos");
        persona.setTipoPersona(tipoPersona);
        
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
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setRut("12345678-9");
        persona.setNombres("Juan Carlos");
        
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
    void testObtenerPorRut_PersonaExiste() {
        // Arrange
        Persona persona = new Persona();
        persona.setRut("12345678-9");
        persona.setNombres("Juan Carlos");
        
        when(personaRepository.findByRut("12345678-9")).thenReturn(Optional.of(persona));

        // Act
        Optional<Persona> result = personaService.obtenerPorRut("12345678-9");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Carlos", result.get().getNombres());
        verify(personaRepository, times(1)).findByRut("12345678-9");
    }

    @Test
    void testCrear_PersonaValida() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        
        Persona persona = new Persona();
        persona.setRut("12345678-9");
        persona.setNombres("Juan Carlos");
        persona.setCorreo("juan@test.com");
        persona.setTipoPersona(tipoPersona);
        
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
        Persona persona = new Persona();
        persona.setRut("12345678-9");
        persona.setNombres("Juan Carlos");
        persona.setCorreo("juan@test.com");
        
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
    void testActualizar_PersonaExiste() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        
        Persona personaExistente = new Persona();
        personaExistente.setId(1L);
        personaExistente.setRut("12345678-9");
        personaExistente.setNombres("Juan Carlos");
        personaExistente.setTipoPersona(tipoPersona);
        
        Persona personaActualizada = new Persona();
        personaActualizada.setNombres("Juan Carlos Actualizado");
        personaActualizada.setCorreo("juan.actualizado@test.com");
        personaActualizada.setTipoPersona(tipoPersona);
        
        when(personaRepository.findById(1L)).thenReturn(Optional.of(personaExistente));
        when(personaRepository.save(any(Persona.class))).thenReturn(personaExistente);

        // Act
        Optional<Persona> result = personaService.actualizar(1L, personaActualizada);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Carlos Actualizado", personaExistente.getNombres());
        assertEquals("juan.actualizado@test.com", personaExistente.getCorreo());
        verify(personaRepository, times(1)).findById(1L);
        verify(personaRepository, times(1)).save(personaExistente);
    }

    @Test
    void testObtenerEstudiantes() {
        // Arrange
        Persona estudiante = new Persona();
        estudiante.setRut("12345678-9");
        estudiante.setNombres("Juan Carlos");
        
        List<Persona> estudiantes = Arrays.asList(estudiante);
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
        Persona profesor = new Persona();
        profesor.setRut("98765432-1");
        profesor.setNombres("María González");
        
        List<Persona> profesores = Arrays.asList(profesor);
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
    void testExistePorEmail_Existe() {
        // Arrange
        when(personaRepository.existsByCorreoIgnoreCase("juan@test.com")).thenReturn(true);

        // Act
        boolean result = personaService.existePorEmail("juan@test.com");

        // Assert
        assertTrue(result);
        verify(personaRepository, times(1)).existsByCorreoIgnoreCase("juan@test.com");
    }
}