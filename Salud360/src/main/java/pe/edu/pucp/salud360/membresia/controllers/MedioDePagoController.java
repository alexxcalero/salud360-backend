package pe.edu.pucp.salud360.membresia.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoDTO;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;
import pe.edu.pucp.salud360.membresia.services.MedioDePagoService;

import java.util.List;

@RestController
@RequestMapping("/api/mediosDePago")
public class MedioDePagoController {

    @Autowired
    private MedioDePagoService medioDePagoService;

    @GetMapping
    public ResponseEntity<List<MedioDePagoResumenDTO>> listar() {
        return ResponseEntity.ok(medioDePagoService.listar());
    }

    @PostMapping
    public ResponseEntity<MedioDePagoDTO> crear(@RequestBody MedioDePagoDTO dto) {
        return ResponseEntity.ok(medioDePagoService.crear(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedioDePagoResumenDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(medioDePagoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedioDePagoDTO> actualizar(@PathVariable Integer id, @RequestBody MedioDePagoDTO dto) {
        return ResponseEntity.ok(medioDePagoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = medioDePagoService.eliminar(id);
        return eliminado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<MedioDePagoResumenDTO>> listarPorUsuario(@PathVariable Integer id) {
        // Obtiene los medios de pago asociados al usuario con el id proporcionado
        List<MedioDePagoResumenDTO> mediosDePago = medioDePagoService.listarPorUsuario(id);
        return ResponseEntity.ok(mediosDePago);
    }

    @PostMapping("/verificar")
    public Boolean VerificarTarjeta(@RequestBody MedioDePagoDTO dto) {
        return medioDePagoService.verificarDatosSensibles(dto);
    }
}

