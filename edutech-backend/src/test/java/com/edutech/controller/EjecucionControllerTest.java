package com.edutech.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.edutech.model.Ejecucion;
import com.edutech.model.Curso;
import com.edutech.service.EjecucionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebMvcTest(EjecucionController.class)
public class EjecucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EjecucionService ejecucionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Ejecucion ejecucion;

    @BeforeEach
    void setUp() {
        // Crear curso de ejemplo
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Matemáticas Básica");
        
        // Crear ejecución de ejemplo
        ejecucion = new Ejecucion();
        ejecucion.setId(1L);
        ejecucion.setCurso(curso);
        ejecucion.setSeccion("A");
        ejecucion.setPeriodo("2024-1");
        ejecucion.setFechaInicio(LocalDate.now().plusDays(1));
        ejecucion.setFechaFin(LocalDate.now().plusDays(90));
        ejecucion.setCuposDisponibles(30);
        ejecucion.setCapacidadMaxima(30);
    }

    @Test
    public void testObtenerTodas() throws Exception {
        when(ejecucionService.obtenerTodas()).thenReturn(List.of(ejecucion));

        mockMvc.perform(get("/api/ejecuciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].seccion").value("A"))
                .andExpect(jsonPath("$[0].periodo").value("2024-1"));
    }

    @Test
    public void testObtenerPorId_Existe() throws Exception {
        when(ejecucionService.obtenerPorId(1L)).thenReturn(Optional.of(ejecucion));

        mockMvc.perform(get("/api/ejecuciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.seccion").value("A"))
                .andExpect(jsonPath("$.periodo").value("2024-1"));
    }

    @Test
    public void testObtenerPorId_NoExiste() throws Exception {
        when(ejecucionService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ejecuciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrear_Exitoso() throws Exception {
        when(ejecucionService.crear(any(Ejecucion.class))).thenReturn(ejecucion);

        mockMvc.perform(post("/api/ejecuciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ejecucion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.seccion").value("A"));
    }

    @Test
    public void testCrear_Error() throws Exception {
        when(ejecucionService.crear(any(Ejecucion.class)))
                .thenThrow(new IllegalArgumentException("Ejecución duplicada"));

        mockMvc.perform(post("/api/ejecuciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ejecucion)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizar_Exitoso() throws Exception {
        when(ejecucionService.actualizar(eq(1L), any(Ejecucion.class))).thenReturn(ejecucion);

        mockMvc.perform(put("/api/ejecuciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ejecucion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.seccion").value("A"));
    }

    @Test
    public void testActualizar_Error() throws Exception {
        when(ejecucionService.actualizar(eq(999L), any(Ejecucion.class)))
                .thenThrow(new IllegalArgumentException("Ejecución no encontrada"));

        mockMvc.perform(put("/api/ejecuciones/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ejecucion)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEliminar_Exitoso() throws Exception {
        doNothing().when(ejecucionService).eliminar(1L);

        mockMvc.perform(delete("/api/ejecuciones/1"))
                .andExpect(status().isNoContent());

        verify(ejecucionService, times(1)).eliminar(1L);
    }

    @Test
    public void testEliminar_Error() throws Exception {
        doThrow(new IllegalArgumentException("No se puede eliminar"))
                .when(ejecucionService).eliminar(999L);

        mockMvc.perform(delete("/api/ejecuciones/999"))
                .andExpect(status().isBadRequest());
    }
}