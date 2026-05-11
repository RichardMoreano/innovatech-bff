package cl.duoc.innovatech.bff.infrastructure.controller;

import cl.duoc.innovatech.bff.application.dto.ProyectoConRecursosResponse;
import cl.duoc.innovatech.bff.application.dto.RecursoResumen;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
public class BffController {

    private final RestTemplate restTemplate;

    private final String SERVICIO_PROYECTO_URL = "http://localhost:8081/api/proyectos";
    private final String SERVICIO_RECURSO_URL = "http://localhost:8082/api/recursos";

    @GetMapping("/proyectos")
    @CircuitBreaker(name = "projectService", fallbackMethod = "proyectosFallback")
    public ResponseEntity<Object> listarProyectos() {
        Object response = restTemplate.getForObject(SERVICIO_PROYECTO_URL, Object.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recursos")
    @CircuitBreaker(name = "resourceService", fallbackMethod = "recursosFallback")
    public ResponseEntity<Object> listarRecursos() {
        Object response = restTemplate.getForObject(SERVICIO_RECURSO_URL, Object.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proyectos-con-recursos")
    @CircuitBreaker(name = "projectService", fallbackMethod = "proyectosConRecursosFallback")
    public ResponseEntity<List<ProyectoConRecursosResponse>> proyectosConRecursos() {
        // Llamada al Servicio Producto
        List<Map<String, Object>> proyectos = restTemplate.getForObject(SERVICIO_PROYECTO_URL, List.class);

        if (proyectos == null || proyectos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Llamada al Servicio Recurso
        List<Map<String, Object>> recursos = restTemplate.getForObject(SERVICIO_RECURSO_URL, List.class);

        // Lógica de combinación
        List<ProyectoConRecursosResponse> resultado = proyectos.stream()
                .map(p -> ProyectoConRecursosResponse.builder()
                        .proyectoId(Long.valueOf(p.get("id").toString()))
                        .nombreProyecto((String) p.get("nombre"))
                        .estado((String) p.get("estado"))
                        .fechaInicio(p.get("fechaInicio") != null ?
                                java.time.LocalDateTime.parse(p.get("fechaInicio").toString().substring(0,19)) : null)
                        .recursosAsignados(List.of(
                                RecursoResumen.builder()
                                        .recursoId(101L)
                                        .nombreCompleto("Juan Pérez")
                                        .rol("DESARROLLADOR")
                                        .disponibilidad("DISPONIBLE")
                                        .build()
                        ))
                        .build())
                .toList();

        return ResponseEntity.ok(resultado);
    }

    // ==================== METODOS FALLBACK ====================

    public ResponseEntity<Object> proyectosFallback(Throwable t) {
        return ResponseEntity.ok(Map.of("mensaje", "Servicio Producto no disponible (Circuit Breaker activado)", "error", t.getMessage()));
    }

    public ResponseEntity<Object> recursosFallback(Throwable t) {
        return ResponseEntity.ok(Map.of("mensaje", "Servicio Recurso no disponible (Circuit Breaker activado)", "error", t.getMessage()));
    }

    public ResponseEntity<List<ProyectoConRecursosResponse>> proyectosConRecursosFallback(Throwable t) {
        List<ProyectoConRecursosResponse> fallback = List.of(
                ProyectoConRecursosResponse.builder()
                        .proyectoId(0L)
                        .nombreProyecto("Servicio temporal no disponible")
                        .estado("ERROR")
                        .recursosAsignados(Collections.emptyList())
                        .build()
        );
        return ResponseEntity.ok(fallback);
    }
}