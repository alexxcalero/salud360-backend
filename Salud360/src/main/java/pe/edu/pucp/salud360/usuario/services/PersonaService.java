package pe.edu.pucp.salud360.usuario.services;

import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaVistaAdminDTO;

import java.util.List;

public interface PersonaService {
    PersonaVistaAdminDTO crearPersona(PersonaRegistroDTO personaDTO);
    PersonaVistaAdminDTO actualizarPersona(Integer idPersona, PersonaVistaAdminDTO personaDTO);
    void eliminarPersona(Integer idPersona);
    List<PersonaVistaAdminDTO> listarPersonasTodas();
    PersonaVistaAdminDTO buscarPersonaPorId(Integer idPersona);
}
