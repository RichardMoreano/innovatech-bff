package cl.duoc.innovatech.bff.dto;

public record ProyectoResponseDTO(
    Long id,
    String nombre,
    String descripcion,
    String estado
) {}