package pe.edu.pucp.salud360.control.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.control.dto.*;

import pe.edu.pucp.salud360.control.services.ReporteService;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // POST: Reporte de usuarios
    @PostMapping("/usuarios")
    public ResponseEntity<ReporteDTO> generarReporteUsuarios(@RequestBody ReporteUsuarioRequestDTO filtro) {
        ReporteDTO reporte = reporteService.generarReporteUsuarios(filtro);
        return ResponseEntity.ok(reporte);
    }

    // POST: Reporte de servicios
    @PostMapping("/servicios")
    public ResponseEntity<ReporteDTO> generarReporteServicios(@RequestBody ReporteServicioRequestDTO filtro) {
        ReporteDTO reporte = reporteService.generarReporteServicios(filtro);
        return ResponseEntity.ok(reporte);
    }

    // POST: Reporte de locales
    @PostMapping("/locales")
    public ResponseEntity<ReporteDTO> generarReporteLocales(@RequestBody ReporteLocalRequestDTO filtro) {
        ReporteDTO reporte = reporteService.generarReporteLocales(filtro);
        return ResponseEntity.ok(reporte);
    }

    @PostMapping("/boleta")
    public ResponseEntity<ReporteDTO> generarBoleta(@RequestBody PagoDTO pago) {
        ReporteDTO reporte = reporteService.generarBoleta(pago);
        return ResponseEntity.ok(reporte);
    }
}
