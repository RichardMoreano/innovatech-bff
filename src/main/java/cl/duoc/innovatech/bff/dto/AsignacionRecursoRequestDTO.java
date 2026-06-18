package cl.duoc.innovatech.bff.dto;

import jakarta.validation.constraints.NotNull;

public record AsignacionRecursoRequestDTO(
    @NotNull(message = "El ID del recurso es obligatorio.")
    Long recursoId
) {}