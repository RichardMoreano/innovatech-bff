package cl.duoc.innovatech.bff.infrastructure.controller;

import cl.duoc.innovatech.bff.application.dto.ProyectoConRecursosResponse;
import cl.duoc.innovatech.bff.application.dto.RecursoResumen;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
public class BffController {

    private final RestTemplate restTemplate;

    private final String SERVICIO_PROYECTO_URL =
            "http://localhost:8081/api/proyectos";

    private final String SERVICIO_RECURSO_URL =
            "http://localhost:8082/api/recursos";

    // ==================== PROYECTOS ====================

    @GetMapping("/proyectos")
    @CircuitBreaker(
            name = "projectService",
            fallbackMethod = "proyectosFallback"
    )
    public ResponseEntity<Object> listarProyectos(
            @RequestHeader("Authorization") String token
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        ResponseEntity<Object> response =
                restTemplate.exchange(
                        SERVICIO_PROYECTO_URL,
                        HttpMethod.GET,
                        entity,
                        Object.class
                );

        return ResponseEntity.ok(response.getBody());
    }

    // ==================== RECURSOS ====================

    @GetMapping("/recursos")
    @CircuitBreaker(
            name = "resourceService",
            fallbackMethod = "recursosFallback"
    )
    public ResponseEntity<Object> listarRecursos(
            @RequestHeader("Authorization") String token
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        ResponseEntity<Object> response =
                restTemplate.exchange(
                        SERVICIO_RECURSO_URL,
                        HttpMethod.GET,
                        entity,
                        Object.class
                );

        return ResponseEntity.ok(response.getBody());
    }

    // ==================== ORQUESTACIÓN ====================

    @GetMapping("/proyectos-con-recursos")
    @CircuitBreaker(name = "projectService", fallbackMethod = "proyectosConRecursosFallback")
    public ResponseEntity<List<ProyectoConRecursosResponse>> proyectosConRecursos(
            @RequestHeader("Authorization") String token) {

        // Headers con JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Obtener proyectos
        ResponseEntity<List> proyectosResponse = restTemplate.exchange(
                SERVICIO_PROYECTO_URL,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> proyectos = proyectosResponse.getBody();

        if (proyectos == null || proyectos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Obtener recursos
        ResponseEntity<List> recursosResponse = restTemplate.exchange(
                SERVICIO_RECURSO_URL,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> recursosBody = recursosResponse.getBody();

        final List<Map<String, Object>> recursos =
                recursosBody != null ? recursosBody : Collections.emptyList();

        // Combinar
        List<ProyectoConRecursosResponse> resultado = proyectos.stream()
                .map(p -> {

                    Long responsableId = Long.valueOf(
                            p.get("responsableId").toString()
                    );

                    List<RecursoResumen> recursosAsignados = recursos.stream()
                            .filter(r ->
                                    Long.valueOf(r.get("id").toString())
                                            .equals(responsableId)
                            )
                            .map(r -> RecursoResumen.builder()
                                    .recursoId(Long.valueOf(r.get("id").toString()))
                                    .nombreCompleto(
                                            r.get("nombre") + " " + r.get("apellido")
                                    )
                                    .rol((String) r.get("rol"))
                                    .disponibilidad((String) r.get("disponibilidad"))
                                    .build())
                            .toList();

                    return ProyectoConRecursosResponse.builder()
                            .proyectoId(Long.valueOf(p.get("id").toString()))
                            .nombreProyecto((String) p.get("nombre"))
                            .estado((String) p.get("estado"))
                            .fechaInicio(
                                    p.get("fechaInicio") != null
                                            ? java.time.LocalDateTime.parse(
                                            p.get("fechaInicio").toString().substring(0, 19)
                                    )
                                            : null
                            )
                            .recursosAsignados(recursosAsignados)
                            .build();
                })
                .toList();

        return ResponseEntity.ok(resultado);
    }

    // ==================== FALLBACKS ====================

    public ResponseEntity<Object> proyectosFallback(
            String token,
            Throwable t
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "mensaje",
                        "Servicio Proyecto no disponible",
                        "error",
                        t.getMessage()
                )
        );
    }

    public ResponseEntity<Object> recursosFallback(
            String token,
            Throwable t
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "mensaje",
                        "Servicio Recurso no disponible",
                        "error",
                        t.getMessage()
                )
        );
    }

    public ResponseEntity<List<ProyectoConRecursosResponse>>
    proyectosConRecursosFallback(
            String token,
            Throwable t
    ) {

        List<ProyectoConRecursosResponse> fallback =
                List.of(
                        ProyectoConRecursosResponse
                                .builder()
                                .proyectoId(0L)
                                .nombreProyecto(
                                        "Servicio temporal no disponible")
                                .estado("ERROR")
                                .recursosAsignados(
                                        Collections.emptyList())
                                .build()
                );

        return ResponseEntity.ok(fallback);
    }
}