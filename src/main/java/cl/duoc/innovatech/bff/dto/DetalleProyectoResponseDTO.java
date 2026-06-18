package cl.duoc.innovatech.bff.dto;

import java.util.List;

public record DetalleProyectoResponseDTO(
    Long id,
    String nombre,
    String descripcion,
    String estado,
    List<RecursoResponseDTO> recursosAsignados
) {}