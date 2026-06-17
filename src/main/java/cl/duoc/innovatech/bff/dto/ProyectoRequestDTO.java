package cl.duoc.innovatech.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProyectoRequestDTO(
    @NotBlank(message = "El nombre del proyecto no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    String nombre,

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
    String descripcion,

    @NotBlank(message = "El estado del proyecto es obligatorio.")
    @Size(max = 50, message = "El estado no puede superar los 50 caracteres.")
    String estado
) {}