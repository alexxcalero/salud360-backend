package pe.edu.pucp.salud360.comunidad.mappers;


import org.mapstruct.*;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Foro;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        ServicioMapper.class,
        MembresiaMapper.class
})
public interface ComunidadMapper {

    @Mapping(source = "foro.idForo", target = "idForo")
    ComunidadDTO mapToDTO(Comunidad comunidad);

    @Mapping(target = "foro", ignore = true) // o usar @Context + @AfterMapping si lo seteas tú
    @Mapping(target = "servicios", ignore = true)
    @Mapping(target = "membresia", ignore = true)
    @Mapping(target = "testimonios", ignore = true)
    @Mapping(target = "persona", ignore = true)
    Comunidad mapToModel(ComunidadDTO dto);

    ComunidadResumenDTO mapToResumenDTO(Comunidad comunidad);
    List<ComunidadResumenDTO> mapToResumenList(List<Comunidad> comunidades);

    List<ComunidadDTO> mapToDTOList(List<Comunidad> comunidades);

    @AfterMapping
    default void setForo(@MappingTarget Comunidad comunidad, @Context Foro foro) {
        comunidad.setForo(foro);
    }
}


