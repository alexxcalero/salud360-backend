package pe.edu.pucp.salud360.servicio.mappers;


import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        UsuarioMapper.class,
        ClaseMapper.class,
        CitaMedicaMapper.class
})
public interface ReservaMapper {

    // Para operaciones CRUD
    ReservaDTO mapToDTO(Reserva reserva);
    Reserva mapToModel(ReservaDTO dto);

    // Para vistas resumidas
    ReservaResumenDTO mapToResumenDTO(Reserva reserva);
    Reserva mapToModel(ReservaResumenDTO dto);

    // Para vista admin detallada
    ReservaVistaAdminDTO mapToVistaAdminDTO(Reserva reserva);
    Reserva mapToModel(ReservaVistaAdminDTO dto);

    // Listas
    List<ReservaDTO> mapToDTOList(List<Reserva> reservas);
    List<ReservaResumenDTO> mapToResumenList(List<Reserva> reservas);
    List<ReservaVistaAdminDTO> mapToVistaAdminList(List<Reserva> reservas);
}

