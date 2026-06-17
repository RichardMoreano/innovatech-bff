package cl.duoc.innovatech.bff.dto;

import jakarta.validation.constraints.NotBlank;

public record EventoMetricaRequestDTO(
    @NotBlank(message = "El tipo de evento es obligatorio")
    String tipoEvento,
    
    @NotBlank(message = "El componente es obligatorio")
    String componente,
    
    String descripcion,
    Long duracionMs,
    Long usuarioId
) {}