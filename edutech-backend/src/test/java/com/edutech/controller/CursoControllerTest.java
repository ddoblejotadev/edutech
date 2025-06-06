package com.edutech.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.edutech.model.Curso;
import com.edutech.service.CursoService;
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

@WebMvcTest(CursoController.class)
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
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
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(cursoService.obtenerTodos()).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].codigo").value("EDU001"))
                .andExpect(jsonPath("$[0].nombre").value("Matemáticas Básica"));
    }

    @Test
    public void testObtenerPorId_Existe() throws Exception {
        when(cursoService.obtenerPorId(1L)).thenReturn(Optional.of(curso));

        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("EDU001"))
                .andExpect(jsonPath("$.nombre").value("Matemáticas Básica"));
    }

    @Test
    public void testObtenerPorId_NoExiste() throws Exception {
        when(cursoService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cursos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearCurso_Exitoso() throws Exception {
        when(cursoService.crear(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("EDU001"));
    }

    @Test
    public void testCrearCurso_Error() throws Exception {
        when(cursoService.crear(any(Curso.class)))
                .thenThrow(new IllegalArgumentException("Código duplicado"));

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizarCurso_Exitoso() throws Exception {
        when(cursoService.actualizar(eq(1L), any(Curso.class))).thenReturn(Optional.of(curso));

        mockMvc.perform(put("/api/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("EDU001"));
    }

    @Test
    public void testActualizarCurso_NoExiste() throws Exception {
        when(cursoService.actualizar(eq(999L), any(Curso.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/cursos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testEliminarCurso_Exitoso() throws Exception {
        doNothing().when(cursoService).eliminar(1L);

        mockMvc.perform(delete("/api/cursos/1"))
                .andExpect(status().isNoContent());

        verify(cursoService, times(1)).eliminar(1L);
    }

    @Test
    public void testEliminarCurso_Error() throws Exception {
        doThrow(new IllegalArgumentException("No se puede eliminar"))
                .when(cursoService).eliminar(999L);

        mockMvc.perform(delete("/api/cursos/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBuscarPorNombre() throws Exception {
        when(cursoService.buscarPorNombre("Matemáticas")).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/cursos/buscar/nombre/Matemáticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Matemáticas Básica"));
    }

    @Test
    public void testObtenerPorRangoDuracion() throws Exception {
        when(cursoService.obtenerPorRangoDuracion(30, 60)).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/cursos/duracion")
                        .param("duracionMin", "30")
                        .param("duracionMax", "60"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalHoras").value(50));
    }

    @Test
    public void testObtenerOrdenadosPorNombre() throws Exception {
        when(cursoService.obtenerOrdenadosPorNombre()).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/cursos/ordenados/nombre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Matemáticas Básica"));
    }

    @Test
    public void testExistePorNombre_Existe() throws Exception {
        when(cursoService.existePorNombre("Matemáticas Básica")).thenReturn(true);

        mockMvc.perform(get("/api/cursos/existe/nombre/Matemáticas Básica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testExistePorNombre_NoExiste() throws Exception {
        when(cursoService.existePorNombre("Curso Inexistente")).thenReturn(false);

        mockMvc.perform(get("/api/cursos/existe/nombre/Curso Inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
}