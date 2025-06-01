package pe.edu.pucp.salud360.servicio.mappers;


import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalResumenDTO;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {ServicioMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)


public interface LocalMapper {

    // DTO básico para crear/editar
    @Mapping(source = "servicio.idServicio", target = "idServicio")
    LocalDTO mapToDTO(Local local);
    Local mapToModel(LocalDTO dto);

    // Vista administrativa enriquecida
    LocalVistaAdminDTO mapToVistaAdminDTO(Local local);
    Local mapToModel(LocalVistaAdminDTO dto);

    List<LocalDTO> mapToDTOList(List<Local> locales);
    List<LocalVistaAdminDTO> mapToVistaAdminList(List<Local> locales);

    // Opcional: resumen para tabla
    LocalResumenDTO mapToResumenDTO(Local local);
    Local mapToModel(LocalResumenDTO dto);
}

