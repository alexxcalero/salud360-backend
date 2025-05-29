package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import pe.edu.pucp.salud360.usuario.models.Usuario;

@Mapper(componentModel = "spring", uses = {RolMapper.class})
public interface UsuarioMapper {
    UsuarioResumenDTO mapToResumenDTO(Usuario usuario);
    Usuario mapToModel(UsuarioResumenDTO usuarioDTO);

    Usuario mapToModel(UsuarioRegistroDTO usuarioDTO);
}
