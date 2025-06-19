package pe.edu.pucp.salud360.control.services;

import pe.edu.pucp.salud360.control.dto.*;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;

public interface ReporteService {

    ReporteDTO generarReporteUsuarios(ReporteUsuarioRequestDTO filtro);

    ReporteDTO generarReporteServicios(ReporteServicioRequestDTO filtro);

    ReporteDTO generarReporteLocales(ReporteLocalRequestDTO filtro);

    ReporteDTO generarBoleta(PagoDTO pago);
}
