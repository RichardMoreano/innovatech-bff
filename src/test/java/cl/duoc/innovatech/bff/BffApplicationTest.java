package cl.duoc.innovatech.bff;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BffApplicationTest {

    @Test
    @DisplayName("Debería iniciar el contexto de la aplicación Spring Boot a través del método main sin lanzar excepciones")
    void testMainApplicationStart() {
        // Temporarily set properties to disable external infra and embedded server during this test.
        String prevEureka = System.getProperty("eureka.client.enabled");
        String prevCircuit = System.getProperty("spring.cloud.openfeign.circuitbreaker.enabled");
        String prevWebType = System.getProperty("spring.main.web-application-type");

        try {
            System.setProperty("eureka.client.enabled", "false");
            System.setProperty("spring.cloud.openfeign.circuitbreaker.enabled", "false");
            // Avoid starting embedded server when invoking main in tests
            System.setProperty("spring.main.web-application-type", "none");

            // Run the application's main and assert it doesn't throw
            assertDoesNotThrow(() -> BffApplication.main(new String[]{}));
        } finally {
            // Restore previous values so other tests are not affected
            if (prevEureka == null) System.clearProperty("eureka.client.enabled"); else System.setProperty("eureka.client.enabled", prevEureka);
            if (prevCircuit == null) System.clearProperty("spring.cloud.openfeign.circuitbreaker.enabled"); else System.setProperty("spring.cloud.openfeign.circuitbreaker.enabled", prevCircuit);
            if (prevWebType == null) System.clearProperty("spring.main.web-application-type"); else System.setProperty("spring.main.web-application-type", prevWebType);
        }
    }
}