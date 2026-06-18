package cl.duoc.innovatech.bff.dto;

import java.util.List;

public record DashboardAnaliticoResponseDTO(
    long totalEventos,
    long totalErrores,
    double promedioLatenciaMs,
    List<MetricaResumenResponseDTO> ultimosEventos
) {}