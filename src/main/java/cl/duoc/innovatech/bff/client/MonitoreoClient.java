package cl.duoc.innovatech.bff.client;

import cl.duoc.innovatech.bff.config.FeignClientConfig;
import cl.duoc.innovatech.bff.dto.DashboardAnaliticoResponseDTO;
import cl.duoc.innovatech.bff.dto.EventoMetricaRequestDTO;
import cl.duoc.innovatech.bff.dto.MetricaResumenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-monitoreo-analitica", url = "http://ms-monitoreo-analitica:8084", configuration = FeignClientConfig.class)
public interface MonitoreoClient {

    @PostMapping("/api/v2/monitoreo/eventos")
    MetricaResumenResponseDTO registrarEvento(@RequestBody EventoMetricaRequestDTO request);

    @GetMapping("/api/v2/monitoreo/dashboard")
    DashboardAnaliticoResponseDTO obtenerDashboard();
}