package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.membresia.mappers.AfiliacionMapper;
import pe.edu.pucp.salud360.membresia.mappers.MedioDePagoMapper;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.models.Persona;

@Mapper(componentModel = "spring",
        uses = {CitaMedicaMapper.class,
                ReservaMapper.class,
                ComunidadMapper.class,
                ClaseMapper.class,
                AfiliacionMapper.class,
                MedioDePagoMapper.class,
                CitaMedicaMapper.class,})
public interface PersonaMapper {
    PersonaVistaAdminDTO mapToVistaAdminDTO(Persona persona);
    Persona mapToModel(PersonaVistaAdminDTO personaDTO);

    PersonaResumenDTO mapToResumenDTO(Persona persona);
    Persona mapToModel(PersonaResumenDTO personaDTO);

    Persona mapToModel(PersonaRegistroDTO personaDTO);
}
