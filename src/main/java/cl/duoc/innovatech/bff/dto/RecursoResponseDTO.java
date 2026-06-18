package cl.duoc.innovatech.bff.dto;

public record RecursoResponseDTO(
    Long id,
    String nombre,
    String apellido,
    String email,
    String rol,
    String disponibilidad,
    Integer horasSemana
) {}