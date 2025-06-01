package pe.edu.pucp.salud360.servicio.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.mappers.LocalMapper;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.LocalRepository;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.servicio.services.LocalService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocalServiceImp implements LocalService {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private LocalMapper localMapper;

    @Override
    public LocalVistaAdminDTO crearLocal(LocalVistaAdminDTO dto) {
        Local local = localMapper.mapToModel(dto);

        // Extraer el id del servicio desde el DTO anidado
        Integer idServicio = dto.getServicio().getIdServicio();
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        local.setServicio(servicio);
        local.setFechaCreacion(LocalDateTime.now());
        local.setActivo(true);

        return localMapper.mapToVistaAdminDTO(localRepository.save(local));
    }

    @Override
    public LocalVistaAdminDTO actualizarLocal(Integer id, LocalVistaAdminDTO dto) {
        Optional<Local> optional = localRepository.findById(id);
        if (optional.isEmpty()) return null;

        Local local = optional.get();

        local.setNombre(dto.getNombre());
        local.setDireccion(dto.getDireccion());
        local.setTelefono(dto.getTelefono());
        local.setImagenes(dto.getImagenes());
        local.setTipoServicio(dto.getTipoServicio());

        // Actualizar servicio si viene incluido
        Integer idServicio = dto.getServicio().getIdServicio();
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        local.setServicio(servicio);

        return localMapper.mapToVistaAdminDTO(localRepository.save(local));
    }

    @Override
    public void eliminarLocal(Integer id) {
        Optional<Local> optional = localRepository.findById(id);
        if (optional.isPresent()) {
            Local local = optional.get();
            local.setActivo(false);
            local.setFechaDesactivacion(LocalDateTime.now());
            localRepository.save(local);
        }
    }

    @Override
    public List<LocalVistaAdminDTO> listarLocalesTodos() {
        return localRepository.findAll().stream()
                .filter(Local::getActivo)
                .map(localMapper::mapToVistaAdminDTO)
                .collect(Collectors.toList());
    }
    /*Comentar*/
    @Override
    public List<LocalDTO> listarLocalesResumen() {
        return localRepository.findAll().stream()
                .filter(Local::getActivo)
                .map(localMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LocalVistaAdminDTO buscarLocalPorId(Integer id) {
        return localRepository.findById(id)
                .filter(Local::getActivo)
                .map(localMapper::mapToVistaAdminDTO)
                .orElse(null);
    }

    @Override
    public Page<LocalVistaAdminDTO> listarLocalesPaginado(Pageable pageable) {
        return localRepository.findByActivoTrue(pageable)
                .map(localMapper::mapToVistaAdminDTO);
    }
}
