package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.models.Medico;

@Mapper(componentModel = "spring", uses = {TipoDocumentoMapper.class, CitaMedicaMapper.class})
public interface MedicoMapper {

    @Mapping(source = "fotoPerfil", target = "fotoPerfil")
    MedicoVistaAdminDTO mapToVistaAdminDTO(Medico medico);

    @Mapping(source = "fotoPerfil", target = "fotoPerfil")
    Medico mapToModel(MedicoVistaAdminDTO medicoDTO);

    @Mapping(source = "fotoPerfil", target = "fotoPerfil")
    MedicoResumenDTO mapToResumenDTO(Medico medico);

    @Mapping(source = "fotoPerfil", target = "fotoPerfil")
    Medico mapToModel(MedicoResumenDTO medicoDTO);

    @Mapping(source = "fotoPerfil", target = "fotoPerfil")
    Medico mapToModel(MedicoRegistroDTO medicoDTO);
}
