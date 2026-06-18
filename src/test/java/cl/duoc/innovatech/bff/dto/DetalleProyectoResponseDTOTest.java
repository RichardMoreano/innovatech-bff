package cl.duoc.innovatech.bff.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DetalleProyectoResponseDTOTest {

    @Test
    @DisplayName("Debería construir el Record correctamente y retornar los valores mediante sus métodos de acceso")
    void testConstruccionYAcceso() {
        // Instancia básica de la dependencia interna para evitar problemas de tipos
        RecursoResponseDTO recursoMock = new RecursoResponseDTO(1L, "AWS Instance", "CLOUD", "ACTIVE", "Detail", "Zone-A", 1);
        List<RecursoResponseDTO> recursos = List.of(recursoMock);

        DetalleProyectoResponseDTO dto = new DetalleProyectoResponseDTO(
            10L,
            "InnovaTech BFF",
            "Portal de Integración",
            "EN_DESARROLLO",
            recursos
        );

        assertEquals(10L, dto.id());
        assertEquals("InnovaTech BFF", dto.nombre());
        assertEquals("Portal de Integración", dto.descripcion());
        assertEquals("EN_DESARROLLO", dto.estado());
        assertNotNull(dto.recursosAsignados());
        assertEquals(1, dto.recursosAsignados().size());
    }

    @Test
    @DisplayName("Debería cumplir el contrato de equals y hashCode estructural")
    void testEqualsYHashCode() {
        List<RecursoResponseDTO> vacio = Collections.emptyList();

        DetalleProyectoResponseDTO dto1 = new DetalleProyectoResponseDTO(1L, "P1", "D1", "ACT", vacio);
        DetalleProyectoResponseDTO dto2 = new DetalleProyectoResponseDTO(1L, "P1", "D1", "ACT", vacio);
        DetalleProyectoResponseDTO dto3 = new DetalleProyectoResponseDTO(2L, "P2", "D2", "DES", vacio);

        assertEquals(dto1, dto2, "Instancias con idéntico estado interno deben ser iguales");
        assertNotEquals(dto1, dto3, "Instancias con distinto estado interno no deben ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Instancias iguales deben generar el mismo hashCode");
    }

    @Test
    @DisplayName("Debería generar una representación String válida que contenga los atributos del Record")
    void testToString() {
        DetalleProyectoResponseDTO dto = new DetalleProyectoResponseDTO(1L, "Proyecto Alfa", "Desc", "ACT", Collections.emptyList());
        String toStringStr = dto.toString();

        assertNotNull(toStringStr);
        assertTrue(toStringStr.contains("id=1"));
        assertTrue(toStringStr.contains("nombre=Proyecto Alfa"));
    }
}