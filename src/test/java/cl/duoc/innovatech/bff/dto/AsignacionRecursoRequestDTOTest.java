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

class AsignacionRecursoRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería pasar la validación cuando el ID del recurso es válido")
    void testDtoValido() {
        // Instanciación del Record y verificación del método de acceso
        AsignacionRecursoRequestDTO dto = new AsignacionRecursoRequestDTO(15L);
        
        assertEquals(15L, dto.recursoId(), "El getter implícito del Record debería retornar el valor correcto");

        Set<ConstraintViolation<AsignacionRecursoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No deberían existir violaciones de validación para datos correctos");
    }

    @Test
    @DisplayName("Debería fallar la validación con un mensaje explícito cuando el ID del recurso es nulo")
    void testDtoInvalidoPorRecursoIdNulo() {
        AsignacionRecursoRequestDTO dto = new AsignacionRecursoRequestDTO(null);

        Set<ConstraintViolation<AsignacionRecursoRequestDTO>> violations = validator.validate(dto);
        
        assertFalse(violations.isEmpty(), "La validación debería fallar cuando recursoId es null");
        assertEquals(1, violations.size(), "Debería haber exactamente una violación de restricción");
        
        String mensajeEsperado = "El ID del recurso es obligatorio.";
        String mensajeObtenido = violations.iterator().next().getMessage();
        assertEquals(mensajeEsperado, mensajeObtenido, "El mensaje de error de validación debe coincidir con el de la anotación @NotNull");
    }

    @Test
    @DisplayName("Debería verificar la igualdad y el contrato de hashCode inherentes al Record")
    void testContratoEqualsYHashCode() {
        AsignacionRecursoRequestDTO dto1 = new AsignacionRecursoRequestDTO(100L);
        AsignacionRecursoRequestDTO dto2 = new AsignacionRecursoRequestDTO(100L);
        AsignacionRecursoRequestDTO dto3 = new AsignacionRecursoRequestDTO(200L);

        assertEquals(dto1, dto2, "Dos instancias con idénticos valores deberían ser iguales");
        assertNotEquals(dto1, dto3, "Dos instancias con valores distintos no deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Instancias iguales deben generar el mismo hashCode");
    }
}