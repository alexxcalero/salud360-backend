package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.MedicoDTO;
import pe.edu.pucp.salud360.usuario.models.Medico;

@Mapper(componentModel = "spring")
public interface MedicoMapper {
    MedicoDTO mapToDTO(Medico medico);
    Medico mapToModel(MedicoDTO medicoDTO);
}
