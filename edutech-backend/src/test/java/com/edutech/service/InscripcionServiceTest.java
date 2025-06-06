package com.edutech.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
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

    // Inyecta el servicio de Inscripción para ser probado
    @Autowired
    private InscripcionService inscripcionService;

    // Crea mocks de los repositorios para simular su comportamiento
    @MockBean
    private InscripcionRepository inscripcionRepository;

    @MockBean
    private PersonaRepository personaRepository;

    @MockBean
    private EjecucionRepository ejecucionRepository;

    @Test
    void testObtenerTodas() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con una Inscripción
        Inscripcion inscripcion = crearInscripcionEjemplo();
        when(inscripcionRepository.findAll()).thenReturn(List.of(inscripcion));

        // Llama al método obtenerTodas() del servicio
        List<Inscripcion> result = inscripcionService.obtenerTodas();

        // Verifica que la lista devuelta no sea nula y contenga exactamente una Inscripción
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACTIVA", result.get(0).getEstado());
    }

    @Test
    void testObtenerPorId_Existe() {
        // Define el comportamiento del mock: cuando se llame a findById() con 1L, devuelve una Inscripción
        Inscripcion inscripcion = crearInscripcionEjemplo();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

        // Llama al método obtenerPorId() del servicio
        Optional<Inscripcion> result = inscripcionService.obtenerPorId(1L);

        // Verifica que la Inscripción devuelta exista y tenga el estado correcto
        assertTrue(result.isPresent());
        assertEquals("ACTIVA", result.get().getEstado());
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con 999L, devuelve vacío
        when(inscripcionRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método obtenerPorId() del servicio
        Optional<Inscripcion> result = inscripcionService.obtenerPorId(999L);

        // Verifica que no se encontró ninguna Inscripción
        assertFalse(result.isPresent());
    }

    @Test
    void testCrear_Exitoso() {
        // Crea una inscripción válida para guardar
        Inscripcion inscripcion = crearInscripcionEjemplo();
        
        // Define el comportamiento del mock: cuando se llame a save(), devuelve la inscripción
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // Llama al método crear() del servicio
        Inscripcion result = inscripcionService.crear(inscripcion);

        // Verifica que la inscripción se haya creado correctamente
        assertNotNull(result);
        assertEquals("ACTIVA", result.getEstado());
        assertNotNull(result.getFechaInscripcion());
    }

    @Test
    void testCrear_SinEstudiante() {
        // Crea una inscripción sin estudiante (inválida)
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setPersona(null);
        inscripcion.setEjecucion(crearEjecucionEjemplo());

        // Verifica que se lance una excepción al intentar crear la inscripción
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.crear(inscripcion)
        );
        
        assertEquals("El estudiante es obligatorio", exception.getMessage());
    }

    @Test
    void testCrear_SinEjecucion() {
        // Crea una inscripción sin ejecución (inválida)
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setPersona(crearPersonaEjemplo());
        inscripcion.setEjecucion(null);

        // Verifica que se lance una excepción al intentar crear la inscripción
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.crear(inscripcion)
        );
        
        assertEquals("La ejecución es obligatoria", exception.getMessage());
    }

    @Test
    void testInscribir_Exitoso() {
        // Prepara los datos de prueba
        Persona estudiante = crearPersonaEjemplo();
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setFechaInicio(LocalDate.now().minusDays(1)); // Fecha pasada (válida)
        Inscripcion inscripcionGuardada = crearInscripcionEjemplo();

        // Define el comportamiento de los mocks
        when(personaRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(inscripcionRepository.existsByPersonaIdAndEjecucionId(1L, 1L)).thenReturn(false);
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcionGuardada);

        // Llama al método inscribir() del servicio
        Inscripcion result = inscripcionService.inscribir(1L, 1L);

        // Verifica que la inscripción se haya creado correctamente
        assertNotNull(result);
        assertEquals("ACTIVA", result.getEstado());
        verify(inscripcionRepository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    void testInscribir_EstudianteNoExiste() {
        // Define el comportamiento del mock: estudiante no encontrado
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // Verifica que se lance una excepción cuando el estudiante no existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.inscribir(999L, 1L)
        );
        
        assertEquals("Estudiante no encontrado", exception.getMessage());
    }

    @Test
    void testInscribir_EjecucionNoExiste() {
        // Prepara datos de prueba
        Persona estudiante = crearPersonaEjemplo();
        
        // Define el comportamiento de los mocks
        when(personaRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Verifica que se lance una excepción cuando la ejecución no existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcionService.inscribir(1L, 999L)
        );
        
        assertEquals("Ejecución no encontrada", exception.getMessage());
    }

    @Test
    void testInscribir_YaInscrito() {
        // Prepara los datos de prueba
        Persona estudiante = crearPersonaEjemplo();
        Ejecucion ejecucion = crearEjecucionEjemplo();

        // Define el comportamiento de los mocks
        when(personaRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(inscripcionRepository.existsByPersonaIdAndEjecucionId(1L, 1L)).thenReturn(true);

        // Verifica que se lance una excepción cuando el estudiante ya está inscrito
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> inscripcionService.inscribir(1L, 1L)
        );
        
        assertEquals("El estudiante ya está inscrito en esta ejecución", exception.getMessage());
    }

    @Test
    void testInscribir_EjecucionConFechaFutura() {
        // Prepara los datos de prueba
        Persona estudiante = crearPersonaEjemplo();
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setFechaInicio(LocalDate.now().plusDays(30)); // Fecha futura

        // Define el comportamiento de los mocks
        when(personaRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(inscripcionRepository.existsByPersonaIdAndEjecucionId(1L, 1L)).thenReturn(false);

        // Verifica que se lance una excepción cuando la ejecución tiene fecha futura
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> inscripcionService.inscribir(1L, 1L)
        );
        
        assertEquals("No se puede inscribir a una ejecución que aún no ha comenzado", exception.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testEliminar_Exitoso() {
        // Define el comportamiento del mock: la inscripción existe
        when(inscripcionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(inscripcionRepository).deleteById(1L);

        // Llama al método eliminar() del servicio
        boolean result = inscripcionService.eliminar(1L);

        // Verifica que la eliminación fue exitosa
        assertTrue(result);
        verify(inscripcionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_NoExiste() {
        // Define el comportamiento del mock: la inscripción no existe
        when(inscripcionRepository.existsById(999L)).thenReturn(false);

        // Llama al método eliminar() del servicio
        boolean result = inscripcionService.eliminar(999L);

        // Verifica que no se eliminó nada
        assertFalse(result);
        verify(inscripcionRepository, never()).deleteById(any());
    }

    // ===== MÉTODOS AUXILIARES PARA CREAR OBJETOS DE PRUEBA =====
    
    private Inscripcion crearInscripcionEjemplo() {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setPersona(crearPersonaEjemplo());
        inscripcion.setEjecucion(crearEjecucionEjemplo());
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado("ACTIVA");
        inscripcion.setActivo(true);
        return inscripcion;
    }

    private Persona crearPersonaEjemplo() {
        TipoPersona tipo = new TipoPersona();
        tipo.setId(1L);
        tipo.setNombre("ESTUDIANTE");

        Persona persona = new Persona();
        persona.setId(1L);
        persona.setRut("12.345.678-9");
        persona.setNombres("Juan Carlos");
        persona.setApellidoPaterno("González");
        persona.setTipoPersona(tipo);
        return persona;
    }

    private Ejecucion crearEjecucionEjemplo() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");

        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        ejecucion.setFechaInicio(LocalDate.now().minusDays(1));
        ejecucion.setFechaFin(LocalDate.now().plusDays(90));
        return ejecucion;
    }
}