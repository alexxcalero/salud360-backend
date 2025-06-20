package pe.edu.pucp.salud360.control.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.control.dto.*;

import pe.edu.pucp.salud360.control.services.ReporteService;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;

import java.time.LocalDate;

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
    @GetMapping("/usuarios/html-preview")
    public ResponseEntity<String> obtenerVistaPreviaUsuarios(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        ReporteUsuarioRequestDTO filtro = new ReporteUsuarioRequestDTO();
        filtro.setFechaInicio(fechaInicio);
        filtro.setFechaFin(fechaFin);
        filtro.setDescripcion("Vista previa de generación del reporte de usuarios"); // o lo que uses

        // Generar contenido HTML sin generar PDF
        String html = reporteService.generarHTMLVistaPreviaUsuarios(filtro); // crea este método aparte
        return ResponseEntity.ok(html);
    }

    @PostMapping("/boleta")
    public ResponseEntity<ReporteDTO> generarBoleta(@RequestBody PagoDTO pago) {
        ReporteDTO reporte = reporteService.generarBoleta(pago);
        return ResponseEntity.ok(reporte);
    }
}
