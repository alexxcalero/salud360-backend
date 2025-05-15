package pe.edu.pucp.salud360.comunidad.services;

import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;

import java.util.List;

public interface ComunidadService {
    ComunidadDTO crearComunidad(ComunidadDTO dto);
    ComunidadDTO actualizarComunidad(Integer id, ComunidadDTO dto);
    boolean eliminarComunidad(Integer id);
    ComunidadDTO obtenerComunidadPorId(Integer id);
    List<ComunidadDTO> listarComunidades();
    boolean restaurarComunidad(Integer id);
    List<ServicioDTO> listarServiciosPorComunidad(Integer idComunidad);
    List<MembresiaDTO> listarMembresiasPorComunidad(Integer idComunidad);

}
