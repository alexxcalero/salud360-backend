package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.models.Medico;

@Mapper(componentModel = "spring", uses = {TipoDocumentoMapper.class, CitaMedicaMapper.class})
public interface MedicoMapper {
    MedicoVistaAdminDTO mapToVistaAdminDTO(Medico medico);
    Medico mapToModel(MedicoVistaAdminDTO medicoDTO);

    MedicoResumenDTO mapToResumenDTO(Medico medico);
    Medico mapToModel(MedicoResumenDTO medicoDTO);

    Medico mapToModel(MedicoRegistroDTO medicoDTO);
}
