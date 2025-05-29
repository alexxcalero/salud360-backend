package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorResumenDTO;
import pe.edu.pucp.salud360.usuario.models.Administrador;

@Mapper(componentModel = "spring", uses = {TipoDocumentoMapper.class})
public interface AdministradorMapper {
    AdministradorLogueadoDTO mapToLogueadoDTO(Administrador administrador);
    Administrador mapToModel(AdministradorLogueadoDTO administradorDTO);

    AdministradorResumenDTO mapToResumenDTO(Administrador administrador);
    Administrador mapToModel(AdministradorResumenDTO administradorDTO);

    Administrador mapToModel(AdministradorRegistroDTO administradorDTO);
}
