package pe.edu.pucp.salud360.servicio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.services.ReservaService;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final S3UrlGenerator s3UrlGenerator;
    
    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        ReservaDTO creada = reservaService.crearReserva(reservaDTO);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable("id") Integer id) {
        ReservaDTO existente = reservaService.buscarReservaPorId(id);
        if (existente != null) {
            reservaService.cancelarReserva(id);
            return new ResponseEntity<>("Reserva cancelada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> buscarReservaPorId(@PathVariable("id") Integer id) {
        ReservaDTO reserva = reservaService.buscarReservaPorId(id);
        if (reserva != null)
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/archivo-url/{nombreArchivo}")
    public ResponseEntity<String> obtenerUrlArchivo(@PathVariable String nombreArchivo) {
        String url = s3UrlGenerator.generarUrlLectura(nombreArchivo);
        return ResponseEntity.ok(url);
    }
}
