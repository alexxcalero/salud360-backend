package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.models.TipoDocumento;

@Mapper(componentModel = "spring")
public interface TipoDocumentoMapper {
    TipoDocumentoVistaAdminDTO mapToVistaAdminDTO(TipoDocumento tipoDocumento);
    TipoDocumento mapToModel(TipoDocumentoVistaAdminDTO tipoDocumentoDTO);

    TipoDocumentoResumenDTO mapToResumenDTO(TipoDocumento tipoDocumento);
    TipoDocumento mapToModel(TipoDocumentoResumenDTO tipoDocumentoDTO);
}
