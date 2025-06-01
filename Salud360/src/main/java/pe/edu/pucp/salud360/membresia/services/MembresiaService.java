package pe.edu.pucp.salud360.membresia.services;

import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;

import java.util.List;

public interface MembresiaService {
    MembresiaDTO crearMembresia(MembresiaDTO dto);
    List<MembresiaDTO> listarMembresias();
    MembresiaResumenDTO buscarMembresiaPorId(Integer id);
    MembresiaResumenDTO actualizarMembresia(Integer id, MembresiaDTO dto);
    boolean eliminarMembresia(Integer id);
}