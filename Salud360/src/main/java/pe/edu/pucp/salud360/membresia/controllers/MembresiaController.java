package pe.edu.pucp.salud360.membresia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.services.MembresiaService;

import java.util.List;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {

    @Autowired
    private MembresiaService membresiaService;

    @PostMapping
    public ResponseEntity<MembresiaDTO> crear(@RequestBody MembresiaDTO dto) {
        return new ResponseEntity<>(membresiaService.crearMembresia(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MembresiaDTO>> listar() {
        return new ResponseEntity<>(membresiaService.listarMembresias(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembresiaResumenDTO> buscar(@PathVariable Integer id) {
        MembresiaResumenDTO dto = membresiaService.buscarMembresiaPorId(id);
        return dto != null ? new ResponseEntity<>(dto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembresiaResumenDTO> actualizar(@PathVariable Integer id, @RequestBody MembresiaDTO dto) {
        MembresiaResumenDTO actualizado = membresiaService.actualizarMembresia(id, dto);
        return actualizado != null ? new ResponseEntity<>(actualizado, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return membresiaService.eliminarMembresia(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

