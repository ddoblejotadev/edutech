package com.edutech.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import com.edutech.model.Persona;
import com.edutech.model.TipoPersona;
import com.edutech.repository.PersonaRepository;
import com.edutech.repository.TipoPersonaRepository;

@SpringBootTest
class PersonaServiceTest {

    // Inyecta el servicio de Persona para ser probado
    @Autowired
    private PersonaService personaService;

    // Crea mocks de los repositorios para simular su comportamiento
    @MockBean
    private PersonaRepository personaRepository;

    @MockBean
    private TipoPersonaRepository tipoPersonaRepository;

    @Test
    void testObtenerTodas() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con una Persona
        Persona persona = crearPersonaEjemplo();
        when(personaRepository.findAll()).thenReturn(List.of(persona));

        // Llama al método obtenerTodas() del servicio
        List<Persona> result = personaService.obtenerTodas();

        // Verifica que la lista devuelta no sea nula y contenga exactamente una Persona
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("12.345.678-9", result.get(0).getRut());
    }

    @Test
    void testObtenerPorId_Existe() {
        // Define el comportamiento del mock: cuando se llame a findById() con 1L, devuelve una Persona
        Persona persona = crearPersonaEjemplo();
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        // Llama al método obtenerPorId() del servicio
        Optional<Persona> result = personaService.obtenerPorId(1L);

        // Verifica que la Persona devuelta exista y tenga los valores correctos
        assertTrue(result.isPresent());
        assertEquals("12.345.678-9", result.get().getRut());
        assertEquals("Juan Carlos", result.get().getNombres());
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con 999L, devuelve vacío
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método obtenerPorId() del servicio
        Optional<Persona> result = personaService.obtenerPorId(999L);

        // Verifica que no se encontró ninguna Persona
        assertFalse(result.isPresent());
    }

    @Test
    void testObtenerPorRut_Existe() {
        // Define el comportamiento del mock: cuando se busque por RUT, devuelve una Persona
        Persona persona = crearPersonaEjemplo();
        when(personaRepository.findByRut("12.345.678-9")).thenReturn(Optional.of(persona));

        // Llama al método obtenerPorRut() del servicio
        Optional<Persona> result = personaService.obtenerPorRut("12.345.678-9");

        // Verifica que la Persona devuelta tenga el RUT correcto
        assertTrue(result.isPresent());
        assertEquals("12.345.678-9", result.get().getRut());
    }

    @Test
    void testCrear_PersonaValida() {
        // Crea una Persona válida para guardar
        Persona persona = crearPersonaEjemplo();
        TipoPersona tipoPersona = crearTipoPersonaEjemplo();
        
        // Define el comportamiento de los mocks
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoPersona));
        when(personaRepository.existsByRut("12.345.678-9")).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        // Llama al método crear() del servicio
        Persona result = personaService.crear(persona);

        // Verifica que la Persona se haya creado correctamente
        assertNotNull(result);
        assertEquals("12.345.678-9", result.getRut());
        assertEquals("Juan Carlos", result.getNombres());
        assertEquals("González", result.getApellidoPaterno());
    }

    @Test
    void testCrear_TipoPersonaNoExiste() {
        // Prepara una persona con tipo inexistente
        Persona persona = crearPersonaEjemplo();
        
        // Define el comportamiento del mock: tipo de persona no existe
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica que se lance una excepción cuando el tipo de persona no existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.crear(persona)
        );
        
        assertEquals("Tipo de persona no encontrado", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCrear_RutDuplicado() {
        // Prepara una persona con RUT que ya existe
        Persona persona = crearPersonaEjemplo();
        TipoPersona tipoPersona = crearTipoPersonaEjemplo();
        
        // Define el comportamiento de los mocks: tipo existe pero RUT duplicado
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoPersona));
        when(personaRepository.existsByRut("12.345.678-9")).thenReturn(true);

        // Verifica que se lance una excepción cuando el RUT ya existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaService.crear(persona)
        );
        
        assertEquals("Ya existe una persona con el RUT: 12.345.678-9", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testActualizar_PersonaExiste() {
        // Prepara los datos de prueba
        Persona personaExistente = crearPersonaEjemplo();
        Persona personaActualizada = new Persona();
        personaActualizada.setRut("98.765.432-1");
        personaActualizada.setNombres("María José");
        personaActualizada.setApellidoPaterno("Rodríguez");
        personaActualizada.setApellidoMaterno("Silva");
        personaActualizada.setTipoPersona(crearTipoPersonaEjemplo());

        TipoPersona tipoPersona = crearTipoPersonaEjemplo();

        // Define el comportamiento de los mocks
        when(personaRepository.findById(1L)).thenReturn(Optional.of(personaExistente));
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoPersona));
        when(personaRepository.save(any(Persona.class))).thenReturn(personaExistente);

        // Llama al método actualizar() del servicio
        Optional<Persona> result = personaService.actualizar(1L, personaActualizada);

        // Verifica que la actualización fue exitosa
        assertTrue(result.isPresent());
        assertEquals("98.765.432-1", personaExistente.getRut());
        assertEquals("María José", personaExistente.getNombres());
        assertEquals("Rodríguez", personaExistente.getApellidoPaterno());
    }

    @Test
    void testActualizar_PersonaNoExiste() {
        // Prepara datos de prueba
        Persona personaActualizada = new Persona();
        personaActualizada.setNombres("María José");
        
        // Define el comportamiento del mock: Persona no encontrada
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método actualizar() del servicio
        Optional<Persona> result = personaService.actualizar(999L, personaActualizada);

        // Verifica que no se actualizó nada
        assertFalse(result.isPresent());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testEliminar_PersonaExiste() {
        // Given
        when(personaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(personaRepository).deleteById(1L);

        // When
        personaService.eliminar(1L);

        // Then
        verify(personaRepository).deleteById(1L);
    }

    @Test
    void testEliminar_PersonaNoExiste() {
        // Given
        when(personaRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> personaService.eliminar(1L));
    }

    @Test
    void testObtenerEstudiantes() {
        // Define el comportamiento del mock: devuelve estudiantes
        Persona estudiante = crearPersonaEjemplo();
        when(personaRepository.findEstudiantes()).thenReturn(List.of(estudiante));

        // Llama al método obtenerEstudiantes() del servicio
        List<Persona> result = personaService.obtenerEstudiantes();

        // Verifica que se devuelvan los estudiantes
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ESTUDIANTE", result.get(0).getTipoPersona().getNombre());
    }

    @Test
    void testObtenerProfesores() {
        // Define el comportamiento del mock: devuelve profesores
        Persona profesor = crearPersonaEjemplo();
        TipoPersona tipoProfesor = new TipoPersona();
        tipoProfesor.setNombre("PROFESOR");
        profesor.setTipoPersona(tipoProfesor);
        
        when(personaRepository.findProfesores()).thenReturn(List.of(profesor));

        // Llama al método obtenerProfesores() del servicio
        List<Persona> result = personaService.obtenerProfesores();

        // Verifica que se devuelvan los profesores
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PROFESOR", result.get(0).getTipoPersona().getNombre());
    }

    @Test
    void testBuscarPorNombreOApellido() {
        // Given
        Persona persona = crearPersonaEjemplo();
        when(personaRepository.findByNombresContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCaseOrApellidoMaternoContainingIgnoreCase("Juan", "Juan", "Juan"))
                .thenReturn(List.of(persona));

        // When
        List<Persona> result = personaService.buscarPorNombreOApellido("Juan");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Carlos", result.get(0).getNombres());
    }

    @Test
    void testExistePorRut_Existe() {
        // Define el comportamiento del mock: el RUT existe
        when(personaRepository.existsByRut("12.345.678-9")).thenReturn(true);

        // Llama al método existePorRut() del servicio
        boolean result = personaService.existePorRut("12.345.678-9");

        // Verifica que la persona existe
        assertTrue(result);
    }

    @Test
    void testExistePorRut_NoExiste() {
        // Define el comportamiento del mock: el RUT no existe
        when(personaRepository.existsByRut("99.999.999-9")).thenReturn(false);

        // Llama al método existePorRut() del servicio
        boolean result = personaService.existePorRut("99.999.999-9");

        // Verifica que la persona no existe
        assertFalse(result);
    }

    // ===== MÉTODOS AUXILIARES PARA CREAR OBJETOS DE PRUEBA =====
    
    private Persona crearPersonaEjemplo() {
        TipoPersona tipoPersona = crearTipoPersonaEjemplo();
        
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setRut("12.345.678-9");
        persona.setNombres("Juan Carlos");
        persona.setApellidoPaterno("González");
        persona.setApellidoMaterno("López");
        persona.setEmail("juan.gonzalez@edutech.cl");
        persona.setTelefono("+56 9 12345678");
        persona.setDireccion("Av. Principal 123, Santiago");
        persona.setTipoPersona(tipoPersona);
        persona.setActivo(true);
        return persona;
    }

    private TipoPersona crearTipoPersonaEjemplo() {
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        tipoPersona.setDescripcion("Tipo persona para estudiantes");
        tipoPersona.setActivo(true);
        return tipoPersona;
    }
}