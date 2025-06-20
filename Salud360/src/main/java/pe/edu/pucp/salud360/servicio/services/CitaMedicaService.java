package pe.edu.pucp.salud360.servicio.services;

import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;

import java.util.List;

public interface CitaMedicaService {
    CitaMedicaDTO crearCitaMedica(CitaMedicaDTO citaMedicaDTO);
    CitaMedicaDTO actualizarCitaMedica(Integer idCitaMedica, CitaMedicaDTO citaMedicaDTO);
    void eliminarCitaMedica(Integer idCitaMedica);
    void reactivarCitaMedica(Integer idCitaMedica);
    List<CitaMedicaDTO> listarCitasMedicasTodas();
    CitaMedicaDTO buscarCitaMedicaPorId(Integer idCitaMedica);
}
