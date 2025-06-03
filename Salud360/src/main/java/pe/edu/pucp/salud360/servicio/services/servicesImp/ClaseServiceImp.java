package pe.edu.pucp.salud360.servicio.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;
import pe.edu.pucp.salud360.servicio.mappers.LocalMapper;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
import pe.edu.pucp.salud360.servicio.repositories.LocalRepository;
import pe.edu.pucp.salud360.servicio.services.ClaseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaseServiceImp implements ClaseService {

    private final ClaseRepository claseRepository;
    private final ClaseMapper claseMapper;
    private final LocalMapper localMapper;
    private final LocalRepository localRepository;

    @Override
    public ClaseDTO crearClase(ClaseDTO dto) {
        Local local = localRepository.findById(dto.getLocal().getIdLocal())
                .orElseThrow(() -> new RuntimeException("Local no encontrado"));

        Clase clase = claseMapper.mapToModel(dto);
        clase.setCantAsistentes(0);
        clase.setEstado("Disponible");
        clase.setActivo(true);
        clase.setFechaCreacion(LocalDateTime.now());
        clase.setClientes(new ArrayList<>());
        clase.setReservas(new ArrayList<>());
        clase.setLocal(localMapper.mapToModel(dto.getLocal()));
        clase.setLocal(local);

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
        clase.setEstado(dto.getEstado());

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
    public void reactivarClase(Integer idClase) {
        Optional<Clase> optional = claseRepository.findById(idClase);
        if (optional.isPresent()) {
            Clase clase = optional.get();
            clase.setActivo(true);
            clase.setFechaDesactivacion(null);
            claseRepository.save(clase);
        }
    }

    @Override
    public List<ClaseDTO> listarClasesTodas() {
        return claseRepository.findAll().stream()
                .map(claseMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClaseDTO buscarClasePorId(Integer idClase) {
        return claseRepository.findById(idClase)
                .map(claseMapper::mapToDTO)
                .orElse(null);
    }
}
