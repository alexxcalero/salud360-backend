package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorResumenDTO;
import pe.edu.pucp.salud360.usuario.models.Administrador;

@Mapper(componentModel = "spring", uses = {TipoDocumentoMapper.class})
public interface AdministradorMapper {
    @Mapping(source = "usuario.correo", target = "correo")
    @Mapping(source = "usuario.rol.nombre", target = "rol")
    AdministradorLogueadoDTO mapToLogueadoDTO(Administrador administrador);

    @Mapping(target = "usuario.correo", source = "correo")
    @Mapping(target = "usuario.rol.nombre", source = "rol")
    Administrador mapToModel(AdministradorLogueadoDTO administradorDTO);

    @Mapping(source = "usuario.correo", target = "correo")
    @Mapping(source = "usuario.rol.nombre", target = "rol")
    AdministradorResumenDTO mapToResumenDTO(Administrador administrador);

    @Mapping(target = "usuario.correo", source = "correo")
    @Mapping(target = "usuario.rol.nombre", source = "rol")
    Administrador mapToModel(AdministradorResumenDTO administradorDTO);

    Administrador mapToModel(AdministradorRegistroDTO administradorDTO);
}
