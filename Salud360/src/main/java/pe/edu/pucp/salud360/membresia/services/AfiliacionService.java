package pe.edu.pucp.salud360.membresia.services;

import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;

import java.util.List;

public interface AfiliacionService {
    AfiliacionResumenDTO crearAfiliacion(AfiliacionDTO dto);
    List<AfiliacionResumenDTO> listarAfiliaciones();
    boolean eliminarAfiliacion(Integer id);
    AfiliacionResumenDTO buscarAfiliacionPorId(Integer id);
    AfiliacionResumenDTO actualizarAfiliacion(Integer id, AfiliacionDTO dto);
}

