package cl.duoc.innovatech.bff.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MetricaResumenResponseDTOTest {

    @Test
    @DisplayName("Debería instanciar el Record correctamente y retornar los valores exactos mediante sus métodos de acceso")
    void testConstruccionYAcceso() {
        LocalDateTime ahora = LocalDateTime.now();
        
        MetricaResumenResponseDTO dto = new MetricaResumenResponseDTO(
            1L,
            "ERROR",
            "Gateway-BFF",
            "Fallo de conexión en Microservicio",
            250L,
            101L,
            ahora
        );

        // Verificación de campos obligatoria para JaCoCo
        assertEquals(1L, dto.id());
        assertEquals("ERROR", dto.tipoEvento());
        assertEquals("Gateway-BFF", dto.componente());
        assertEquals("Fallo de conexión en Microservicio", dto.descripcion());
        assertEquals(250L, dto.duracionMs());
        assertEquals(101L, dto.usuarioId());
        assertEquals(ahora, dto.fechaRegistro());
    }

    @Test
    @DisplayName("Debería validar el contrato estructural de equals y hashCode basados en estado")
    void testEqualsYHashCode() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 18, 12, 0);

        MetricaResumenResponseDTO dto1 = new MetricaResumenResponseDTO(1L, "INFO", "Comp", "Desc", 5L, 2L, fecha);
        MetricaResumenResponseDTO dto2 = new MetricaResumenResponseDTO(1L, "INFO", "Comp", "Desc", 5L, 2L, fecha);
        MetricaResumenResponseDTO dto3 = new MetricaResumenResponseDTO(2L, "INFO", "Comp", "Desc", 5L, 2L, fecha);

        assertEquals(dto1, dto2, "Instancias con idéntico estado interno deben ser iguales");
        assertNotEquals(dto1, dto3, "Instancias con distinto ID no deben ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Instancias iguales deben generar idéntico hashCode");
    }

    @Test
    @DisplayName("Debería generar una representación String legible que incluya los atributos clave")
    void testToString() {
        MetricaResumenResponseDTO dto = new MetricaResumenResponseDTO(99L, "WARN", "DB", "Slow query", 1200L, null, LocalDateTime.now());
        String str = dto.toString();

        assertNotNull(str);
        assertTrue(str.contains("id=99"));
        assertTrue(str.contains("tipoEvento=WARN"));
        assertTrue(str.contains("componente=DB"));
        assertTrue(str.contains("descripcion=Slow query"));
        assertTrue(str.contains("duracionMs=1200"));
        assertTrue(str.contains("usuarioId=null"));
    }
}