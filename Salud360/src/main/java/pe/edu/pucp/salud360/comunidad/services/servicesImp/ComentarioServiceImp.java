package pe.edu.pucp.salud360.comunidad.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO.ComentarioDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ComentarioMapper;
import pe.edu.pucp.salud360.comunidad.models.Comentario;
import pe.edu.pucp.salud360.comunidad.models.Publicacion;
import pe.edu.pucp.salud360.comunidad.repositories.ComentarioRepository;
import pe.edu.pucp.salud360.comunidad.repositories.PublicacionRepository;
import pe.edu.pucp.salud360.usuario.models.Persona;
import pe.edu.pucp.salud360.usuario.repositories.PersonaRepository;
import pe.edu.pucp.salud360.comunidad.services.ComentarioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComentarioServiceImp implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Override
    public ComentarioDTO crearComentario(ComentarioDTO dto) {
        Comentario comentario = comentarioMapper.mapToModel(dto);

        Persona usuario = personaRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Publicacion publicacion = publicacionRepository.findById(dto.getIdPublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        comentario.setPersona(usuario);
        comentario.setPublicacion(publicacion);
        comentario.setFechaCreacion(LocalDateTime.now());
        comentario.setActivo(true);

        return comentarioMapper.mapToDTO(comentarioRepository.save(comentario));
    }

    @Override
    public ComentarioDTO actualizarComentario(Integer id, ComentarioDTO dto) {
        Optional<Comentario> optional = comentarioRepository.findById(id);
        if (optional.isEmpty()) return null;

        Comentario comentario = optional.get();
        comentario.setContenido(dto.getContenido());

        return comentarioMapper.mapToDTO(comentarioRepository.save(comentario));
    }

    @Override
    public boolean eliminarComentario(Integer id) {
        Optional<Comentario> optional = comentarioRepository.findById(id);
        if (optional.isEmpty()) return false;

        Comentario comentario = optional.get();
        comentario.setActivo(false);
        comentario.setFechaDesactivacion(LocalDateTime.now());
        comentarioRepository.save(comentario);
        return true;
    }

    @Override
    public ComentarioDTO buscarComentarioPorId(Integer id) {
        return comentarioRepository.findById(id)
                .filter(Comentario::getActivo)
                .map(comentarioMapper::mapToDTO)
                .orElse(null);
    }

    @Override
    public List<ComentarioDTO> listarComentarios() {
        return comentarioRepository.findAll().stream()
                .filter(Comentario::getActivo)
                .map(comentarioMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}


