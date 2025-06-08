package pe.edu.pucp.salud360.servicio.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaResumenDTO;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.usuario.mappers.ClienteMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ClienteMapper.class,
        ClaseMapper.class,
        CitaMedicaMapper.class,
        ComunidadMapper.class
})
public interface ReservaMapper {

    // Para operaciones CRUD
    ReservaDTO mapToDTO(Reserva reserva);
    Reserva mapToModel(ReservaDTO dto);

    // Para vistas resumidas
    ReservaResumenDTO mapToResumenDTO(Reserva reserva);
    Reserva mapToModel(ReservaResumenDTO dto);

    // Listas
    List<ReservaDTO> mapToDTOList(List<Reserva> reservas);
}
