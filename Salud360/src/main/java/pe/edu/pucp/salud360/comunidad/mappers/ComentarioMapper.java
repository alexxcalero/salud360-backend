package pe.edu.pucp.salud360.comunidad.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO.ComentarioDTO;
import pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO.ComentarioResumenDTO;
import pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO.ComentarioVistaAdminDTO;
import pe.edu.pucp.salud360.comunidad.models.Comentario;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface ComentarioMapper {

    // Crear / Editar
    ComentarioDTO mapToDTO(Comentario comentario);
    Comentario mapToModel(ComentarioDTO dto);

    // Para vista simple en publicaciones
    ComentarioResumenDTO mapToResumenDTO(Comentario comentario);
    Comentario mapToModel(ComentarioResumenDTO dto);

    // Para vista admin con usuario
    ComentarioVistaAdminDTO mapToVistaAdminDTO(Comentario comentario);
    Comentario mapToModel(ComentarioVistaAdminDTO dto);

    // Listas
    List<ComentarioDTO> mapToDTOList(List<Comentario> comentarios);
    List<ComentarioResumenDTO> mapToResumenList(List<Comentario> comentarios);
    List<ComentarioVistaAdminDTO> mapToVistaAdminList(List<Comentario> comentarios);
}

