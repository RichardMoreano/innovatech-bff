package cl.duoc.innovatech.bff.client;

import cl.duoc.innovatech.bff.dto.ProyectoRequestDTO;
import cl.duoc.innovatech.bff.dto.ProyectoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-gestion-proyectos", url = "http://ms-gestion-proyectos:8081/api/v2/proyectos")
public interface ProyectoClient {

    @GetMapping
    List<ProyectoResponseDTO> obtenerTodos();

    @GetMapping("/{id}")
    ProyectoResponseDTO obtenerPorId(@PathVariable("id") Long id);

    @PostMapping
    ProyectoResponseDTO crear(@RequestBody ProyectoRequestDTO request);

    @PutMapping("/{id}")
    ProyectoResponseDTO actualizar(@PathVariable("id") Long id, @RequestBody ProyectoRequestDTO request);

    @DeleteMapping("/{id}")
    void eliminar(@PathVariable("id") Long id);
}