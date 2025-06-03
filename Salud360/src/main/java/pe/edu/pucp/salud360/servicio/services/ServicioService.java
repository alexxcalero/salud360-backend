package pe.edu.pucp.salud360.servicio.services;

import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaClienteDTO;

import java.util.List;

public interface ServicioService {
    ServicioDTO crearServicio(ServicioDTO servicioDTO);
    ServicioDTO actualizarServicio(Integer idServicio, ServicioDTO servicioDTO);
    void eliminarServicio(Integer idServicio);
    void reactivarServicio(Integer idServicio);
    List<ServicioVistaAdminDTO> listarServiciosTodos();
    List<ServicioVistaClienteDTO> listarServiciosClientes();
    ServicioVistaAdminDTO buscarServicioPorId(Integer idServicio);
}
