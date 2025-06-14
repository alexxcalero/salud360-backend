package pe.edu.pucp.salud360.servicio.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
    @Mappings({
            @Mapping(source = "descripcion", target = "descripcion"),
            @Mapping(source = "nombreArchivo", target = "nombreArchivo")
    })
    ReservaDTO mapToDTO(Reserva reserva);
    @Mappings({
            @Mapping(source = "descripcion", target = "descripcion"),
            @Mapping(source = "nombreArchivo", target = "nombreArchivo")
    })
    Reserva mapToModel(ReservaDTO dto);

    // Para vistas resumidas
    ReservaResumenDTO mapToResumenDTO(Reserva reserva);
    Reserva mapToModel(ReservaResumenDTO dto);

    // Listas
    List<ReservaDTO> mapToDTOList(List<Reserva> reservas);
}
