package pe.edu.pucp.salud360.servicio.mappers;


import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalResumenDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ServicioMapper.class})
public interface LocalMapper {

    // DTO básico para crear/editar
    LocalDTO mapToDTO(Local local);
    Local mapToModel(LocalDTO dto);

    // Vista administrativa enriquecida
    LocalVistaAdminDTO mapToVistaAdminDTO(Local local);
    Local mapToModel(LocalVistaAdminDTO dto);

    List<LocalVistaAdminDTO> mapToVistaAdminList(List<Local> locales);

    // Opcional: resumen para tabla
    LocalResumenDTO mapToResumenDTO(Local local);
    Local mapToModel(LocalResumenDTO dto);
}

