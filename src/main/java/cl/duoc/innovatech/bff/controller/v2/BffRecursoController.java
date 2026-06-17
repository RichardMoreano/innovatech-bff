package cl.duoc.innovatech.bff.controller.v2;

import cl.duoc.innovatech.bff.client.RecursoClient;
import cl.duoc.innovatech.bff.dto.RecursoRequestDTO;
import cl.duoc.innovatech.bff.dto.RecursoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v2/bff/recursos")
@RequiredArgsConstructor
public class BffRecursoController {

    private final RecursoClient recursoClient;

    @PostMapping
    public ResponseEntity<RecursoResponseDTO> crear(@Valid @RequestBody RecursoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recursoClient.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<RecursoResponseDTO>> listar() {
        return ResponseEntity.ok(recursoClient.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RecursoRequestDTO request) {
        return ResponseEntity.ok(recursoClient.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recursoClient.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}