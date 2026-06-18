package cl.duoc.innovatech.bff.controller.v2;

import cl.duoc.innovatech.bff.client.MonitoreoClient;
import cl.duoc.innovatech.bff.dto.DashboardAnaliticoResponseDTO;
import cl.duoc.innovatech.bff.dto.EventoMetricaRequestDTO;
import cl.duoc.innovatech.bff.dto.MetricaResumenResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.secret=panconqueso=paltadme2==duocuc2026",
    "spring.cloud.openfeign.circuitbreaker.enabled=false",
    "eureka.client.enabled=false"
})
class BffMonitoreoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitoreoClient monitoreoClient;

    @Test
    @DisplayName("Debería retornar el dashboard analítico con estado OK cuando el cliente Feign responde exitosamente")
    void testVerDashboardExitoso() throws Exception {
        DashboardAnaliticoResponseDTO mockResponse = new DashboardAnaliticoResponseDTO(
            100L, 5L, 45.2, Collections.emptyList()
        );
        when(monitoreoClient.obtenerDashboard()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v2/bff/monitoreo/dashboard")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debería registrar el evento correctamente y retornar estado OK cuando el payload es válido")
    void testEmitirEventoExitoso() throws Exception {
        MetricaResumenResponseDTO mockResponse = new MetricaResumenResponseDTO(
            1L, "CLICK", "ComponenteTest", "Detalle", 10L, 99L, LocalDateTime.now()
        );
        when(monitoreoClient.registrarEvento(any(EventoMetricaRequestDTO.class))).thenReturn(mockResponse);

        // JSON estructurado con los campos @NotBlank requeridos por EventoMetricaRequestDTO
        String jsonPayload = """
            {
                "tipoEvento": "CLICK",
                "componente": "BffProyectoController",
                "descripcion": "Click en guardar",
                "duracionMs": 15,
                "usuarioId": 12
            }
            """;

        mockMvc.perform(post("/api/v2/bff/monitoreo/eventos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayload))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debería rechazar la solicitud con BadRequest cuando faltan campos obligatorios en el payload")
    void testEmitirEventoInvalidoPorCamposEnBlanco() throws Exception {
        // Payload inválido: 'tipoEvento' y 'componente' vacíos para gatillar el @Valid
        String jsonPayloadInvalido = """
            {
                "tipoEvento": "",
                "componente": " ",
                "descripcion": "Test de fallo"
            }
            """;

        mockMvc.perform(post("/api/v2/bff/monitoreo/eventos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonPayloadInvalido))
               .andExpect(status().isBadRequest());
    }
}