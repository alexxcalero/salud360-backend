package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.PersonaDTO;
import pe.edu.pucp.salud360.usuario.models.Persona;

@Mapper(componentModel = "spring")
public interface PersonaMapper {
    PersonaDTO mapToDTO(Persona persona);
    Persona mapToModel(PersonaDTO personaDTO);
}
