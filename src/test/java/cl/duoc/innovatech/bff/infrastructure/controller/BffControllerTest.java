package cl.duoc.innovatech.bff.infrastructure.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

// Probamos que el BffController reenvíe el header Authorization hacia el servicio de Proyectos
class BffControllerTest {

    @Test
    void testListarProyectosReenviaAuthorizationHeader() throws Exception {
        // Preparamos la respuesta mock del servicio de proyectos
        List<Map<String, Object>> proyectos = List.of(Map.of("id", 1, "nombre", "P1", "responsableId", 1));
        ResponseEntity<Object> mockResponse = ResponseEntity.ok(proyectos);

        // Creamos un RestTemplate mock y montamos el controlador en standalone MockMvc
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(mockResponse);

        BffController controller = new BffController(restTemplate);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String token = "Bearer dummy-token";

        // Ejecutamos la petición al controlador
        mockMvc.perform(get("/api/bff/proyectos").header("Authorization", token))
                .andExpect(status().isOk());

        // Capturamos el HttpEntity que el controller pasó al RestTemplate
        @SuppressWarnings("unchecked")
        ArgumentCaptor<HttpEntity<?>> captor = (ArgumentCaptor) ArgumentCaptor.forClass(HttpEntity.class);
        Mockito.verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), captor.capture(), eq(Object.class));

        HttpEntity<?> sentEntity = captor.getValue();
        HttpHeaders sentHeaders = sentEntity.getHeaders();

        // Verificamos que el header Authorization fue reenviado intacto
        assertEquals(token, sentHeaders.getFirst("Authorization"), "El header Authorization debe reenviarse al backend");
    }
}
