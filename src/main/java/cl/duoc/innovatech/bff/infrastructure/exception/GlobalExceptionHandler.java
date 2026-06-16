package cl.duoc.innovatech.bff.infrastructure.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleRestClientResponseException(RestClientResponseException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        // mensaje descriptivo para el cliente (usa statusText si está disponible)
        body.put("mensaje", ex.getStatusText() != null ? ex.getStatusText() : ex.getMessage());
        // Algunas implementaciones de RestClientResponseException no exponen el código HTTP directamente.
        // Para mantener compatibilidad y claridad, devolvemos 502 (Bad Gateway) para errores de servicios
        // aguas abajo e incluimos el mensaje en el cuerpo.
        body.put("codigo", 502);
        body.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(502).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        // Manejo por defecto: devolver 500 con mensaje y timestamp
        body.put("mensaje", ex.getMessage());
        body.put("codigo", 500);
        body.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(500).body(body);
    }
}
