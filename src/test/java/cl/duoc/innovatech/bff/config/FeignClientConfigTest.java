package cl.duoc.innovatech.bff.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FeignClientConfigTest {

    private final FeignClientConfig config = new FeignClientConfig();

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("Debería registrar cabeceras cuando existen atributos de petición, tokens y metadatos de identidad")
    void testInterceptorConCabecerasCompletas() {
        RequestInterceptor interceptor = config.requestTokenBearerInterceptor();
        assertNotNull(interceptor);

        // Mocks del entorno Servlet
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer token-seguro-123");
        when(mockRequest.getHeader("X-User-Id")).thenReturn("99");
        when(mockRequest.getHeader("X-User-Roles")).thenReturn("ROLE_ADMIN");

        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        Map<String, Collection<String>> headers = template.headers();
        
        assertTrue(headers.containsKey("Authorization"));
        assertTrue(headers.containsKey("X-User-Id"));
        assertTrue(headers.containsKey("X-User-Roles"));
        assertEquals("Bearer token-seguro-123", headers.get("Authorization").iterator().next());
    }

    @Test
    @DisplayName("Debería ignorar la mutación de cabeceras si no hay un contexto de petición activo")
    void testInterceptorSinContexto() {
        RequestInterceptor interceptor = config.requestTokenBearerInterceptor();
        RequestContextHolder.setRequestAttributes(null);

        RequestTemplate template = new RequestTemplate();
        assertDoesNotThrow(() -> interceptor.apply(template));
        assertTrue(template.headers().isEmpty());
    }

    @Test
    @DisplayName("Debería omitir cabeceras si están presentes en la petición original pero con valores nulos o formatos inválidos")
    void testInterceptorConCabecerasInvalidasONulas() {
        RequestInterceptor interceptor = config.requestTokenBearerInterceptor();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        
        // Token mal formateado (sin prefijo Bearer) y atributos nulos
        when(mockRequest.getHeader("Authorization")).thenReturn("TokenInvalido 123");
        when(mockRequest.getHeader("X-User-Id")).thenReturn(null);
        when(mockRequest.getHeader("X-User-Roles")).thenReturn(null);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertFalse(template.headers().containsKey("Authorization"));
        assertFalse(template.headers().containsKey("X-User-Id"));
    }
}