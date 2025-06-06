package com.edutech.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.edutech.model.Persona;
import com.edutech.model.TipoPersona;
import com.edutech.service.PersonaService;
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

@WebMvcTest(PersonaController.class)
public class PersonaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonaService personaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Persona persona;

    @BeforeEach
    void setUp() {
        // Crear tipo de persona de ejemplo
        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1L);
        tipoPersona.setNombre("ESTUDIANTE");
        
        // Crear persona de ejemplo
        persona = new Persona();
        persona.setId(1L);
        persona.setRut("12.345.678-9");
        persona.setNombres("Juan Carlos");
        persona.setApellidoPaterno("González");
        persona.setApellidoMaterno("López");
        persona.setCorreo("juan.gonzalez@edutech.cl");
        persona.setTelefono("+56 9 12345678");
        persona.setDireccion("Av. Principal 123, Santiago");
        persona.setFechaNacimiento(LocalDate.of(2000, 5, 15));
        persona.setTipoPersona(tipoPersona);
        persona.setActivo(true);
    }

    @Test
    public void testObtenerTodas() throws Exception {
        when(personaService.obtenerTodas()).thenReturn(List.of(persona));

        mockMvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rut").value("12.345.678-9"))
                .andExpect(jsonPath("$[0].nombres").value("Juan Carlos"));
    }

    @Test
    public void testObtenerPorId_Existe() throws Exception {
        when(personaService.obtenerPorId(1L)).thenReturn(Optional.of(persona));

        mockMvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("12.345.678-9"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"));
    }

    @Test
    public void testObtenerPorId_NoExiste() throws Exception {
        when(personaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/personas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testObtenerPorRut_Existe() throws Exception {
        when(personaService.obtenerPorRut("12.345.678-9")).thenReturn(Optional.of(persona));

        mockMvc.perform(get("/api/personas/rut/12.345.678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12.345.678-9"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"));
    }

    @Test
    public void testCrearPersona_Exitoso() throws Exception {
        when(personaService.crear(any(Persona.class))).thenReturn(persona);

        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persona)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("12.345.678-9"));
    }

    @Test
    public void testCrearPersona_Error() throws Exception {
        when(personaService.crear(any(Persona.class)))
                .thenThrow(new IllegalArgumentException("RUT duplicado"));

        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persona)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizarPersona_Exitoso() throws Exception {
        when(personaService.actualizar(eq(1L), any(Persona.class))).thenReturn(Optional.of(persona));

        mockMvc.perform(put("/api/personas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persona)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("12.345.678-9"));
    }

    @Test
    public void testActualizarPersona_NoExiste() throws Exception {
        when(personaService.actualizar(eq(999L), any(Persona.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/personas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persona)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testEliminarPersona_Exitoso() throws Exception {
        doNothing().when(personaService).eliminar(1L);

        mockMvc.perform(delete("/api/personas/1"))
                .andExpect(status().isNoContent());

        verify(personaService, times(1)).eliminar(1L);
    }

    @Test
    public void testEliminarPersona_NoExiste() throws Exception {
        doThrow(new IllegalArgumentException("Persona no encontrada"))
                .when(personaService).eliminar(999L);

        mockMvc.perform(delete("/api/personas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBuscarPorNombreOApellido() throws Exception {
        when(personaService.buscarPorNombreOApellido("Juan")).thenReturn(List.of(persona));

        mockMvc.perform(get("/api/personas/buscar/Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombres").value("Juan Carlos"));
    }

    @Test
    public void testObtenerEstudiantes() throws Exception {
        when(personaService.obtenerEstudiantes()).thenReturn(List.of(persona));

        mockMvc.perform(get("/api/personas/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoPersona.nombre").value("ESTUDIANTE"));
    }

    @Test
    public void testExistePorRut_Existe() throws Exception {
        when(personaService.existePorRut("12.345.678-9")).thenReturn(true);

        mockMvc.perform(get("/api/personas/existe/rut/12.345.678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testExistePorRut_NoExiste() throws Exception {
        when(personaService.existePorRut("99.999.999-9")).thenReturn(false);

        mockMvc.perform(get("/api/personas/existe/rut/99.999.999-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
}