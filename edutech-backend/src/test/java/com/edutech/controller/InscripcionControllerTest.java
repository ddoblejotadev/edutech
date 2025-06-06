package com.edutech.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.edutech.model.Inscripcion;
import com.edutech.model.Persona;
import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.model.TipoPersona;
import com.edutech.service.InscripcionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebMvcTest(InscripcionController.class)
public class InscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscripcionService inscripcionService;

    private Inscripcion inscripcion;

    @BeforeEach
    void setUp() {
        // Crear tipo de persona de ejemplo
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        
        // Crear persona de ejemplo
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setRut("12.345.678-9");
        persona.setNombres("Juan Carlos");
        persona.setTipoPersona(tipoPersona);
        
        // Crear curso de ejemplo
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");
        
        // Crear ejecución de ejemplo
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        
        // Crear inscripción de ejemplo
        inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setPersona(persona);
        inscripcion.setEjecucion(ejecucion);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado("ACTIVA");
        inscripcion.setActivo(true);
    }

    @Test
    public void testObtenerTodas() throws Exception {
        when(inscripcionService.obtenerTodas()).thenReturn(List.of(inscripcion));

        mockMvc.perform(get("/api/inscripciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value("ACTIVA"))
                .andExpect(jsonPath("$[0].persona.rut").value("12.345.678-9"));
    }

    @Test
    public void testObtenerPorId_Existe() throws Exception {
        when(inscripcionService.obtenerPorId(1L)).thenReturn(Optional.of(inscripcion));

        mockMvc.perform(get("/api/inscripciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.persona.rut").value("12.345.678-9"));
    }

    @Test
    public void testObtenerPorId_NoExiste() throws Exception {
        when(inscripcionService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/inscripciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testInscribir_Exitoso() throws Exception {
        when(inscripcionService.inscribir(1L, 1L)).thenReturn(inscripcion);

        mockMvc.perform(post("/api/inscripciones/inscribir/1/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("ACTIVA"));
    }

    @Test
    public void testInscribir_Error() throws Exception {
        when(inscripcionService.inscribir(1L, 1L))
                .thenThrow(new IllegalStateException("Ya está inscrito"));

        mockMvc.perform(post("/api/inscripciones/inscribir/1/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEliminar_Exitoso() throws Exception {
        when(inscripcionService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/inscripciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminar_NoExiste() throws Exception {
        when(inscripcionService.eliminar(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/inscripciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testObtenerPorEstudiante() throws Exception {
        when(inscripcionService.obtenerPorEstudiante(1L)).thenReturn(List.of(inscripcion));

        mockMvc.perform(get("/api/inscripciones/estudiante/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].persona.rut").value("12.345.678-9"));
    }

    @Test
    public void testObtenerPorEjecucion() throws Exception {
        when(inscripcionService.obtenerPorEjecucion(1L)).thenReturn(List.of(inscripcion));

        mockMvc.perform(get("/api/inscripciones/ejecucion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ejecucion.seccion").value("A"));
    }

    @Test
    public void testContarPorEjecucion() throws Exception {
        when(inscripcionService.contarPorEjecucion(1L)).thenReturn(15);

        mockMvc.perform(get("/api/inscripciones/ejecucion/1/contar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(15));
    }

    @Test
    public void testContarPorEstudiante() throws Exception {
        when(inscripcionService.contarPorEstudiante(1L)).thenReturn(3);

        mockMvc.perform(get("/api/inscripciones/estudiante/1/contar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
    }

    @Test
    public void testEstaInscrito_Existe() throws Exception {
        when(inscripcionService.estaInscrito(1L, 1L)).thenReturn(true);

        mockMvc.perform(get("/api/inscripciones/existe/estudiante/1/ejecucion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testEstaInscrito_NoExiste() throws Exception {
        when(inscripcionService.estaInscrito(1L, 1L)).thenReturn(false);

        mockMvc.perform(get("/api/inscripciones/existe/estudiante/1/ejecucion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
}