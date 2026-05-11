package cl.duoc.innovatech.bff.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoResumen {

    private Long recursoId;
    private String nombreCompleto;
    private String rol;
    private String disponibilidad;
}
