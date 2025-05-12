package pe.edu.pucp.salud360.servicio.mappers;


import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.DocumentoMedicoDTO;
import pe.edu.pucp.salud360.servicio.models.DocumentoMedico;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentoMedicoMapper {

    DocumentoMedicoDTO mapToDTO(DocumentoMedico documento);
    DocumentoMedico mapToModel(DocumentoMedicoDTO dto);

    List<DocumentoMedicoDTO> mapToDTOList(List<DocumentoMedico> documentos);
    List<DocumentoMedico> mapToModelList(List<DocumentoMedicoDTO> documentosDTO);
}
