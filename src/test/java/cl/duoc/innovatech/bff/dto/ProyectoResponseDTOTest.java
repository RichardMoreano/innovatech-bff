package cl.duoc.innovatech.bff.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProyectoResponseDTOTest {

    @Test
    @DisplayName("Debería instanciar el Record de respuesta y acceder a sus propiedades correctamente")
    void testConstruccionYAcceso() {
        ProyectoResponseDTO dto = new ProyectoResponseDTO(100L, "Portal BFF", "Módulo BFF", "COMPLETADO");

        assertEquals(100L, dto.id());
        assertEquals("Portal BFF", dto.nombre());
        assertEquals("Módulo BFF", dto.descripcion());
        assertEquals("COMPLETADO", dto.estado());
    }

    @Test
    @DisplayName("Debería validar el comportamiento de equals, hashCode y toString por estado interno")
    void testEqualsHashCodeYToString() {
        ProyectoResponseDTO dto1 = new ProyectoResponseDTO(1L, "A", "B", "C");
        ProyectoResponseDTO dto2 = new ProyectoResponseDTO(1L, "A", "B", "C");
        ProyectoResponseDTO dto3 = new ProyectoResponseDTO(2L, "A", "B", "C");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertTrue(dto1.toString().contains("id=1"));
    }
}