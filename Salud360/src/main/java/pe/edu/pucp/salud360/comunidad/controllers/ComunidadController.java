package pe.edu.pucp.salud360.comunidad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/comunidades")
public class ComunidadController {

    @Autowired
    private ComunidadService comunidadService;

    @PostMapping()
    public ResponseEntity<ComunidadDTO> crear(@RequestBody ComunidadDTO dto) {
        ComunidadDTO creada = comunidadService.crearComunidad(dto);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunidadDTO> actualizar(@PathVariable Integer id, @RequestBody ComunidadDTO dto) {
        ComunidadDTO actualizada = comunidadService.actualizarComunidad(id, dto);
        return actualizada != null
                ? new ResponseEntity<>(actualizada, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        ComunidadDTO comunidadBuscada = comunidadService.obtenerComunidadPorId(id);
        if (comunidadBuscada != null) {
            comunidadService.eliminarComunidad(id);
            return new ResponseEntity<>("Comunidad eliminada satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Comunidad no encontrada", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunidadDTO> obtener(@PathVariable Integer id) {
        ComunidadDTO comunidad = comunidadService.obtenerComunidadPorId(id);
        return comunidad != null
                ? new ResponseEntity<>(comunidad, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ComunidadDTO>> listar() {
        return new ResponseEntity<>(comunidadService.listarComunidades(), HttpStatus.OK);
    }

    @PutMapping("/{id}/restaurar")
    public ResponseEntity<String> restaurarComunidad(@PathVariable Integer id) {
        boolean restaurado = comunidadService.restaurarComunidad(id);
        if (restaurado) {
            return ResponseEntity.ok("Comunidad restaurada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comunidad no encontrada");
        }
    }



}
