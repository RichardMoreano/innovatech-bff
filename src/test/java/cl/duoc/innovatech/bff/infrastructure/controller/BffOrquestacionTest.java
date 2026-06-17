package cl.duoc.innovatech.bff.infrastructure.controller;

import cl.duoc.innovatech.bff.application.dto.ProyectoConRecursosResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BffOrquestacionTest {

    @Test
    void testProyectosConRecursosExitoso() throws Exception {
        // Respuesta simulada del servicio de proyectos
        List<Map<String, Object>> proyectos = List.of(
                Map.of("id", 1, "nombre", "P1", "responsableId", 10, "estado", "ACTIVO")
        );

        // Respuesta simulada del servicio de recursos con responsableId coincidente
        List<Map<String, Object>> recursos = List.of(
                Map.of("id", 10, "nombre", "Juan", "apellido", "Perez", "rol", "DEV", "disponibilidad", "FULL")
        );

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        Mockito.when(restTemplate.exchange(eq("http://localhost:8081/api/proyectos"), eq(HttpMethod.GET), any(HttpEntity.class), eq(List.class)))
                .thenReturn(ResponseEntity.ok(proyectos));

        Mockito.when(restTemplate.exchange(eq("http://localhost:8082/api/recursos"), eq(HttpMethod.GET), any(HttpEntity.class), eq(List.class)))
                .thenReturn(ResponseEntity.ok(recursos));

        BffController controller = new BffController(restTemplate);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String token = "Bearer t";

        mockMvc.perform(get("/api/bff/proyectos-con-recursos").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].proyectoId").value(1))
                .andExpect(jsonPath("$[0].recursosAsignados[0].recursoId").value(10))
                .andExpect(jsonPath("$[0].recursosAsignados[0].nombreCompleto").value("Juan Perez"));
    }

    @Test
    void testProyectosConRecursosFalloCircuitBreaker() throws Exception {
                // En lugar de depender de AOP/CircuitBreaker en el entorno de prueba, invocamos el método fallback directamente
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        BffController controller = new BffController(restTemplate);

        String token = "Bearer t";

        var response = controller.proyectosConRecursosFallback(token, new RuntimeException("Service down"));

        assertNotNull(response);
        assertFalse(response.getBody().isEmpty());
        ProyectoConRecursosResponse first = response.getBody().get(0);
        assertEquals(0L, first.getProyectoId());
        assertEquals("ERROR", first.getEstado());
    }
}
