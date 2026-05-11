package cl.duoc.innovatech.bff.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoConRecursosResponse {
    private Long proyectoId;
    private String nombreProyecto;
    private String estado;
    private LocalDateTime fechaInicio;
    private List<RecursoResumen> recursosAsignados;
}

