package pe.edu.pucp.salud360.comunidad.services;

import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;

import java.util.List;

public interface ComunidadService {
    ComunidadDTO crearComunidad(ComunidadDTO dto);
    ComunidadDTO actualizarComunidad(Integer id, ComunidadDTO dto);
    boolean eliminarComunidad(Integer id);
    ComunidadDTO obtenerComunidadPorId(Integer id);
    List<ComunidadDTO> listarComunidades();
    List<ComunidadDTO> listarComunidadesActivas();
    boolean restaurarComunidad(Integer id);
    List<ReservaDTO> listarReservasPorComunidad(Integer idComunidad);
    List<ClaseResumenDTO> listarClasesPorComunidad(Integer idComunidad);
    List<CitaMedicaResumenDTO> listarCitasMedicasPorComunidad(Integer idComunidad);
    ComunidadDTO obtenerComunidadAleatoriaExcluyendoCliente(Integer idCliente);
    List<ComunidadDTO> obtenerComunidadesExcluyendoCliente(Integer idCliente);
    List<ComunidadDTO> obtenerComunidadesInactivasCliente(Integer idCliente);
}
