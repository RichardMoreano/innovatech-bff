package cl.duoc.innovatech.bff.infrastructure.controller;

import cl.duoc.innovatech.bff.application.dto.LoginRequest;
import cl.duoc.innovatech.bff.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        // Simulación de validación de usuario (en un caso real irías a BD o Auth Service)
        if ("admin".equals(request.getUsername()) && "123456".equals(request.getPassword())) {

            String token = jwtUtil.generateToken(request.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", request.getUsername(),
                    "type", "Bearer"
            ));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
    }
}