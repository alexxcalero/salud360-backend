package pe.edu.pucp.salud360.servicio.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
import pe.edu.pucp.salud360.servicio.services.ClaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClaseServiceImp implements ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private ClaseMapper claseMapper;

    @Override
    public ClaseDTO crearClase(ClaseDTO dto) {
        Clase clase = claseMapper.mapToModel(dto);
        clase.setActivo(true);
        clase.setFechaCreacion(LocalDateTime.now());

        return claseMapper.mapToDTO(claseRepository.save(clase));
    }

    @Override
    public ClaseDTO actualizarClase(Integer idClase, ClaseDTO dto) {
        Optional<Clase> optional = claseRepository.findById(idClase);
        if (optional.isEmpty()) return null;

        Clase clase = optional.get();

        clase.setNombre(dto.getNombre());
        clase.setDescripcion(dto.getDescripcion());
        clase.setFecha(dto.getFecha());
        clase.setHoraInicio(dto.getHoraInicio());
        clase.setHoraFin(dto.getHoraFin());
        clase.setCapacidad(dto.getCapacidad());
        clase.setCantAsistentes(dto.getCantAsistentes());

        return claseMapper.mapToDTO(claseRepository.save(clase));
    }

    @Override
    public void eliminarClase(Integer idClase) {
        Optional<Clase> optional = claseRepository.findById(idClase);
        if (optional.isPresent()) {
            Clase clase = optional.get();
            clase.setActivo(false);
            clase.setFechaDesactivacion(LocalDateTime.now());
            claseRepository.save(clase);
        }
    }

    @Override
    public List<ClaseDTO> listarClasesTodas() {
        return claseRepository.findAll().stream()
                .filter(Clase::getActivo)
                .map(claseMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClaseDTO buscarClasePorId(Integer idClase) {
        return claseRepository.findById(idClase)
                .filter(Clase::getActivo)
                .map(claseMapper::mapToDTO)
                .orElse(null);
    }
}
