package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaClienteDTO;
import pe.edu.pucp.salud360.usuario.models.Rol;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface RolMapper {
    RolVistaAdminDTO mapToVistaAdminDTO(Rol rol);
    Rol mapToModel(RolVistaAdminDTO rolDTO);

    RolVistaClienteDTO mapToVistaClienteDTO(Rol rol);
    Rol mapToModel(RolVistaClienteDTO rolDTO);

    //List<Rol> mapToModelList(List<RolResumenDTO> rolesDTO);
}
