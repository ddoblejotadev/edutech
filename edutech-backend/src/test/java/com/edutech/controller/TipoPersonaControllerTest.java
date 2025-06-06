package com.edutech.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.edutech.model.TipoPersona;
import com.edutech.service.TipoPersonaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

@WebMvcTest(TipoPersonaController.class)
public class TipoPersonaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoPersonaService tipoPersonaService;

    @Autowired
    private ObjectMapper objectMapper;

    private TipoPersona tipoPersona;

    @BeforeEach
    void setUp() {
        tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        tipoPersona.setDescripcion("Estudiante inscrito en cursos");
        tipoPersona.setActivo(true);
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(tipoPersonaService.obtenerTodos()).thenReturn(List.of(tipoPersona));

        mockMvc.perform(get("/api/tipos-persona"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("ESTUDIANTE"))
                .andExpect(jsonPath("$[0].descripcion").value("Estudiante inscrito en cursos"));
    }

    @Test
    public void testObtenerPorId_Existe() throws Exception {
        when(tipoPersonaService.obtenerPorId(1L)).thenReturn(Optional.of(tipoPersona));

        mockMvc.perform(get("/api/tipos-persona/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ESTUDIANTE"));
    }

    @Test
    public void testObtenerPorId_NoExiste() throws Exception {
        when(tipoPersonaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tipos-persona/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrear_Exitoso() throws Exception {
        when(tipoPersonaService.crear(any(TipoPersona.class))).thenReturn(tipoPersona);

        mockMvc.perform(post("/api/tipos-persona")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoPersona)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ESTUDIANTE"));
    }

    @Test
    public void testCrear_Error() throws Exception {
        when(tipoPersonaService.crear(any(TipoPersona.class)))
                .thenThrow(new IllegalArgumentException("Nombre duplicado"));

        mockMvc.perform(post("/api/tipos-persona")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoPersona)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizar_Exitoso() throws Exception {
        when(tipoPersonaService.actualizar(eq(1L), any(TipoPersona.class))).thenReturn(Optional.of(tipoPersona));

        mockMvc.perform(put("/api/tipos-persona/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoPersona)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ESTUDIANTE"));
    }

    @Test
    public void testActualizar_NoExiste() throws Exception {
        when(tipoPersonaService.actualizar(eq(999L), any(TipoPersona.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tipos-persona/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoPersona)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testEliminar_Exitoso() throws Exception {
        doNothing().when(tipoPersonaService).eliminar(1L);

        mockMvc.perform(delete("/api/tipos-persona/1"))
                .andExpect(status().isNoContent());

        verify(tipoPersonaService, times(1)).eliminar(1L);
    }

    @Test
    public void testEliminar_Error() throws Exception {
        doThrow(new IllegalArgumentException("No se puede eliminar"))
                .when(tipoPersonaService).eliminar(999L);

        mockMvc.perform(delete("/api/tipos-persona/999"))
                .andExpect(status().isBadRequest());
    }
}