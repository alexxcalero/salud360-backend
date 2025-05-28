package pe.edu.pucp.salud360.control.services;

import pe.edu.pucp.salud360.control.dto.ReglasDeNegocioDTO;

import java.util.List;

public interface ReglasDeNegocioService {
    ReglasDeNegocioDTO crearReglasDeNegocio(ReglasDeNegocioDTO reglasDTO);
    ReglasDeNegocioDTO actualizarReglasDeNegocio(Integer idReglas, ReglasDeNegocioDTO reglasDTO);
    void eliminarReglasDeNegocio(Integer idReglas);
    List<ReglasDeNegocioDTO> listarReglasDeNegocio();
    ReglasDeNegocioDTO buscarReglaDeNegocioPorId(Integer idReglas);
}
