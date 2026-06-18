package cl.duoc.innovatech.bff.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecursoRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería pasar la validación si el recurso contiene datos correctos dentro de los límites de horas")
    void testDtoValido() {
        RecursoRequestDTO dto = new RecursoRequestDTO(
            "Juan", "Pérez", "juan.perez@duocuc.cl", "DEVELOPER", "DISPONIBLE", 40
        );

        assertEquals("Juan", dto.nombre());
        assertEquals("juan.perez@duocuc.cl", dto.email());
        assertEquals(40, dto.horasSemana());

        Set<ConstraintViolation<RecursoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No deberían existir violaciones de validación");
    }

    @Test
    @DisplayName("Debería fallar la validación si el formato del correo electrónico es inválido")
    void testEmailInvalido() {
        RecursoRequestDTO dto = new RecursoRequestDTO(
            "Juan", "Pérez", "email-incorrecto.cl", "DEVELOPER", "DISPONIBLE", 30
        );

        Set<ConstraintViolation<RecursoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El formato del email no es válido.")));
    }

    @Test
    @DisplayName("Debería fallar la validación si las horas semanales están fuera del rango permitido (1-45)")
    void testHorasSemanalesFueraDeRango() {
        RecursoRequestDTO dtoMenor = new RecursoRequestDTO("A", "B", "a@b.com", "R", "D", 0);
        RecursoRequestDTO dtoMayor = new RecursoRequestDTO("A", "B", "a@b.com", "R", "D", 46);

        Set<ConstraintViolation<RecursoRequestDTO>> violationsMenor = validator.validate(dtoMenor);
        Set<ConstraintViolation<RecursoRequestDTO>> violationsMayor = validator.validate(dtoMayor);

        assertTrue(violationsMenor.stream().anyMatch(v -> v.getMessage().equals("Las horas semanales deben ser al menos 1.")));
        assertTrue(violationsMayor.stream().anyMatch(v -> v.getMessage().equals("Las horas semanales no pueden superar las 45 horas.")));
    }

    @Test
    @DisplayName("Debería cumplir con el contrato de equals, hashCode y toString")
    void testMetodosEstructurales() {
        RecursoRequestDTO dto1 = new RecursoRequestDTO("A", "B", "a@b.com", "R", "D", 20);
        RecursoRequestDTO dto2 = new RecursoRequestDTO("A", "B", "a@b.com", "R", "D", 20);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertTrue(dto1.toString().contains("nombre=A"));
    }
}