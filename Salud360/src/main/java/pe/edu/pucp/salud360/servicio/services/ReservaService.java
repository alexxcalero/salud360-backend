package pe.edu.pucp.salud360.servicio.services;

import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;

public interface ReservaService {
    ReservaDTO crearReserva(ReservaDTO reservaDTO);
    void cancelarReserva(Integer idReserva);
    ReservaDTO buscarReservaPorId(Integer idReserva);
}
