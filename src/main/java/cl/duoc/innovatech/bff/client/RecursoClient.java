package cl.duoc.innovatech.bff.client;

import cl.duoc.innovatech.bff.config.FeignClientConfig;
import cl.duoc.innovatech.bff.dto.RecursoRequestDTO;
import cl.duoc.innovatech.bff.dto.RecursoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "ms-gestion-recursos", url = "http://ms-gestion-recursos:8082", configuration = FeignClientConfig.class)
public interface RecursoClient {

    @PostMapping("/api/v2/recursos")
    RecursoResponseDTO crear(@RequestBody RecursoRequestDTO request);

    @GetMapping("/api/v2/recursos")
    List<RecursoResponseDTO> obtenerTodos();

    @GetMapping("/api/v2/recursos/{id}")
    RecursoResponseDTO obtenerPorId(@PathVariable("id") Long id);

    @PutMapping("/api/v2/recursos/{id}")
    RecursoResponseDTO actualizar(@PathVariable("id") Long id, @RequestBody RecursoRequestDTO request);

    @DeleteMapping("/api/v2/recursos/{id}")
    void eliminar(@PathVariable("id") Long id);
}