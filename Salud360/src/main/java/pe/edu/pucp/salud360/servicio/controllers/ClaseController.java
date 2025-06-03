package pe.edu.pucp.salud360.servicio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;
import pe.edu.pucp.salud360.servicio.services.ClaseService;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
@RequiredArgsConstructor
public class ClaseController {

    private final ClaseService claseService;

    @PostMapping
    public ResponseEntity<ClaseDTO> crearClase(@RequestBody ClaseDTO claseDTO) {
        ClaseDTO creada = claseService.crearClase(claseDTO);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaseDTO> actualizarClase(@PathVariable("id") Integer id,
                                                    @RequestBody ClaseDTO claseDTO) {
        ClaseDTO existente = claseService.buscarClasePorId(id);
        if (existente != null) {
            ClaseDTO actualizada = claseService.actualizarClase(id, claseDTO);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarClase(@PathVariable("id") Integer id) {
        ClaseDTO existente = claseService.buscarClasePorId(id);
        if (existente != null) {
            claseService.eliminarClase(id);
            return new ResponseEntity<>("Clase eliminada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Clase no encontrada", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/reactivar")
    public ResponseEntity<String> reactivarClase(@PathVariable("id") Integer id) {
        ClaseDTO existente = claseService.buscarClasePorId(id);
        if (existente != null) {
            claseService.reactivarClase(id);
            return new ResponseEntity<>("Clase reactivada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Clase no encontrada", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ClaseDTO>> listarClases() {
        List<ClaseDTO> lista = claseService.listarClasesTodas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaseDTO> buscarClasePorId(@PathVariable("id") Integer id) {
        ClaseDTO clase = claseService.buscarClasePorId(id);
        if (clase != null)
            return new ResponseEntity<>(clase, HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }
}
