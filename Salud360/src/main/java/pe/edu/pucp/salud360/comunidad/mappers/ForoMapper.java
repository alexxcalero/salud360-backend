package pe.edu.pucp.salud360.comunidad.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.comunidad.dto.ForoDTO.ForoDTO;
import pe.edu.pucp.salud360.comunidad.dto.ForoDTO.ForoResumenDTO;
import pe.edu.pucp.salud360.comunidad.models.Foro;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ComunidadMapper.class})
public interface ForoMapper {

    // Vista enriquecida con comunidad (para respuesta)
    ForoDTO mapToDTO(Foro foro);
    Foro mapToModel(ForoDTO dto); // ⚠️ este solo funcionará bien si dto.comunidad no es null o si se inyecta por service

    ForoResumenDTO mapToResumenDTO(Foro foro);
    Foro mapToModel(ForoResumenDTO dto);

    List<ForoDTO> mapToDTOList(List<Foro> foros);
    List<ForoResumenDTO> mapToResumenList(List<Foro> foros);
}

