package cl.duoc.innovatech.bff.dto;

import java.time.LocalDateTime;

public record MetricaResumenResponseDTO(
    Long id,
    String tipoEvento,
    String componente,
    String descripcion,
    Long duracionMs,
    Long usuarioId,
    LocalDateTime fechaRegistro
) {}