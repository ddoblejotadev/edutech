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

import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.repository.EjecucionRepository;
import com.edutech.repository.CursoRepository;

@SpringBootTest
class EjecucionServiceTest {

    // Inyecta el servicio de Ejecución para ser probado
    @Autowired
    private EjecucionService ejecucionService;

    // Crea mocks de los repositorios para simular su comportamiento
    @MockBean
    private EjecucionRepository ejecucionRepository;

    @MockBean
    private CursoRepository cursoRepository;

    @Test
    void testObtenerTodas() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con una Ejecución
        Ejecucion ejecucion = crearEjecucionEjemplo();
        when(ejecucionRepository.findAll()).thenReturn(List.of(ejecucion));

        // Llama al método obtenerTodas() del servicio
        List<Ejecucion> result = ejecucionService.obtenerTodas();

        // Verifica que la lista devuelta no sea nula y contenga exactamente una Ejecución
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getSeccion());
    }

    @Test
    void testObtenerPorId_Existe() {
        // Define el comportamiento del mock: cuando se llame a findById() con 1L, devuelve una Ejecución
        Ejecucion ejecucion = crearEjecucionEjemplo();
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));

        // Llama al método obtenerPorId() del servicio
        Optional<Ejecucion> result = ejecucionService.obtenerPorId(1L);

        // Verifica que la Ejecución devuelta exista y tenga los valores correctos
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getSeccion());
        assertEquals("2024-1", result.get().getPeriodo());
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con 999L, devuelve vacío
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método obtenerPorId() del servicio
        Optional<Ejecucion> result = ejecucionService.obtenerPorId(999L);

        // Verifica que no se encontró ninguna Ejecución
        assertFalse(result.isPresent());
    }

    @Test
    void testCrear_EjecucionValida() {
        // Prepara una ejecución válida para crear
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setFechaInicio(LocalDate.now().plusDays(1)); // Fecha futura válida
        ejecucion.setFechaFin(LocalDate.now().plusDays(60));
        ejecucion.setCuposDisponibles(30);

        // Define el comportamiento de los mocks
        when(cursoRepository.existsById(1L)).thenReturn(true);
        when(ejecucionRepository.existsByCursoIdAndSeccionAndPeriodo(1L, "A", "2024-1")).thenReturn(false);
        when(ejecucionRepository.save(any(Ejecucion.class))).thenReturn(ejecucion);

        // Llama al método crear() del servicio
        Ejecucion result = ejecucionService.crear(ejecucion);

        // Verifica que la ejecución se haya creado correctamente
        assertNotNull(result);
        assertEquals("A", result.getSeccion());
        verify(ejecucionRepository, times(1)).save(ejecucion);
    }

    @Test
    void testCrear_CursoNoExiste() {
        // Prepara una ejecución con curso inexistente
        Ejecucion ejecucion = crearEjecucionEjemplo();
        
        // Define el comportamiento del mock: curso no existe
        when(cursoRepository.existsById(1L)).thenReturn(false);

        // Verifica que se lance una excepción cuando el curso no existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("Curso no encontrado con ID: 1", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_EjecucionDuplicada() {
        // Prepara una ejecución que ya existe
        Ejecucion ejecucion = crearEjecucionEjemplo();
        
        // Define el comportamiento de los mocks: curso existe pero ejecución duplicada
        when(cursoRepository.existsById(1L)).thenReturn(true);
        when(ejecucionRepository.existsByCursoIdAndSeccionAndPeriodo(1L, "A", "2024-1")).thenReturn(true);

        // Verifica que se lance una excepción cuando la ejecución ya existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("Ya existe una ejecución del curso con la misma sección en este período", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_FechaInicioAnteriorAHoy() {
        // Prepara una ejecución con fecha de inicio en el pasado
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setFechaInicio(LocalDate.now().minusDays(1)); // Fecha pasada (inválida)
        
        // Define el comportamiento del mock: curso existe
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Verifica que se lance una excepción cuando la fecha de inicio es pasada
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("La fecha de inicio no puede ser anterior a la fecha actual", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_FechaInicioMayorQueFin() {
        // Prepara una ejecución con fechas inconsistentes
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setFechaInicio(LocalDate.now().plusDays(60)); // Fecha inicio después de fin
        ejecucion.setFechaFin(LocalDate.now().plusDays(30));
        
        // Define el comportamiento del mock: curso existe
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Verifica que se lance una excepción cuando la fecha de inicio es posterior a la de fin
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("La fecha de inicio no puede ser posterior a la fecha de fin", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_CupoMaximoInvalido() {
        // Prepara una ejecución con cupo inválido (0 o menor)
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setCuposDisponibles(0); // Cupo inválido
        
        // Define el comportamiento del mock: curso existe
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Verifica que se lance una excepción cuando el cupo es 0 o menor
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("El cupo máximo debe ser mayor a 0", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testCrear_CupoMaximoExcedeLimite() {
        // Prepara una ejecución con cupo excesivo
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setCuposDisponibles(150); // Cupo excesivo (límite es 100)
        
        // Define el comportamiento del mock: curso existe
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Verifica que se lance una excepción cuando el cupo excede el límite
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.crear(ejecucion)
        );
        
        assertEquals("El cupo máximo no puede exceder 100 estudiantes", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testActualizar_EjecucionExiste() {
        // Prepara los datos de prueba
        Ejecucion ejecucionExistente = crearEjecucionEjemplo();
        Ejecucion ejecucionActualizada = new Ejecucion();
        ejecucionActualizada.setCurso(ejecucionExistente.getCurso());
        ejecucionActualizada.setSeccion("B");
        ejecucionActualizada.setPeriodo("2024-2");
        ejecucionActualizada.setFechaInicio(LocalDate.now().plusDays(1));
        ejecucionActualizada.setFechaFin(LocalDate.now().plusDays(60));
        ejecucionActualizada.setCuposDisponibles(25);

        // Define el comportamiento de los mocks
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucionExistente));
        when(cursoRepository.existsById(1L)).thenReturn(true);
        when(ejecucionRepository.save(any(Ejecucion.class))).thenReturn(ejecucionExistente);

        // Llama al método actualizar() del servicio
        Ejecucion result = ejecucionService.actualizar(1L, ejecucionActualizada);

        // Verifica que la actualización fue exitosa
        assertNotNull(result);
        assertEquals("B", ejecucionExistente.getSeccion());
        assertEquals("2024-2", ejecucionExistente.getPeriodo());
        verify(ejecucionRepository, times(1)).save(ejecucionExistente);
    }

    @Test
    void testActualizar_EjecucionNoExiste() {
        // Prepara datos de prueba
        Ejecucion ejecucionActualizada = new Ejecucion();
        ejecucionActualizada.setSeccion("B");
        
        // Define el comportamiento del mock: ejecución no encontrada
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Verifica que se lance una excepción cuando la ejecución no existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.actualizar(999L, ejecucionActualizada)
        );
        
        assertEquals("Ejecución no encontrada con ID: 999", exception.getMessage());
        verify(ejecucionRepository, never()).save(any());
    }

    @Test
    void testEliminar_EjecucionExiste() {
        // Prepara una ejecución sin inscripciones
        Ejecucion ejecucion = crearEjecucionEjemplo();
        
        // Define el comportamiento de los mocks
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(0); // Sin inscripciones
        doNothing().when(ejecucionRepository).delete(ejecucion);

        // Llama al método eliminar() del servicio
        assertDoesNotThrow(() -> ejecucionService.eliminar(1L));

        // Verifica que la eliminación fue exitosa
        verify(ejecucionRepository, times(1)).delete(ejecucion);
    }

    @Test
    void testEliminar_EjecucionNoExiste() {
        // Define el comportamiento del mock: ejecución no encontrada
        when(ejecucionRepository.findById(999L)).thenReturn(Optional.empty());

        // Verifica que se lance una excepción cuando la ejecución no existe
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ejecucionService.eliminar(999L)
        );
        
        assertEquals("Ejecución no encontrada con ID: 999", exception.getMessage());
        verify(ejecucionRepository, never()).delete(any());
    }

    @Test
    void testObtenerPorCurso() {
        // Define el comportamiento del mock: devuelve ejecuciones de un curso
        Ejecucion ejecucion = crearEjecucionEjemplo();
        when(ejecucionRepository.findByCursoId(1L)).thenReturn(List.of(ejecucion));

        // Llama al método obtenerPorCurso() del servicio
        List<Ejecucion> result = ejecucionService.obtenerPorCurso(1L);

        // Verifica que se devuelvan las ejecuciones del curso
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getSeccion());
    }

    @Test
    void testObtenerActivas() {
        // Define el comportamiento del mock: devuelve ejecuciones activas
        Ejecucion ejecucion = crearEjecucionEjemplo();
        when(ejecucionRepository.findEjecucionesActivas()).thenReturn(List.of(ejecucion));

        // Llama al método obtenerActivas() del servicio
        List<Ejecucion> result = ejecucionService.obtenerActivas();

        // Verifica que se devuelvan las ejecuciones activas
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testContarEstudiantesInscritos() {
        // Define el comportamiento del mock: devuelve el número de estudiantes inscritos
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(15);

        // Llama al método contarEstudiantesInscritos() del servicio
        Integer result = ejecucionService.contarEstudiantesInscritos(1L);

        // Verifica que se devuelva el número correcto
        assertEquals(15, result);
    }

    @Test
    void testTieneCuposDisponibles_ConCupos() {
        // Prepara una ejecución con cupos disponibles
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setCuposDisponibles(30); // Capacidad máxima 30
        
        // Define el comportamiento de los mocks: 20 inscritos de 30 cupos
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(20);

        // Llama al método tieneCuposDisponibles() del servicio
        boolean result = ejecucionService.tieneCuposDisponibles(1L);

        // Verifica que hay cupos disponibles
        assertTrue(result);
    }

    @Test
    void testTieneCuposDisponibles_SinCupos() {
        // Prepara una ejecución sin cupos disponibles
        Ejecucion ejecucion = crearEjecucionEjemplo();
        ejecucion.setCuposDisponibles(30); // Capacidad máxima 30
        
        // Define el comportamiento de los mocks: 30 inscritos de 30 cupos (lleno)
        when(ejecucionRepository.findById(1L)).thenReturn(Optional.of(ejecucion));
        when(ejecucionRepository.countEstudiantesInscritos(1L)).thenReturn(30);

        // Llama al método tieneCuposDisponibles() del servicio
        boolean result = ejecucionService.tieneCuposDisponibles(1L);

        // Verifica que no hay cupos disponibles
        assertFalse(result);
    }

    // ===== MÉTODOS AUXILIARES PARA CREAR OBJETOS DE PRUEBA =====
    
    private Ejecucion crearEjecucionEjemplo() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");

        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        ejecucion.setFechaInicio(LocalDate.now().plusDays(1));
        ejecucion.setFechaFin(LocalDate.now().plusDays(90));
        ejecucion.setCuposDisponibles(30);
        ejecucion.setCapacidadMaxima(30);
        return ejecucion;
    }
}