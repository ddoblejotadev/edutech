package com.edutech.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import com.edutech.model.TipoPersona;
import com.edutech.repository.TipoPersonaRepository;

@SpringBootTest
class TipoPersonaServiceTest {

    // Inyecta el servicio de TipoPersona para ser probado
    @Autowired
    private TipoPersonaService tipoPersonaService;

    // Crea un mock del repositorio de TipoPersona para simular su comportamiento
    @MockBean
    private TipoPersonaRepository tipoPersonaRepository;

    @Test
    void testObtenerTodos() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con un TipoPersona
        TipoPersona tipoPersona = crearTipoPersonaEjemplo();
        when(tipoPersonaRepository.findAll()).thenReturn(List.of(tipoPersona));

        // Llama al método obtenerTodos() del servicio
        List<TipoPersona> result = tipoPersonaService.obtenerTodos();

        // Verifica que la lista devuelta no sea nula y contenga exactamente un TipoPersona
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ESTUDIANTE", result.get(0).getNombre());
    }

    @Test
    void testObtenerPorId_Existe() {
        // Define el comportamiento del mock: cuando se llame a findById() con 1L, devuelve un TipoPersona
        TipoPersona tipoPersona = crearTipoPersonaEjemplo();
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoPersona));

        // Llama al método obtenerPorId() del servicio
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorId(1L);

        // Verifica que el TipoPersona devuelto exista y tenga el nombre correcto
        assertTrue(result.isPresent());
        assertEquals("ESTUDIANTE", result.get().getNombre());
        assertEquals("Tipo persona para estudiantes", result.get().getDescripcion());
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con 999L, devuelve vacío
        when(tipoPersonaRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método obtenerPorId() del servicio
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorId(999L);

        // Verifica que no se encontró ningún TipoPersona
        assertFalse(result.isPresent());
    }

    @Test
    void testObtenerPorNombre_Existe() {
        // Define el comportamiento del mock: cuando se busque por nombre, devuelve un TipoPersona
        TipoPersona tipoPersona = crearTipoPersonaEjemplo();
        when(tipoPersonaRepository.findByNombreIgnoreCase("ESTUDIANTE")).thenReturn(Optional.of(tipoPersona));

        // Llama al método obtenerPorNombre() del servicio
        Optional<TipoPersona> result = tipoPersonaService.obtenerPorNombre("ESTUDIANTE");

        // Verifica que el TipoPersona devuelto tenga el nombre correcto
        assertTrue(result.isPresent());
        assertEquals("ESTUDIANTE", result.get().getNombre());
    }

    @Test
    void testCrear_TipoValido() {
        // Crea un TipoPersona válido para guardar
        TipoPersona tipoPersona = crearTipoPersonaEjemplo();
        
        // Define el comportamiento del mock: cuando se llame a save(), devuelve el TipoPersona
        when(tipoPersonaRepository.save(any(TipoPersona.class))).thenReturn(tipoPersona);

        // Llama al método crear() del servicio
        TipoPersona result = tipoPersonaService.crear(tipoPersona);

        // Verifica que el TipoPersona se haya creado correctamente
        assertNotNull(result);
        assertEquals("ESTUDIANTE", result.getNombre());
        assertEquals("Tipo persona para estudiantes", result.getDescripcion());
    }

    @Test
    void testActualizar_TipoExiste() {
        // Prepara los datos de prueba
        TipoPersona tipoExistente = crearTipoPersonaEjemplo();
        TipoPersona tipoActualizado = new TipoPersona();
        tipoActualizado.setNombre("PROFESOR");
        tipoActualizado.setDescripcion("Tipo persona para profesores");

        // Define el comportamiento de los mocks
        when(tipoPersonaRepository.findById(1L)).thenReturn(Optional.of(tipoExistente));
        when(tipoPersonaRepository.save(any(TipoPersona.class))).thenReturn(tipoExistente);

        // Llama al método actualizar() del servicio
        Optional<TipoPersona> result = tipoPersonaService.actualizar(1L, tipoActualizado);

        // Verifica que la actualización fue exitosa
        assertTrue(result.isPresent());
        assertEquals("PROFESOR", tipoExistente.getNombre());
        assertEquals("Tipo persona para profesores", tipoExistente.getDescripcion());
    }

    @Test
    void testActualizar_TipoNoExiste() {
        // Prepara datos de prueba
        TipoPersona tipoActualizado = new TipoPersona();
        tipoActualizado.setNombre("PROFESOR");
        
        // Define el comportamiento del mock: TipoPersona no encontrado
        when(tipoPersonaRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método actualizar() del servicio
        Optional<TipoPersona> result = tipoPersonaService.actualizar(999L, tipoActualizado);

        // Verifica que no se actualizó nada
        assertFalse(result.isPresent());
        verify(tipoPersonaRepository, never()).save(any());
    }

    @Test
    void testEliminar_TipoExiste() {
        // Define el comportamiento del mock: el TipoPersona existe
        when(tipoPersonaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(tipoPersonaRepository).deleteById(1L);

        // Llama al método eliminar() del servicio
        assertDoesNotThrow(() -> tipoPersonaService.eliminar(1L));

        // Verifica que el método deleteById() del repositorio se haya llamado una vez
        verify(tipoPersonaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_TipoNoExiste() {
        // Define el comportamiento del mock: el TipoPersona no existe
        when(tipoPersonaRepository.existsById(999L)).thenReturn(false);

        // Verifica que se lance una excepción cuando el TipoPersona no existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tipoPersonaService.eliminar(999L)
        );
        
        assertEquals("Tipo de persona no encontrado con ID: 999", exception.getMessage());
        verify(tipoPersonaRepository, never()).deleteById(any());
    }

    // ===== MÉTODOS AUXILIARES PARA CREAR OBJETOS DE PRUEBA =====
    
    private TipoPersona crearTipoPersonaEjemplo() {
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        tipoPersona.setDescripcion("Tipo persona para estudiantes");
        tipoPersona.setActivo(true);
        return tipoPersona;
    }
}