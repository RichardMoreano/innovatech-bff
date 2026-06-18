package cl.duoc.innovatech.bff.controller.v2;

import cl.duoc.innovatech.bff.client.ProyectoClient;
import cl.duoc.innovatech.bff.client.RecursoClient;
import cl.duoc.innovatech.bff.dto.ProyectoResponseDTO;
import cl.duoc.innovatech.bff.dto.RecursoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.secret=panconqueso=paltadme2==duocuc2026",
    "spring.cloud.openfeign.circuitbreaker.enabled=false",
    "eureka.client.enabled=false"
})
class BffProyectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProyectoClient proyectoClient;

    @MockBean
    private RecursoClient recursoClient;

    @Test
    @DisplayName("GET /api/v2/bff/proyectos -> 200 OK")
    void testListarTodos() throws Exception {
        when(proyectoClient.obtenerTodos()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v2/bff/proyectos")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v2/bff/proyectos/{id} -> 200 OK")
    void testBuscarPorId() throws Exception {
        ProyectoResponseDTO mockProj = new ProyectoResponseDTO(1L, "Proj", "Desc", "ACTIVO");
        when(proyectoClient.obtenerPorId(1L)).thenReturn(mockProj);

        mockMvc.perform(get("/api/v2/bff/proyectos/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v2/bff/proyectos -> 201 CREATED con JSON válido")
    void testCrearNuevoExitoso() throws Exception {
        ProyectoResponseDTO mockProj = new ProyectoResponseDTO(1L, "InnovaTech", "Desc", "ACTIVO");
        when(proyectoClient.crear(any())).thenReturn(mockProj);

        String jsonPayload = "{\"nombre\":\"InnovaTech\",\"descripcion\":\"Desc\",\"estado\":\"ACTIVO\"}";

        mockMvc.perform(post("/api/v2/bff/proyectos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayload))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v2/bff/proyectos -> 400 BAD REQUEST con JSON inválido")
    void testCrearNuevoInvalido() throws Exception {
        String jsonPayloadInvalido = "{\"nombre\":\"\",\"descripcion\":\"Desc\",\"estado\":\"\"}";

        mockMvc.perform(post("/api/v2/bff/proyectos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayloadInvalido))
               .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/v2/bff/proyectos/{id} -> 200 OK con JSON válido")
    void testModificarExitoso() throws Exception {
        ProyectoResponseDTO mockProj = new ProyectoResponseDTO(1L, "InnovaTech", "Desc", "ACTIVO");
        when(proyectoClient.actualizar(eq(1L), any())).thenReturn(mockProj);

        String jsonPayload = "{\"nombre\":\"InnovaTech\",\"descripcion\":\"Desc\",\"estado\":\"ACTIVO\"}";

        mockMvc.perform(put("/api/v2/bff/proyectos/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayload))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/v2/bff/proyectos/{id} -> 24 NO CONTENT")
    void testBorrar() throws Exception {
        doNothing().when(proyectoClient).eliminar(1L);
        mockMvc.perform(delete("/api/v2/bff/proyectos/1")).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v2/bff/proyectos/{id}/detalles -> 200 OK unificando Proyectos y Recursos")
    void testObtenerDetalleUnificado() throws Exception {
        ProyectoResponseDTO mockProj = new ProyectoResponseDTO(1L, "Proj", "Desc", "ACTIVO");
        RecursoResponseDTO mockRec = new RecursoResponseDTO(99L, "Juan", "Perez", "j@d.cl", "DEV", "OCUPADO", 40);

        when(proyectoClient.obtenerPorId(1L)).thenReturn(mockProj);
        when(proyectoClient.obtenerRecursosIdsPorProyecto(1L)).thenReturn(List.of(99L));
        when(recursoClient.obtenerPorId(99L)).thenReturn(mockRec);

        mockMvc.perform(get("/api/v2/bff/proyectos/1/detalles")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v2/bff/proyectos/{id}/estado -> 200 OK")
    void testCambiarEstado() throws Exception {
        ProyectoResponseDTO mockProj = new ProyectoResponseDTO(1L, "Proj", "Desc", "INACTIVO");
        when(proyectoClient.actualizarEstado(1L, "INACTIVO")).thenReturn(mockProj);

        mockMvc.perform(put("/api/v2/bff/proyectos/1/estado")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"estado\":\"INACTIVO\"}"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v2/bff/proyectos/{id}/recursos -> 200 OK asocia y cambia disponibilidad")
    void testAsignarRecurso() throws Exception {
        doNothing().when(proyectoClient).vincularRecurso(1L, 99L);
        doNothing().when(recursoClient).actualizarDisponibilidad(99L, "OCUPADO");

        mockMvc.perform(post("/api/v2/bff/proyectos/1/recursos")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"recursoId\":99}"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/v2/bff/proyectos/{id}/recursos/{recursoId} -> 24 NO CONTENT")
    void testEliminarRecurso() throws Exception {
        doNothing().when(proyectoClient).desvincularRecurso(1L, 99L);
        doNothing().when(recursoClient).actualizarDisponibilidad(99L, "DISPONIBLE");

        mockMvc.perform(delete("/api/v2/bff/proyectos/1/recursos/99"))
               .andExpect(status().isNoContent());
    }
}