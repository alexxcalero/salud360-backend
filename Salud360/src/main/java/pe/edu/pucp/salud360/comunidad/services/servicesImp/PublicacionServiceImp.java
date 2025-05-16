package pe.edu.pucp.salud360.comunidad.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.dto.PublicacionDTO.PublicacionDTO;
import pe.edu.pucp.salud360.comunidad.mappers.PublicacionMapper;
import pe.edu.pucp.salud360.comunidad.models.Foro;
import pe.edu.pucp.salud360.comunidad.models.Publicacion;
import pe.edu.pucp.salud360.comunidad.repositories.ForoRepository;
import pe.edu.pucp.salud360.comunidad.repositories.PublicacionRepository;
import pe.edu.pucp.salud360.comunidad.services.PublicacionService;
import pe.edu.pucp.salud360.usuario.models.Persona;
import pe.edu.pucp.salud360.usuario.repositories.PersonaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicacionServiceImp implements PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private PublicacionMapper publicacionMapper;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ForoRepository foroRepository;

    @Override
    public PublicacionDTO crearPublicacion(PublicacionDTO dto) {
        Publicacion publicacion = publicacionMapper.mapToModel(dto);

        Persona autor = personaRepository.findById(dto.getAutor().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Foro foro = foroRepository.findById(dto.getIdForo())
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));

        publicacion.setPersona(autor);
        publicacion.setForo(foro);
        publicacion.setFechaCreacion(LocalDateTime.now());
        publicacion.setActivo(true);

        return publicacionMapper.mapToDTO(publicacionRepository.save(publicacion));
    }

    @Override
    public List<PublicacionDTO> listarPublicaciones() {
        return publicacionRepository.findAll().stream()
                .filter(Publicacion::getActivo)
                .map(publicacionMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PublicacionDTO obtenerPublicacionPorId(Integer id) {
        return publicacionRepository.findById(id)
                .filter(Publicacion::getActivo)
                .map(publicacionMapper::mapToDTO)
                .orElse(null);
    }

    @Override
    public PublicacionDTO actualizarPublicacion(Integer id, PublicacionDTO dto) {
        Optional<Publicacion> optional = publicacionRepository.findById(id);
        if (optional.isEmpty()) return null;

        Publicacion publicacion = optional.get();
        publicacion.setContenido(dto.getContenido());

        return publicacionMapper.mapToDTO(publicacionRepository.save(publicacion));
    }

    @Override
    public boolean eliminarPublicacion(Integer id) {
        Optional<Publicacion> optional = publicacionRepository.findById(id);
        if (optional.isEmpty()) return false;

        Publicacion publicacion = optional.get();
        publicacion.setActivo(false);
        publicacion.setFechaDesactivacion(LocalDateTime.now());
        publicacionRepository.save(publicacion);
        return true;
    }
}
