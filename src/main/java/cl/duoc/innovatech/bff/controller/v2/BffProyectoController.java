package cl.duoc.innovatech.bff.controller.v2;

import cl.duoc.innovatech.bff.client.ProyectoClient;
import cl.duoc.innovatech.bff.dto.ProyectoRequestDTO;
import cl.duoc.innovatech.bff.dto.ProyectoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/bff/proyectos")
public class BffProyectoController {

    private final ProyectoClient proyectoClient;

    public BffProyectoController(ProyectoClient proyectoClient) {
        this.proyectoClient = proyectoClient;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(proyectoClient.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoClient.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crearNuevo(@Valid @RequestBody ProyectoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoClient.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> modificar(
            @PathVariable Long id, 
            @Valid @RequestBody ProyectoRequestDTO request) {
        return ResponseEntity.ok(proyectoClient.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        proyectoClient.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}