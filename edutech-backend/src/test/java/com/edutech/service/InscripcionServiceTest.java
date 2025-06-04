package com.edutech.service;

import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;
import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.model.TipoPersona;
import com.edutech.repository.InscripcionRepository;
import com.edutech.repository.PersonaRepository;
import com.edutech.repository.EjecucionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private EjecucionRepository ejecucionRepository;

    @InjectMocks
    private InscripcionService inscripcionService;

    private Inscripcion inscripcion;
    private Inscripcion inscripcionActualizada;
    private Persona estudiante;
    private Ejecucion ejecucion;
    private Curso curso;
    private TipoPersona tipoPersona;

    @BeforeEach
    void setUp() {
        tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");

        estudiante = new Persona();
        estudiante.setId(1L);
        estudiante.setRut("12345678-9");
        estudiante.setNombres("Juan Carlos");
        estudiante.setTipoPersona(tipoPersona);

        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");

        ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        ejecucion.setFechaInicio(LocalDate.now().plusDays(1));
        ejecucion.setFechaFin(LocalDate.now().plusDays(60));

        inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setEstudiante(estudiante);
        inscripcion.setEjecucion(ejecucion);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado("ACTIVA");
        inscripcion.setActivo(true);

        inscripcionActualizada = new Inscripcion();
        inscripcionActualizada.setEstudiante(estudiante);
        inscripcionActualizada.setEjecucion(ejecucion);
        inscripcionActualizada.setEstado("CANCELADA");
        inscripcionActualizada.setActivo(false);
    }

    @Test
    void testObtenerTodas() {
        // Arrange
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
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

        // Act
        Optional<Inscripcion> result = inscripcionService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ACTIVA", result.get().getEstado());
        assertEquals("12345678-9", result.get().getEstudiante().getRut());
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
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // Act
        Inscripcion result = inscripcionService.crear(inscripcion);

        // Assert
        assertNotNull(result);
        assertEquals("ACTIVA", result.getEstado());
        assertNotNull(result.getFechaInscripcion());
        verify(inscripcionRepository, times(1)).save(inscripcion);
    }

    @Test
    void testCrear_EstudianteVacio() {
        // Arrange
        inscripcion.setEstudiante(null);

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
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // Act
        Optional<Inscripcion> result = inscripcionService.actualizar(1L, inscripcionActualizada);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("CANCELADA", inscripcion.getEstado());
        assertFalse(inscripcion.getActivo());
        verify(inscripcionRepository, times(1)).findById(1L);
        verify(inscripcionRepository, times(1)).save(inscripcion);
    }

    @Test
    void testActualizar_InscripcionNoExiste() {
        // Arrange
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
        when(inscripcionRepository.findByPersonaIdAndEjecucionIdAndActivoTrue(1L, 1L))
                .thenReturn(Optional.of(inscripcion));

        // Act
        assertDoesNotThrow(() -> inscripcionService.cancelarInscripcion(1L, 1L));

        // Assert
        assertEquals("CANCELADA", inscripcion.getEstado());
        assertFalse(inscripcion.getActivo());
        verify(inscripcionRepository, times(1)).findByPersonaIdAndEjecucionIdAndActivoTrue(1L, 1L);
        verify(inscripcionRepository, times(1)).save(inscripcion);
    }

    @Test
    void testObtenerPorEstudiante() {
        // Arrange
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
    void testObtenerFuturasDeEstudiante() {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findInscripcionesFuturasByEstudiante(1L)).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerFuturasDeEstudiante(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findInscripcionesFuturasByEstudiante(1L);
    }

    @Test
    void testObtenerPasadasDeEstudiante() {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findInscripcionesPasadasByEstudiante(1L)).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerPasadasDeEstudiante(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findInscripcionesPasadasByEstudiante(1L);
    }

    @Test
    void testObtenerEstudiantesInscritos() {
        // Arrange
        List<Persona> estudiantes = Arrays.asList(estudiante);
        when(inscripcionRepository.findEstudiantesByEjecucionId(1L)).thenReturn(estudiantes);

        // Act
        List<Persona> result = inscripcionService.obtenerEstudiantesInscritos(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findEstudiantesByEjecucionId(1L);
    }

    @Test
    void testObtenerPorCurso() {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findByCursoId(1L)).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerPorCurso(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findByCursoId(1L);
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
    void testContarPorEstudiante() {
        // Arrange
        when(inscripcionRepository.countByPersonaId(1L)).thenReturn(5);

        // Act
        Integer result = inscripcionService.contarPorEstudiante(1L);

        // Assert
        assertEquals(5, result);
        verify(inscripcionRepository, times(1)).countByPersonaId(1L);
    }

    @Test
    void testEstaInscrito() {
        // Arrange
        when(inscripcionRepository.existsByPersonaIdAndEjecucionIdAndActivoTrue(1L, 1L)).thenReturn(true);

        // Act
        boolean result = inscripcionService.estaInscrito(1L, 1L);

        // Assert
        assertTrue(result);
        verify(inscripcionRepository, times(1)).existsByPersonaIdAndEjecucionIdAndActivoTrue(1L, 1L);
    }

    @Test
    void testEstaInscritoEnCurso() {
        // Arrange
        when(inscripcionRepository.existsByPersonaIdAndCursoId(1L, 1L)).thenReturn(true);

        // Act
        boolean result = inscripcionService.estaInscritoEnCurso(1L, 1L);

        // Assert
        assertTrue(result);
        verify(inscripcionRepository, times(1)).existsByPersonaIdAndCursoId(1L, 1L);
    }

    @Test
    void testObtenerUltimas() {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findTop10ByOrderByFechaInscripcionDesc()).thenReturn(inscripciones);

        // Act
        List<Inscripcion> result = inscripcionService.obtenerUltimas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inscripcionRepository, times(1)).findTop10ByOrderByFechaInscripcionDesc();
    }
}