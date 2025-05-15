package pe.edu.pucp.salud360.comunidad.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.dto.ForoDTO.ForoDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ForoMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Foro;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.repositories.ForoRepository;
import pe.edu.pucp.salud360.comunidad.services.ForoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForoServiceImp implements ForoService {

    @Autowired
    private ForoRepository foroRepository;

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Autowired
    private ForoMapper foroMapper;

    @Override
    public ForoDTO crearForo(ForoDTO dto) {
        Integer idComunidad = dto.getComunidad().getIdComunidad();
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        Foro foro = foroMapper.mapToModel(dto);
        foro.setComunidad(comunidad);
        foro.setFechaCreacion(LocalDateTime.now());
        foro.setActivo(true);

        return foroMapper.mapToDTO(foroRepository.save(foro));
    }

    @Override
    public ForoDTO actualizarForo(Integer id, ForoDTO dto) {
        Optional<Foro> optional = foroRepository.findById(id);
        if (optional.isEmpty()) return null;

        Foro foro = optional.get();
        foro.setTitulo(dto.getTitulo());
        foro.setDescripcion(dto.getDescripcion());
        foro.setCantPublicaciones(dto.getCantPublicaciones());

        return foroMapper.mapToDTO(foroRepository.save(foro));
    }

    @Override
    public boolean eliminarForo(Integer id) {
        Optional<Foro> optional = foroRepository.findById(id);
        if (optional.isEmpty()) return false;

        Foro foro = optional.get();
        foro.setActivo(false);
        foro.setFechaDesactivacion(LocalDateTime.now());
        foroRepository.save(foro);
        return true;
    }

    @Override
    public ForoDTO buscarPorId(Integer id) {
        return foroRepository.findById(id)
                .filter(Foro::getActivo)
                .map(foroMapper::mapToDTO)
                .orElse(null);
    }

    @Override
    public List<ForoDTO> listarTodos() {
        return foroRepository.findAll().stream()
                .filter(Foro::getActivo)
                .map(foroMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}

