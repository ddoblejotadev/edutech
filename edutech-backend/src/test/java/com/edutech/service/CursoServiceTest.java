package com.edutech.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import com.edutech.model.Curso;
import com.edutech.repository.CursoRepository;

@SpringBootTest
class CursoServiceTest {

    // Inyecta el servicio de Curso para ser probado
    @Autowired
    private CursoService cursoService;

    // Crea un mock del repositorio de Curso para simular su comportamiento
    @MockBean
    private CursoRepository cursoRepository;

    @Test
    void testObtenerTodos() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con un Curso
        Curso curso = crearCursoEjemplo();
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        // Llama al método obtenerTodos() del servicio
        List<Curso> result = cursoService.obtenerTodos();

        // Verifica que la lista devuelta no sea nula y contenga exactamente un Curso
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EDU001", result.get(0).getCodigo());
    }

    @Test
    void testObtenerPorId_Existe() {
        // Define el comportamiento del mock: cuando se llame a findById() con 1L, devuelve un Curso
        Curso curso = crearCursoEjemplo();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        // Llama al método obtenerPorId() del servicio
        Optional<Curso> result = cursoService.obtenerPorId(1L);

        // Verifica que el Curso devuelto exista y tenga los valores correctos
        assertTrue(result.isPresent());
        assertEquals("EDU001", result.get().getCodigo());
        assertEquals("Matemáticas Básica", result.get().getNombre());
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // Define el comportamiento del mock: cuando se llame a findById() con 999L, devuelve vacío
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método obtenerPorId() del servicio
        Optional<Curso> result = cursoService.obtenerPorId(999L);

        // Verifica que no se encontró ningún Curso
        assertFalse(result.isPresent());
    }

    @Test
    void testCrear_CursoValido() {
        // Crea un Curso válido para guardar
        Curso curso = crearCursoEjemplo();
        
        // Define el comportamiento del mock: cuando se llame a save(), devuelve el Curso
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Llama al método crear() del servicio
        Curso result = cursoService.crear(curso);

        // Verifica que el Curso se haya creado correctamente
        assertNotNull(result);
        assertEquals("EDU001", result.getCodigo());
        assertEquals("Matemáticas Básica", result.getNombre());
        assertEquals(4, result.getCreditos());
    }

    @Test
    void testActualizar_CursoExiste() {
        // Prepara los datos de prueba
        Curso cursoExistente = crearCursoEjemplo();
        Curso cursoActualizado = new Curso();
        cursoActualizado.setNombre("Física Básica");
        cursoActualizado.setDescripcion("Curso de física para principiantes");
        cursoActualizado.setCreditos(5);
        cursoActualizado.setHorasTeoricas(35);
        cursoActualizado.setHorasPracticas(25);
        cursoActualizado.setTotalHoras(60);
        cursoActualizado.setActivo(true);

        // Define el comportamiento de los mocks
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoExistente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoExistente);

        // Llama al método actualizar() del servicio
        Optional<Curso> result = cursoService.actualizar(1L, cursoActualizado);

        // Verifica que la actualización fue exitosa
        assertTrue(result.isPresent());
        assertEquals("Física Básica", cursoExistente.getNombre());
        assertEquals(5, cursoExistente.getCreditos());
    }

    @Test
    void testActualizar_CursoNoExiste() {
        // Prepara datos de prueba
        Curso cursoActualizado = new Curso();
        cursoActualizado.setNombre("Física Básica");
        
        // Define el comportamiento del mock: Curso no encontrado
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        // Llama al método actualizar() del servicio
        Optional<Curso> result = cursoService.actualizar(999L, cursoActualizado);

        // Verifica que no se actualizó nada
        assertFalse(result.isPresent());
        verify(cursoRepository, never()).save(any());
    }

    @Test
    void testEliminar_CursoExiste() {
        // Define el comportamiento del mock: el Curso existe
        when(cursoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cursoRepository).deleteById(1L);

        // Llama al método eliminar() del servicio
        boolean result = cursoService.eliminar(1L);

        // Verifica que la eliminación fue exitosa
        assertTrue(result);
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_CursoNoExiste() {
        // Define el comportamiento del mock: el Curso no existe
        when(cursoRepository.existsById(999L)).thenReturn(false);

        // Llama al método eliminar() del servicio
        boolean result = cursoService.eliminar(999L);

        // Verifica que no se eliminó nada
        assertFalse(result);
        verify(cursoRepository, never()).deleteById(any());
    }

    @Test
    void testBuscarPorNombre() {
        // Define el comportamiento del mock: busca cursos por nombre
        Curso curso = crearCursoEjemplo();
        when(cursoRepository.findByNombreContainingIgnoreCase("Matemáticas")).thenReturn(List.of(curso));

        // Llama al método buscarPorNombre() del servicio
        List<Curso> result = cursoService.buscarPorNombre("Matemáticas");

        // Verifica que se devuelvan los cursos que contienen el texto buscado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getNombre().contains("Matemáticas"));
    }

    @Test
    void testBuscarPorDescripcion() {
        // Define el comportamiento del mock: busca cursos por descripción
        Curso curso = crearCursoEjemplo();
        when(cursoRepository.findByDescripcionContainingIgnoreCase("matemáticas")).thenReturn(List.of(curso));

        // Llama al método buscarPorDescripcion() del servicio
        List<Curso> result = cursoService.buscarPorDescripcion("matemáticas");

        // Verifica que se devuelvan los cursos que contienen el texto buscado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDescripcion().toLowerCase().contains("matemáticas"));
    }

    @Test
    void testObtenerOrdenadosPorNombre() {
        // Define el comportamiento del mock: devuelve cursos ordenados por nombre
        Curso curso = crearCursoEjemplo();
        when(cursoRepository.findAllByOrderByNombreAsc()).thenReturn(List.of(curso));

        // Llama al método obtenerOrdenadosPorNombre() del servicio
        List<Curso> result = cursoService.obtenerOrdenadosPorNombre();

        // Verifica que se devuelvan los cursos ordenados por nombre
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Matemáticas Básica", result.get(0).getNombre());
    }

    @Test
    void testObtenerPorRangoDuracion() {
        // Define el comportamiento del mock: devuelve cursos en rango de duración
        Curso curso = crearCursoEjemplo();
        when(cursoRepository.findByDuracionHorasBetween(30, 60)).thenReturn(List.of(curso));

        // Llama al método obtenerPorRangoDuracion() del servicio
        List<Curso> result = cursoService.obtenerPorRangoDuracion(30, 60);

        // Verifica que se devuelvan los cursos en el rango especificado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(50), result.get(0).getTotalHoras());
    }

    @Test
    void testExistePorNombre_Existe() {
        // Define el comportamiento del mock: el curso existe
        when(cursoRepository.existsByNombreIgnoreCase("Matemáticas Básica")).thenReturn(true);

        // Llama al método existePorNombre() del servicio
        boolean result = cursoService.existePorNombre("Matemáticas Básica");

        // Verifica que el curso existe
        assertTrue(result);
    }

    @Test
    void testExistePorNombre_NoExiste() {
        // Define el comportamiento del mock: el curso no existe
        when(cursoRepository.existsByNombreIgnoreCase("Curso Inexistente")).thenReturn(false);

        // Llama al método existePorNombre() del servicio
        boolean result = cursoService.existePorNombre("Curso Inexistente");

        // Verifica que el curso no existe
        assertFalse(result);
    }

    // ===== MÉTODOS AUXILIARES PARA CREAR OBJETOS DE PRUEBA =====
    
    private Curso crearCursoEjemplo() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("EDU001");
        curso.setNombre("Matemáticas Básica");
        curso.setDescripcion("Curso de matemáticas para principiantes");
        curso.setCreditos(4);
        curso.setHorasTeoricas(30);
        curso.setHorasPracticas(20);
        curso.setTotalHoras(50);
        curso.setCiclo("2024-1");
        curso.setModalidad("PRESENCIAL");
        curso.setActivo(true);
        return curso;
    }
}