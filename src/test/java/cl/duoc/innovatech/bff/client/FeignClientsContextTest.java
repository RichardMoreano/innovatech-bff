package cl.duoc.innovatech.bff.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "spring.cloud.openfeign.circuitbreaker.enabled=false",
    "jwt.secret=panconqueso=paltadme2==duocuc2026"
})
class FeignClientsContextTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Debería registrar e inyectar correctamente los clientes Feign en el contexto de Spring")
    void testFeignClientsAreRegistered() {
        // Verificar registro de MonitoreoClient
        assertTrue(applicationContext.containsBean("cl.duoc.innovatech.bff.client.MonitoreoClient"), 
            "El cliente de monitoreo debería estar registrado");
        assertNotNull(applicationContext.getBean(MonitoreoClient.class));

        // Verificar registro de ProyectoClient
        assertTrue(applicationContext.containsBean("cl.duoc.innovatech.bff.client.ProyectoClient"), 
            "El cliente de proyectos debería estar registrado");
        assertNotNull(applicationContext.getBean(ProyectoClient.class));

        // Verificar registro de RecursoClient
        assertTrue(applicationContext.containsBean("cl.duoc.innovatech.bff.client.RecursoClient"), 
            "El cliente de recursos debería estar registrado");
        assertNotNull(applicationContext.getBean(RecursoClient.class));
    }
}