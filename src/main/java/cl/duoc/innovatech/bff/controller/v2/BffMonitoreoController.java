package cl.duoc.innovatech.bff.controller.v2;

import cl.duoc.innovatech.bff.client.MonitoreoClient;
import cl.duoc.innovatech.bff.dto.DashboardAnaliticoResponseDTO;
import cl.duoc.innovatech.bff.dto.EventoMetricaRequestDTO;
import cl.duoc.innovatech.bff.dto.MetricaResumenResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/bff/monitoreo")
@RequiredArgsConstructor
public class BffMonitoreoController {

    private final MonitoreoClient monitoreoClient;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardAnaliticoResponseDTO> verDashboard() {
        // Se corrige .obtainDashboard() por .obtenerDashboard() según la interfaz Feign
        return ResponseEntity.ok(monitoreoClient.obtenerDashboard()); 
    }

    @PostMapping("/eventos")
    public ResponseEntity<MetricaResumenResponseDTO> emitirEvento(@Valid @RequestBody EventoMetricaRequestDTO request) {
        return ResponseEntity.ok(monitoreoClient.registrarEvento(request));
    }
}