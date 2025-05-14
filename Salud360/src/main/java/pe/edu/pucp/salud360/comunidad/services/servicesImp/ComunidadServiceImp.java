package pe.edu.pucp.salud360.comunidad.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Foro;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.repositories.ForoRepository;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ComunidadServiceImp implements ComunidadService {

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Autowired
    private ForoRepository foroRepository;

    @Override
    public ComunidadDTO crearComunidad(ComunidadDTO dto) {
        Foro foro = foroRepository.findById(dto.getIdForo()).orElse(null);
        Comunidad comunidad = ComunidadMapper.mapToModel(dto, foro);
        comunidad.setFechaCreacion(LocalDateTime.now());
        Comunidad guardada = comunidadRepository.save(comunidad);
        return ComunidadMapper.mapToDTO(guardada);
    }

    @Override
    public ComunidadDTO actualizarComunidad(Integer id, ComunidadDTO dto) {
        Comunidad comunidad = comunidadRepository.findById(id).orElse(null);
        if (comunidad == null) return null;

        comunidad.setNombre(dto.getNombre());
        comunidad.setDescripcion(dto.getDescripcion());
        comunidad.setProposito(dto.getProposito());
        comunidad.setImagenes(dto.getImagen());
        comunidad.setActivo(dto.getActivo());
        comunidad.setFechaDesactivacion(dto.getFechaDesactivacion());

        Foro foro = foroRepository.findById(dto.getIdForo()).orElse(null);
        comunidad.setForo(foro);

        Comunidad actualizada = comunidadRepository.save(comunidad);
        return ComunidadMapper.mapToDTO(actualizada);
    }

    @Override
    public boolean eliminarComunidad(Integer id) {
        Optional<Comunidad> comunidadOpt = comunidadRepository.findById(id);
        if (comunidadOpt.isEmpty()) return false;

        Comunidad comunidad = comunidadOpt.get();
        comunidad.setActivo(false);
        comunidad.setFechaDesactivacion(LocalDateTime.now());

        comunidadRepository.save(comunidad);
        return true;
    }

    @Override
    public ComunidadDTO obtenerComunidadPorId(Integer id) {
        Comunidad comunidad = comunidadRepository.findById(id).orElse(null);
        return ComunidadMapper.mapToDTO(comunidad);
    }

    @Override
    public List<ComunidadDTO> listarComunidades() {
        return comunidadRepository.findAll().stream()
                .map(ComunidadMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean restaurarComunidad(Integer id) {
        Optional<Comunidad> comunidadOpt = comunidadRepository.findById(id);
        if (comunidadOpt.isPresent()) {
            Comunidad comunidad = comunidadOpt.get();
            comunidad.setActivo(true);
            comunidad.setFechaDesactivacion(null);
            comunidadRepository.save(comunidad);
            return true;
        }
        return false;
    }

}
