package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaClienteDTO;
import pe.edu.pucp.salud360.usuario.models.Usuario;

@Mapper(componentModel = "spring", uses = {TipoDocumentoMapper.class, RolMapper.class})
public interface UsuarioMapper {
    UsuarioVistaAdminDTO mapToVistaAdminDTO(Usuario usuario);
    Usuario mapToModel(UsuarioVistaAdminDTO usuarioDTO);

    UsuarioVistaClienteDTO mapToVistaClienteDTO(Usuario usuario);
    Usuario mapToModel(UsuarioVistaClienteDTO usuarioDTO);

    UsuarioResumenDTO mapToResumenDTO(Usuario usuario);
    Usuario mapToModel(UsuarioResumenDTO usuarioDTO);

    Usuario mapToModel(UsuarioRegistroDTO usuarioDTO);
}
