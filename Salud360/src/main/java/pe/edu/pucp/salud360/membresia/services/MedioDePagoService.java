package pe.edu.pucp.salud360.membresia.services;


import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoDTO;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;

import java.util.List;

public interface MedioDePagoService {
    List<MedioDePagoResumenDTO> listar();
    MedioDePagoDTO crear(MedioDePagoDTO dto);
    MedioDePagoResumenDTO obtenerPorId(Integer id);
    boolean eliminar(Integer id);
    MedioDePagoDTO actualizar(Integer id, MedioDePagoDTO dto);
    List<MedioDePagoResumenDTO> listarPorUsuario(Integer idUsuario);
}

