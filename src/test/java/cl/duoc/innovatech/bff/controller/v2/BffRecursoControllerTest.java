package cl.duoc.innovatech.bff.controller.v2;

import cl.duoc.innovatech.bff.client.RecursoClient;
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
class BffRecursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecursoClient recursoClient;

    @Test
    @DisplayName("POST /api/v2/bff/recursos -> 201 CREATED con DTO válido")
    void testCrearExitoso() throws Exception {
        RecursoResponseDTO mockResponse = new RecursoResponseDTO(1L, "Juan", "Pérez", "juan@duoc.cl", "DEV", "DISPONIBLE", 40);
        when(recursoClient.crear(any())).thenReturn(mockResponse);

        String jsonPayload = """
            {
                "nombre": "Juan",
                "apellido": "Pérez",
                "email": "juan@duoc.cl",
                "rol": "DEV",
                "disponibilidad": "DISPONIBLE",
                "horasSemana": 40
            }
            """;

        mockMvc.perform(post("/api/v2/bff/recursos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayload))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v2/bff/recursos -> 400 BAD REQUEST con DTO inválido")
    void testCrearInvalido() throws Exception {
        String jsonPayloadInvalido = "{\"nombre\":\"\",\"email\":\"invalido\"}";

        mockMvc.perform(post("/api/v2/bff/recursos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayloadInvalido))
               .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v2/bff/recursos -> 200 OK")
    void testListar() throws Exception {
        when(recursoClient.obtenerTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/bff/recursos"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v2/bff/recursos/{id} -> 200 OK con DTO válido")
    void testActualizarExitoso() throws Exception {
        RecursoResponseDTO mockResponse = new RecursoResponseDTO(1L, "Juan", "Pérez", "juan@duoc.cl", "DEV", "DISPONIBLE", 40);
        when(recursoClient.actualizar(eq(1L), any())).thenReturn(mockResponse);

        String jsonPayload = """
            {
                "nombre": "Juan",
                "apellido": "Pérez",
                "email": "juan@duoc.cl",
                "rol": "DEV",
                "disponibilidad": "DISPONIBLE",
                "horasSemana": 40
            }
            """;

        mockMvc.perform(put("/api/v2/bff/recursos/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayload))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/v2/bff/recursos/{id} -> 204 NO CONTENT")
    void testEliminar() throws Exception {
        doNothing().when(recursoClient).eliminar(1L);

        mockMvc.perform(delete("/api/v2/bff/recursos/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/v2/bff/recursos/{id}/disponibilidad -> 200 OK")
    void testActualizarDisponibilidad() throws Exception {
        doNothing().when(recursoClient).actualizarDisponibilidad(1L, "OCUPADO");

        mockMvc.perform(put("/api/v2/bff/recursos/1/disponibilidad")
               .param("disponibilidad", "OCUPADO"))
               .andExpect(status().isOk());
    }
}