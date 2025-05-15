package pe.edu.pucp.salud360.comunidad.mappers;


import org.mapstruct.*;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Foro;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ServicioMapper.class})
public interface ComunidadMapper {

    @Mapping(source = "foro.idForo", target = "idForo")
    ComunidadDTO mapToDTO(Comunidad comunidad);

    @Mapping(target = "foro", source = "foro")
    @Mapping(target = "servicios", ignore = true) // Si deseas setear manualmente
    Comunidad mapToModel(ComunidadDTO dto, @Context Foro foro);

    List<ComunidadDTO> mapToDTOList(List<Comunidad> comunidades);

    // Método default por si necesitas transformar servicios tú mismo
    default List<ServicioDTO> mapServicios(List<Servicio> servicios) {
        if (servicios == null) return null;
        return servicios.stream().map(s -> new ServicioDTO(
                s.getIdServicio(),
                s.getNombre(),
                s.getDescripcion(),
                null,
                null
        )).collect(Collectors.toList());
    }
}

