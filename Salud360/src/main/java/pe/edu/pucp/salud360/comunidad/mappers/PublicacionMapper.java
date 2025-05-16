package pe.edu.pucp.salud360.comunidad.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.comunidad.dto.PublicacionDTO.PublicacionDTO;
import pe.edu.pucp.salud360.comunidad.dto.PublicacionDTO.PublicacionResumenDTO;
import pe.edu.pucp.salud360.comunidad.models.Publicacion;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface PublicacionMapper {

    // Vista completa (admin, detalle)
    @Mapping(source = "persona", target = "autor")
    @Mapping(source = "foro.idForo", target = "idForo")
    PublicacionDTO mapToDTO(Publicacion publicacion);

    @Mapping(target = "persona", source = "autor")
    @Mapping(target = "foro.idForo", source = "idForo")
    Publicacion mapToModel(PublicacionDTO dto);

    // Vista resumida (usuario, comunidad)
    @Mapping(source = "persona", target = "autor")
    PublicacionResumenDTO mapToResumenDTO(Publicacion publicacion);

    List<PublicacionDTO> mapToDTOList(List<Publicacion> publicaciones);
    List<PublicacionResumenDTO> mapToResumenList(List<Publicacion> publicaciones);
}

