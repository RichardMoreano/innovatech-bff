package cl.duoc.innovatech.bff.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DashboardAnaliticoResponseDTOTest {

    @Test
    @DisplayName("Debería instanciar correctamente el Record y retornar los valores mediante sus métodos de acceso")
    void testGettersYConstruccionRecord() {
        // Mock elemental de la dependencia interna de la lista
        MetricaResumenResponseDTO metricaMock = new MetricaResumenResponseDTO(
            1L,
            "EVENTO_TEST",
            "COMPONENTE_TEST",
            "Descripción de prueba",
            123L,
            42L,
            LocalDateTime.now()
        );
        List<MetricaResumenResponseDTO> listaEventos = List.of(metricaMock);

        // Inicialización mediante constructor canónico obligatorio
        DashboardAnaliticoResponseDTO dto = new DashboardAnaliticoResponseDTO(
            1500L, 
            23L, 
            145.85, 
            listaEventos
        );

        // Aserciones directas sobre los campos para JaCoCo
        assertEquals(1500L, dto.totalEventos(), "El total de eventos debe coincidir");
        assertEquals(23L, dto.totalErrores(), "El total de errores debe coincidir");
        assertEquals(145.85, dto.promedioLatenciaMs(), 0.001, "El promedio de latencia debe coincidir");
        assertNotNull(dto.ultimosEventos(), "La lista de últimos eventos no debe ser nula");
        assertEquals(1, dto.ultimosEventos().size(), "La lista debe contener exactamente un elemento");
    }

    @Test
    @DisplayName("Debería cumplir estrictamente con el contrato de igualdad (equals) y código hash (hashCode)")
    void testContratoEqualsYHashCode() {
        List<MetricaResumenResponseDTO> vacia = Collections.emptyList();

        DashboardAnaliticoResponseDTO dtoA = new DashboardAnaliticoResponseDTO(10L, 0L, 5.0, vacia);
        DashboardAnaliticoResponseDTO dtoB = new DashboardAnaliticoResponseDTO(10L, 0L, 5.0, vacia);
        DashboardAnaliticoResponseDTO dtoC = new DashboardAnaliticoResponseDTO(20L, 1L, 12.5, vacia);

        // Verificaciones de igualdad estructural
        assertEquals(dtoA, dtoB, "Instancias con idéntico estado deben ser declaradas iguales");
        assertNotEquals(dtoA, dtoC, "Instancias con distinto estado no deben ser iguales");
        
        // Verificación de Hash
        assertEquals(dtoA.hashCode(), dtoB.hashCode(), "Instancias iguales deben producir el mismo hashCode");
    }

    @Test
    @DisplayName("Debería retornar una cadena de texto representativa no vacía al invocar toString()")
    void testToStringGenerado() {
        DashboardAnaliticoResponseDTO dto = new DashboardAnaliticoResponseDTO(1L, 0L, 1.0, Collections.emptyList());
        String toStringResult = dto.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("totalEventos=1"));
        assertTrue(toStringResult.contains("totalErrores=0"));
        assertTrue(toStringResult.contains("promedioLatenciaMs=1.0"));
        assertTrue(toStringResult.contains("ultimosEventos=[]"));
    }
}