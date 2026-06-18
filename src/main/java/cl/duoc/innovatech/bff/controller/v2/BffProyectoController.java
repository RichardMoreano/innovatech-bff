package cl.duoc.innovatech.bff.controller.v2;

import cl.duoc.innovatech.bff.client.ProyectoClient;
import cl.duoc.innovatech.bff.client.RecursoClient;
import cl.duoc.innovatech.bff.dto.AsignacionRecursoRequestDTO;
import cl.duoc.innovatech.bff.dto.DetalleProyectoResponseDTO;
import cl.duoc.innovatech.bff.dto.ProyectoRequestDTO;
import cl.duoc.innovatech.bff.dto.ProyectoResponseDTO;
import cl.duoc.innovatech.bff.dto.RecursoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/bff/proyectos")
@RequiredArgsConstructor 
public class BffProyectoController {

    private final ProyectoClient proyectoClient;
    private final RecursoClient recursoClient;

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

    @GetMapping("/{id}/detalles")
    public ResponseEntity<DetalleProyectoResponseDTO> obtenerDetalleUnificado(@PathVariable Long id) {
        ProyectoResponseDTO proyecto = proyectoClient.obtenerPorId(id);
        List<Long> idsAsignados = proyectoClient.obtenerRecursosIdsPorProyecto(id);
        
        List<RecursoResponseDTO> recursosDetalle = idsAsignados.stream()
                .map(recursoClient::obtenerPorId)
                .toList();
                
        return ResponseEntity.ok(new DetalleProyectoResponseDTO(
                proyecto.id(),
                proyecto.nombre(),
                proyecto.descripcion(),
                proyecto.estado(),
                recursosDetalle
        ));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ProyectoResponseDTO> cambiarEstado(
            @PathVariable Long id, 
            @RequestBody java.util.Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        return ResponseEntity.ok(proyectoClient.actualizarEstado(id, nuevoEstado));
    }

    @PostMapping("/{id}/recursos")
    public ResponseEntity<Void> asignarRecurso(
            @PathVariable Long id, 
            @Valid @RequestBody AsignacionRecursoRequestDTO request) {
        
        // 1. Mapear explícitamente el Body del Frontend al @RequestParam que Feign exige
        proyectoClient.vincularRecurso(id, request.recursoId());
        
        // 2. Sincronizar el estado en el microservicio de recursos a ocupado
        recursoClient.actualizarDisponibilidad(request.recursoId(), "OCUPADO");

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/recursos/{recursoId}")
    public ResponseEntity<Void> eliminarRecurso(
            @PathVariable Long id, 
            @PathVariable Long recursoId) {

        // 1. Desvincular en el microservicio de proyectos consumiendo su @RequestParam
        proyectoClient.desvincularRecurso(id, recursoId);
        
        // 2. Sincronizar el estado en el microservicio de recursos a disponible
        recursoClient.actualizarDisponibilidad(recursoId, "DISPONIBLE");

        return ResponseEntity.noContent().build();
    }
}