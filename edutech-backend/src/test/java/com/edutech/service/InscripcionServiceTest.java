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
import java.time.LocalDateTime;

import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;
import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.model.TipoPersona;
import com.edutech.repository.InscripcionRepository;
import com.edutech.repository.PersonaRepository;
import com.edutech.repository.EjecucionRepository;

@SpringBootTest
class InscripcionServiceTest {

    @MockBean
    private InscripcionRepository inscripcionRepository;

    @MockBean
    private PersonaRepository personaRepository;

    @MockBean
    private EjecucionRepository ejecucionRepository;

    @Autowired
    private InscripcionService inscripcionService;

    @Test
    void testObtenerTodas() {
        // Arrange
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");

        Persona estudiante = new Persona();
        estudiante.setId(1L);
        estudiante.setRut("12345678-9");
        estudiante.setNombres("Juan Carlos");
        estudiante.setTipoPersona(tipoPersona);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");

        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setPersona(estudiante);
        inscripcion.setEjecucion(ejecucion);
        inscripcion.setEstado("ACTIVA");
        
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findAll()).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerTodas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACTIVA", result.get(0).getEstado());
        verify(inscripcionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_InscripcionExiste() {
        // Arrange
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setEstado("ACTIVA");
        
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

        // Act
        Optional<Inscripcion> result = inscripcionService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ACTIVA", result.get().getEstado());
        verify(inscripcionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_InscripcionNoExiste() {
        // Arrange
        when(inscripcionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Inscripcion> result = inscripcionService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(inscripcionRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear_InscripcionValida() {
        // Arrange
        Persona estudiante = new Persona();
        estudiante.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setPersona(estudiante);
        inscripcion.setEjecucion(ejecucion);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado("ACTIVA");
        
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // Act
        Inscripcion result = inscripcionService.crear(inscripcion);

        // Assert
        assertNotNull(result);
        assertEquals("ACTIVA", result.getEstado());
        verify(inscripcionRepository, times(1)).save(inscripcion);
    }

    @Test
    void testCrear_EstudianteVacio() {
        // Arrange
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setPersona(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.crear(inscripcion)
        );
        
        assertEquals("El estudiante es obligatorio", exception.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testCrear_EjecucionVacia() {
        // Arrange
        Persona estudiante = new Persona();
        estudiante.setId(1L);
        
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setPersona(estudiante);
        inscripcion.setEjecucion(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.crear(inscripcion)
        );
        
        assertEquals("La ejecución es obligatoria", exception.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testActualizar_InscripcionExiste() {
        // Arrange
        Inscripcion inscripcionExistente = new Inscripcion();
        inscripcionExistente.setId(1L);
        inscripcionExistente.setEstado("ACTIVA");
        
        Inscripcion inscripcionActualizada = new Inscripcion();
        inscripcionActualizada.setEstado("CANCELADA");
        
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcionExistente));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcionExistente);

        // Act
        Optional<Inscripcion> result = inscripcionService.actualizar(1L, inscripcionActualizada);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("CANCELADA", inscripcionExistente.getEstado());
        verify(inscripcionRepository, times(1)).findById(1L);
        verify(inscripcionRepository, times(1)).save(inscripcionExistente);
    }

    @Test
    void testActualizar_InscripcionNoExiste() {
        // Arrange
        Inscripcion inscripcionActualizada = new Inscripcion();
        inscripcionActualizada.setEstado("CANCELADA");
        
        when(inscripcionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Inscripcion> result = inscripcionService.actualizar(999L, inscripcionActualizada);

        // Assert
        assertFalse(result.isPresent());
        verify(inscripcionRepository, times(1)).findById(999L);
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testEliminar_InscripcionExiste() {
        // Arrange
        when(inscripcionRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = inscripcionService.eliminar(1L);

        // Assert
        assertTrue(result);
        verify(inscripcionRepository, times(1)).existsById(1L);
        verify(inscripcionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_InscripcionNoExiste() {
        // Arrange
        when(inscripcionRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = inscripcionService.eliminar(999L);

        // Assert
        assertFalse(result);
        verify(inscripcionRepository, times(1)).existsById(999L);
        verify(inscripcionRepository, never()).deleteById(any());
    }

    @Test
    void testInscribir_ExitosoPrimerPaso() {
        // Arrange
        Persona estudiante = new Persona();
        estudiante.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setPersona(estudiante);
        inscripcion.setEjecucion(ejecucion);
        
        when(personaRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(inscripcionRepository.existsByPersonaIdAndEjecucionId(1L, 1L)).thenReturn(false);
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // Act
        Inscripcion result = inscripcionService.inscribir(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(personaRepository, times(1)).findById(1L);
        verify(ejecucionRepository, times(1)).findById(1L);
        verify(inscripcionRepository, times(1)).existsByPersonaIdAndEjecucionId(1L, 1L);
        verify(inscripcionRepository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    void testInscribir_EstudianteNoExiste() {
        // Arrange
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.inscribir(999L, 1L)
        );
        
        assertEquals("Estudiante no encontrado", exception.getMessage());
        verify(personaRepository, times(1)).findById(999L);
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testInscribir_EjecucionNoExiste() {
        // Arrange
        Persona estudiante = new Persona();
        estudiante.setId(1L);
        
        when(personaRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.inscribir(1L, 999L)
        );
        
        assertEquals("Ejecución no encontrada", exception.getMessage());
        verify(ejecucionRepository, times(1)).findById(999L);
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testInscribir_YaInscrito() {
        // Arrange
        Persona estudiante = new Persona();
        estudiante.setId(1L);
        
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        
        when(personaRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(inscripcionRepository.existsByPersonaIdAndEjecucionId(1L, 1L)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> inscripcionService.inscribir(1L, 1L)
        );
        
        assertEquals("El estudiante ya está inscrito en esta ejecución", exception.getMessage());
        verify(inscripcionRepository, times(1)).existsByPersonaIdAndEjecucionId(1L, 1L);
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testCancelarInscripcion_PorId() {
        // Arrange
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setEstado("ACTIVA");
        
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

        // Act
        assertDoesNotThrow(() -> inscripcionService.cancelarInscripcion(1L));

        // Assert
        assertEquals("CANCELADA", inscripcion.getEstado());
        assertFalse(inscripcion.getActivo());
        verify(inscripcionRepository, times(1)).findById(1L);
        verify(inscripcionRepository, times(1)).save(inscripcion);
    }

    @Test
    void testCancelarInscripcion_PorEstudianteYEjecucion() {
        // Arrange
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setEstado("ACTIVA");
        
        when(inscripcionRepository.findByPersonaIdAndEjecucionId(1L, 1L))
                .thenReturn(Optional.of(inscripcion));

        // Act
        assertDoesNotThrow(() -> inscripcionService.cancelarInscripcion(1L, 1L));

        // Assert
        assertEquals("CANCELADA", inscripcion.getEstado());
        assertFalse(inscripcion.getActivo());
        verify(inscripcionRepository, times(1)).findByPersonaIdAndEjecucionId(1L, 1L);
        verify(inscripcionRepository, times(1)).save(inscripcion);
    }

    @Test
    void testObtenerPorEstudiante() {
        // Arrange
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstado("ACTIVA");
        
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findByPersonaId(1L)).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerPorEstudiante(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findByPersonaId(1L);
    }

    @Test
    void testObtenerPorEjecucion() {
        // Arrange
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstado("ACTIVA");
        
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findByEjecucionId(1L)).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerPorEjecucion(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findByEjecucionId(1L);
    }

    @Test
    void testObtenerActivasDeEstudiante() {
        // Arrange
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstado("ACTIVA");
        
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findByPersonaIdAndActivoTrue(1L)).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerActivasDeEstudiante(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findByPersonaIdAndActivoTrue(1L);
    }

    @Test
    void testContarPorEjecucion() {
        // Arrange
        when(inscripcionRepository.countByEjecucionId(1L)).thenReturn(25);

        // Act
        Integer result = inscripcionService.contarPorEjecucion(1L);

        // Assert
        assertEquals(25, result);
        verify(inscripcionRepository, times(1)).countByEjecucionId(1L);
    }

    @Test
    void testEstaInscrito() {
        // Arrange
        when(inscripcionRepository.existsByPersonaIdAndEjecucionId(1L, 1L)).thenReturn(true);

        // Act
        boolean result = inscripcionService.estaInscrito(1L, 1L);

        // Assert
        assertTrue(result);
        verify(inscripcionRepository, times(1)).existsByPersonaIdAndEjecucionId(1L, 1L);
    }

    @Test
    void testEstaInscritoActivo() {
        // Arrange
        when(inscripcionRepository.existsByPersonaIdAndEjecucionIdAndActivoTrue(1L, 1L)).thenReturn(true);

        // Act
        boolean result = inscripcionService.estaInscritoActivo(1L, 1L);

        // Assert
        assertTrue(result);
        verify(inscripcionRepository, times(1)).existsByPersonaIdAndEjecucionIdAndActivoTrue(1L, 1L);
    }

    @Test
    void testContarPorEstudiante() {
        // Arrange
        when(inscripcionRepository.countByPersonaId(1L)).thenReturn(3);

        // Act
        Integer result = inscripcionService.contarPorEstudiante(1L);

        // Assert
        assertEquals(3, result);
        verify(inscripcionRepository, times(1)).countByPersonaId(1L);
    }
}