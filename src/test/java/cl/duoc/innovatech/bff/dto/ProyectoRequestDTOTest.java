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

class ProyectoRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería pasar la validación si todos los atributos cumplen con las restricciones")
    void testDtoValido() {
        ProyectoRequestDTO dto = new ProyectoRequestDTO("InnovaTech", "Descripción del proyecto", "ACTIVO");

        assertEquals("InnovaTech", dto.nombre());
        assertEquals("Descripción del proyecto", dto.descripcion());
        assertEquals("ACTIVO", dto.estado());

        Set<ConstraintViolation<ProyectoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No deberían existir violaciones de validación");
    }

    @Test
    @DisplayName("Debería fallar la validación cuando los campos obligatorios están en blanco")
    void testDtoInvalidoCamposVacios() {
        ProyectoRequestDTO dto = new ProyectoRequestDTO(" ", "Descripción", "");

        Set<ConstraintViolation<ProyectoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size(), "Deberían fallar las validaciones de nombre y estado");
    }

    @Test
    @DisplayName("Debería fallar la validación cuando los textos superan el límite máximo de caracteres")
    void testDtoInvalidoPorTamanoMaximo() {
        String nombreLargo = "A".repeat(101);
        String descripcionLarga = "B".repeat(501);
        String estadoLargo = "C".repeat(51);

        ProyectoRequestDTO dto = new ProyectoRequestDTO(nombreLargo, descripcionLarga, estadoLargo);

        Set<ConstraintViolation<ProyectoRequestDTO>> violations = validator.validate(dto);
        assertEquals(3, violations.size(), "Deberían fallar las 3 restricciones de tamaño máximo (@Size)");
    }

    @Test
    @DisplayName("Debería verificar correctamente el contrato de equals, hashCode y toString")
    void testMetodosEstructurales() {
        ProyectoRequestDTO dto1 = new ProyectoRequestDTO("P1", "D1", "ACT");
        ProyectoRequestDTO dto2 = new ProyectoRequestDTO("P1", "D1", "ACT");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertTrue(dto1.toString().contains("nombre=P1"));
    }
}