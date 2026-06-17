package cl.duoc.innovatech.bff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecursoRequestDTO(
    @NotBlank(message = "El nombre es obligatorio.")
    String nombre,
    
    @NotBlank(message = "El apellido es obligatorio.")
    String apellido,
    
    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    String email,
    
    @NotBlank(message = "El rol es obligatorio.")
    String rol,
    
    @NotNull(message = "La disponibilidad es obligatoria.")
    Boolean disponibilidad,
    
    @NotNull(message = "Las horas semanales son obligatorias.")
    @Min(value = 1, message = "Las horas semanales deben ser al menos 1.")
    @Max(value = 45, message = "Las horas semanales no pueden superar las 45 horas.")
    Integer horasSemana
) {}