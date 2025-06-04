package com.edutech.config;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.time.LocalDate;

import com.edutech.model.*;
import com.edutech.repository.*;

@SpringBootTest
class DataLoaderTest {

    @MockBean
    private EjecucionRepository ejecucionRepository;

    @MockBean
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private DataLoader dataLoader;

    @Test
    void testCreateEvaluaciones_Success() {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Matemáticas Básica");
        curso1.setCodigo("MAT001");

        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombre("Física Avanzada");
        curso2.setCodigo("FIS002");

        Ejecucion ejecucion1 = new Ejecucion();
        ejecucion1.setId(1L);
        ejecucion1.setCurso(curso1);
        ejecucion1.setPeriodo("2024-1");
        ejecucion1.setSeccion("A");
        ejecucion1.setFechaInicio(LocalDate.now());
        ejecucion1.setFechaFin(LocalDate.now().plusDays(90));

        Ejecucion ejecucion2 = new Ejecucion();
        ejecucion2.setId(2L);
        ejecucion2.setCurso(curso2);
        ejecucion2.setPeriodo("2024-2");
        ejecucion2.setSeccion("B");
        ejecucion2.setFechaInicio(LocalDate.now());
        ejecucion2.setFechaFin(LocalDate.now().plusDays(90));

        List<Ejecucion> mockEjecuciones = Arrays.asList(ejecucion1, ejecucion2);
        
        when(ejecucionRepository.findAll()).thenReturn(mockEjecuciones);
        when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        dataLoader.createEvaluaciones();

        // Assert
        // Verify that evaluaciones are saved (3-5 per ejecucion, so 6-10 total for 2 ejecuciones)
        verify(evaluacionRepository, atLeast(6)).save(any(Evaluacion.class));
        verify(evaluacionRepository, atMost(10)).save(any(Evaluacion.class));
        verify(ejecucionRepository, times(1)).findAll();
    }

    @Test
    void testCreateEvaluaciones_VerifyEvaluacionProperties() {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Matemáticas Básica");
        curso1.setCodigo("MAT001");

        Ejecucion ejecucion1 = new Ejecucion();
        ejecucion1.setId(1L);
        ejecucion1.setCurso(curso1);
        ejecucion1.setPeriodo("2024-1");
        ejecucion1.setSeccion("A");
        ejecucion1.setFechaInicio(LocalDate.now());
        ejecucion1.setFechaFin(LocalDate.now().plusDays(90));

        when(ejecucionRepository.findAll()).thenReturn(Collections.singletonList(ejecucion1));
        
        // Capture the evaluacion being saved
        when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(invocation -> {
            Evaluacion evaluacion = invocation.getArgument(0);
            
            // Verify required properties are set
            assertNotNull(evaluacion.getEjecucion());
            assertEquals(ejecucion1, evaluacion.getEjecucion());
            assertNotNull(evaluacion.getTitulo());
            assertNotNull(evaluacion.getDescripcion());
            assertNotNull(evaluacion.getTipo());
            assertNotNull(evaluacion.getFechaInicio());
            assertNotNull(evaluacion.getFechaFin());
            assertNotNull(evaluacion.getDuracionMinutos());
            assertNotNull(evaluacion.getPuntajeTotal());
            
            // Verify specific values
            assertTrue(evaluacion.getDuracionMinutos() >= 60 && evaluacion.getDuracionMinutos() <= 180);
            assertTrue(evaluacion.getPuntajeTotal() >= 50 && evaluacion.getPuntajeTotal() <= 100);
            assertEquals(4.0, evaluacion.getNotaMinimaAprobacion());
            assertEquals(7.0, evaluacion.getNotaMaxima());
            assertEquals(60.0, evaluacion.getExigenciaPorcentual());
            assertTrue(evaluacion.getIntentosPermitidos() >= 1 && evaluacion.getIntentosPermitidos() <= 3);
            assertTrue(evaluacion.getPonderacion() >= 10 && evaluacion.getPonderacion() <= 30);
            assertTrue(evaluacion.getActivo());
            
            // Verify fecha logic
            assertTrue(evaluacion.getFechaFin().isAfter(evaluacion.getFechaInicio()));
            
            return evaluacion;
        });

        // Act
        dataLoader.createEvaluaciones();

        // Assert
        verify(evaluacionRepository, atLeast(3)).save(any(Evaluacion.class));
        verify(evaluacionRepository, atMost(5)).save(any(Evaluacion.class));
    }

    @Test
    void testCreateEvaluaciones_VerifyEvaluacionTypes() {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Matemáticas Básica");

        Ejecucion ejecucion1 = new Ejecucion();
        ejecucion1.setId(1L);
        ejecucion1.setCurso(curso1);
        ejecucion1.setPeriodo("2024-1");
        ejecucion1.setSeccion("A");
        ejecucion1.setFechaInicio(LocalDate.now());
        ejecucion1.setFechaFin(LocalDate.now().plusDays(90));

        when(ejecucionRepository.findAll()).thenReturn(Collections.singletonList(ejecucion1));
        
        when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(invocation -> {
            Evaluacion evaluacion = invocation.getArgument(0);
            
            // Verify tipo is one of the expected values
            String[] validTypes = {"PRUEBA", "TAREA", "PROYECTO", "EXAMEN"};
            assertTrue(Arrays.asList(validTypes).contains(evaluacion.getTipo()));
            
            // Verify titulo contains the tipo
            assertTrue(evaluacion.getTitulo().contains(evaluacion.getTipo()));
            
            return evaluacion;
        });

        // Act
        dataLoader.createEvaluaciones();

        // Assert
        verify(evaluacionRepository, atLeast(3)).save(any(Evaluacion.class));
    }

    @Test
    void testCreateEvaluaciones_EmptyEjecucionesList() {
        // Arrange
        when(ejecucionRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        dataLoader.createEvaluaciones();

        // Assert
        verify(ejecucionRepository, times(1)).findAll();
        verify(evaluacionRepository, never()).save(any(Evaluacion.class));
    }

    @Test
    void testCreateEvaluaciones_SingleEjecucion() {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Matemáticas Básica");

        Ejecucion ejecucion1 = new Ejecucion();
        ejecucion1.setId(1L);
        ejecucion1.setCurso(curso1);
        ejecucion1.setPeriodo("2024-1");
        ejecucion1.setSeccion("A");
        ejecucion1.setFechaInicio(LocalDate.now());
        ejecucion1.setFechaFin(LocalDate.now().plusDays(90));

        when(ejecucionRepository.findAll()).thenReturn(Collections.singletonList(ejecucion1));
        when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        dataLoader.createEvaluaciones();

        // Assert
        // Should create 3-5 evaluaciones for single ejecucion
        verify(evaluacionRepository, atLeast(3)).save(any(Evaluacion.class));
        verify(evaluacionRepository, atMost(5)).save(any(Evaluacion.class));
    }

    @Test
    void testCreateEvaluaciones_MultipleEjecuciones() {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Matemáticas Básica");

        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombre("Física Avanzada");

        Ejecucion ejecucion1 = new Ejecucion();
        ejecucion1.setId(1L);
        ejecucion1.setCurso(curso1);
        ejecucion1.setPeriodo("2024-1");
        ejecucion1.setSeccion("A");

        Ejecucion ejecucion2 = new Ejecucion();
        ejecucion2.setId(2L);
        ejecucion2.setCurso(curso2);
        ejecucion2.setPeriodo("2024-2");
        ejecucion2.setSeccion("B");

        List<Ejecucion> mockEjecuciones = Arrays.asList(ejecucion1, ejecucion2);
        
        when(ejecucionRepository.findAll()).thenReturn(mockEjecuciones);
        when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        dataLoader.createEvaluaciones();

        // Assert
        // Should create 3-5 evaluaciones per ejecucion (2 ejecuciones = 6-10 total)
        verify(evaluacionRepository, atLeast(6)).save(any(Evaluacion.class));
        verify(evaluacionRepository, atMost(10)).save(any(Evaluacion.class));
    }

    @Test
    void testCreateEvaluaciones_RepositoryException() {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Matemáticas Básica");

        Ejecucion ejecucion1 = new Ejecucion();
        ejecucion1.setId(1L);
        ejecucion1.setCurso(curso1);
        ejecucion1.setPeriodo("2024-1");
        ejecucion1.setSeccion("A");
        ejecucion1.setFechaInicio(LocalDate.now());
        ejecucion1.setFechaFin(LocalDate.now().plusDays(90));

        when(ejecucionRepository.findAll()).thenReturn(Collections.singletonList(ejecucion1));
        when(evaluacionRepository.save(any(Evaluacion.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dataLoader.createEvaluaciones());
        verify(ejecucionRepository, times(1)).findAll();
    }
}