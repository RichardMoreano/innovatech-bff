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

class EventoMetricaRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería pasar la validación si todos los campos obligatorios están presentes y son válidos")
    void testDtoValido() {
        EventoMetricaRequestDTO dto = new EventoMetricaRequestDTO(
            "CLICK",
            "BffProyectoController",
            "Usuario hizo clic en guardar",
            120L,
            99L
        );

        // Verificación de accesores implícitos para cobertura de JaCoCo
        assertEquals("CLICK", dto.tipoEvento());
        assertEquals("BffProyectoController", dto.componente());
        assertEquals("Usuario hizo clic en guardar", dto.descripcion());
        assertEquals(120L, dto.duracionMs());
        assertEquals(99L, dto.usuarioId());

        Set<ConstraintViolation<EventoMetricaRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No deberían existir violaciones de validación");
    }

    @Test
    @DisplayName("Debería fallar la validación si 'tipoEvento' o 'componente' están en blanco o vacíos")
    void testDtoInvalidoCamposEnBlanco() {
        EventoMetricaRequestDTO dtoInvalido = new EventoMetricaRequestDTO(
            " ", // En blanco
            "",  // Vacío
            "Descripción opcional",
            null,
            null
        );

        Set<ConstraintViolation<EventoMetricaRequestDTO>> violations = validator.validate(dtoInvalido);
        
        assertFalse(violations.isEmpty(), "La validación debe fallar");
        assertEquals(2, violations.size(), "Debería haber exactamente dos violaciones");

        boolean msgTipoEvento = violations.stream().anyMatch(v -> v.getMessage().equals("El tipo de evento es obligatorio"));
        boolean msgComponente = violations.stream().anyMatch(v -> v.getMessage().equals("El componente es obligatorio"));

        assertTrue(msgTipoEvento, "Falta el mensaje de validación para tipoEvento");
        assertTrue(msgComponente, "Falta el mensaje de validación para componente");
    }

    @Test
    @DisplayName("Debería cumplir con el contrato estructural de equals, hashCode y toString")
    void testMetodosEstructurales() {
        EventoMetricaRequestDTO dto1 = new EventoMetricaRequestDTO("ERROR", "Auth", "Fail", 10L, 1L);
        EventoMetricaRequestDTO dto2 = new EventoMetricaRequestDTO("ERROR", "Auth", "Fail", 10L, 1L);
        EventoMetricaRequestDTO dto3 = new EventoMetricaRequestDTO("INFO", "Auth", "Fail", 10L, 1L);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        
        String toStringResult = dto1.toString();
        assertTrue(toStringResult.contains("tipoEvento=ERROR"));
        assertTrue(toStringResult.contains("componente=Auth"));
        assertTrue(toStringResult.contains("descripcion=Fail"));
        assertTrue(toStringResult.contains("duracionMs=10"));
        assertTrue(toStringResult.contains("usuarioId=1"));
    }
}