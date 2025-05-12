package pe.edu.pucp.salud360.servicio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.services.ServicioService;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @PostMapping
    public ResponseEntity<ServicioDTO> crearServicio(@RequestBody ServicioDTO dto) {
        ServicioDTO creado = servicioService.crearServicio(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable("id") Integer id,
                                                          @RequestBody ServicioDTO dto) {
        ServicioDTO existente = servicioService.buscarServicioPorId(id);
        if (existente != null) {
            ServicioDTO actualizado = servicioService.actualizarServicio(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarServicio(@PathVariable("id") Integer id) {
        ServicioDTO existente = servicioService.buscarServicioPorId(id);
        if (existente != null) {
            servicioService.eliminarServicio(id);
            return new ResponseEntity<>("Servicio eliminado correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Servicio no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listarServicios() {
        List<ServicioDTO> lista = servicioService.listarServiciosTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> buscarServicioPorId(@PathVariable("id") Integer id) {
        ServicioDTO servicio = servicioService.buscarServicioPorId(id);
        if (servicio != null)
            return new ResponseEntity<>(servicio, HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }
}
