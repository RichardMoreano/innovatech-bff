package cl.duoc.innovatech.bff.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
    "eureka.client.enabled=false"
})
class SecurityConfigTest {

    @Autowired(required = false)
    private SecurityFilterChain securityFilterChain;

    @Test
    @DisplayName("Debería inicializar el filtro de seguridad de forma exitosa dentro del contexto")
    void testSecurityFilterChainBeanExists() {
        assertNotNull(securityFilterChain, "El bean de la cadena de filtros de seguridad debe estar registrado en el contexto de Spring");
    }
}