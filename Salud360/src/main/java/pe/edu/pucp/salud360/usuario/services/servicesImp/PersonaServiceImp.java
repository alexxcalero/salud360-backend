package pe.edu.pucp.salud360.usuario.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaDTO;
import pe.edu.pucp.salud360.usuario.mappers.PersonaMapper;
import pe.edu.pucp.salud360.usuario.mappers.RolMapper;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.models.Persona;
import pe.edu.pucp.salud360.usuario.repositories.PersonaRepository;
import pe.edu.pucp.salud360.usuario.services.PersonaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaServiceImp implements PersonaService {
    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaMapper personaMapper;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private RolMapper rolMapper;

    @Override
    public PersonaDTO crearPersona(PersonaDTO personaDTO) {
        Persona persona = personaMapper.mapToModel(personaDTO);
        persona.setActivo(true);
        persona.setFechaCreacion(LocalDateTime.now());
        persona.setFechaDesactivacion(null);
        Persona personaCreada = personaRepository.save(persona);
        return personaMapper.mapToDTO(personaCreada);
    }

    @Override
    public PersonaDTO actualizarPersona(Integer idPersona, PersonaDTO personaDTO) {
        if(personaRepository.findById(idPersona).isPresent()){
            Persona persona = personaRepository.findById(idPersona).get();
            persona.setNombres(personaDTO.getNombres());
            persona.setApellidos(personaDTO.getApellidos());
            persona.setNumeroDocumento(personaDTO.getNumeroDocumento());
            persona.setTelefono(personaDTO.getTelefono());
            persona.setFechaNacimiento(personaDTO.getFechaNacimiento());
            persona.setTipoDocumento(tipoDocumentoMapper.mapToModel(personaDTO.getTipoDocumento()));
            persona.setFotoPerfil(personaDTO.getFotoPerfil());
            Persona personaModificada = personaRepository.save(persona);
            return personaMapper.mapToDTO(personaModificada);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarPersona(Integer idPersona) {
        Optional<Persona> persona = personaRepository.findById(idPersona);
        if(persona.isPresent()) {
            Persona personaEliminar = persona.get();
            personaEliminar.setActivo(false);
            personaEliminar.setFechaDesactivacion(LocalDateTime.now());
            personaRepository.save(personaEliminar);
        }
    }

    @Override
    public List<PersonaDTO> listarPersonasTodas() {
        List<Persona> personas = personaRepository.findAll();
        if(personas.isEmpty()) {
            return null;
        } else {
            return personas.stream().map(personaMapper::mapToDTO).toList();
        }
    }

    @Override
    public PersonaDTO buscarPersonaPorId(Integer idPersona) {
        if(personaRepository.findById(idPersona).isPresent()) {
            Persona personaBuscada = personaRepository.findById(idPersona).get();
            return personaMapper.mapToDTO(personaBuscada);
        } else {
            return null;
        }
    }
}
