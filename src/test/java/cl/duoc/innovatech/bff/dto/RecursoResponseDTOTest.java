package cl.duoc.innovatech.bff.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecursoResponseDTOTest {

    @Test
    @DisplayName("Debería instanciar el Record de respuesta y acceder a sus campos de forma transparente")
    void testConstruccionYAcceso() {
        RecursoResponseDTO dto = new RecursoResponseDTO(
            1L, "Carlos", "Soto", "carlos@duocuc.cl", "ANALYST", "ASIGNADO", 45
        );

        assertEquals(1L, dto.id());
        assertEquals("Carlos", dto.nombre());
        assertEquals("Soto", dto.apellido());
        assertEquals("carlos@duocuc.cl", dto.email());
        assertEquals("ANALYST", dto.rol());
        assertEquals("ASIGNADO", dto.disponibilidad());
        assertEquals(45, dto.horasSemana());
    }

    @Test
    @DisplayName("Debería validar la igualdad estructural por estado, hashCode y toString del Record")
    void testEqualsHashCodeYToString() {
        RecursoResponseDTO dto1 = new RecursoResponseDTO(1L, "A", "B", "c@d.com", "R", "D", 10);
        RecursoResponseDTO dto2 = new RecursoResponseDTO(1L, "A", "B", "c@d.com", "R", "D", 10);
        RecursoResponseDTO dto3 = new RecursoResponseDTO(2L, "A", "B", "c@d.com", "R", "D", 10);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertTrue(dto1.toString().contains("id=1"));
    }
}