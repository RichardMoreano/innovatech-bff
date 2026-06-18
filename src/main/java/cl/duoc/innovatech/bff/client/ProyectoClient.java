package cl.duoc.innovatech.bff.client;

import cl.duoc.innovatech.bff.dto.ProyectoRequestDTO;
import cl.duoc.innovatech.bff.dto.ProyectoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(
    name = "servicio-proyecto", 
    url = "${PROYECTOS_URL:http://ms-gestion-proyectos:8081}/api/v2/proyectos", 
    configuration = cl.duoc.innovatech.bff.config.FeignClientConfig.class
)
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

    @GetMapping("/{id}/recursos-ids")
    List<Long> obtenerRecursosIdsPorProyecto(@PathVariable("id") Long id);

    @PutMapping("/{id}/estado-interno")
    ProyectoResponseDTO actualizarEstado(@PathVariable("id") Long id, @RequestParam("estado") String estado);

    @PostMapping("/{id}/vincular")
    void vincularRecurso(@PathVariable("id") Long id, @RequestParam("recursoId") Long recursoId);

    @DeleteMapping("/{id}/desvincular")
    void desvincularRecurso(@PathVariable("id") Long id, @RequestParam("recursoId") Long recursoId);
}