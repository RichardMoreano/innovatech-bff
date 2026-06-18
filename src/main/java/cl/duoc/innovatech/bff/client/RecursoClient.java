package cl.duoc.innovatech.bff.client;

import cl.duoc.innovatech.bff.dto.RecursoRequestDTO;
import cl.duoc.innovatech.bff.dto.RecursoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(
    name = "servicio-recurso", 
    url = "${RECURSOS_URL:http://ms-gestion-recursos:8086}/api/v2/recursos", 
    configuration = cl.duoc.innovatech.bff.config.FeignClientConfig.class
)
public interface RecursoClient {

    @GetMapping
    List<RecursoResponseDTO> obtenerTodos();

    @GetMapping("/{id}")
    RecursoResponseDTO obtenerPorId(@PathVariable("id") Long id);

    @PostMapping
    RecursoResponseDTO crear(@RequestBody RecursoRequestDTO request);

    @PutMapping("/{id}")
    RecursoResponseDTO actualizar(@PathVariable("id") Long id, @RequestBody RecursoRequestDTO request);

    @DeleteMapping("/{id}")
    void eliminar(@PathVariable("id") Long id);

    @PutMapping("/{id}/disponibilidad")
    void actualizarDisponibilidad(
        @PathVariable("id") Long id,
        @RequestParam("disponibilidad") String disponibilidad
    );
}