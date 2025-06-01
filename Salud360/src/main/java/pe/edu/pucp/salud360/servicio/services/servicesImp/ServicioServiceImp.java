package pe.edu.pucp.salud360.servicio.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.servicio.services.ServicioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicioServiceImp implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ServicioMapper servicioMapper;

    @Override
    public ServicioDTO crearServicio(ServicioDTO dto) {
        Servicio servicio = servicioMapper.mapToModel(dto);
        servicio.setActivo(true);
        servicio.setFechaCreacion(LocalDateTime.now());

        return servicioMapper.mapToDTO(servicioRepository.save(servicio));
    }

    @Override
    public ServicioDTO actualizarServicio(Integer id, ServicioDTO dto) {
        Optional<Servicio> optional = servicioRepository.findById(id);
        if (optional.isEmpty()) return null;

        Servicio servicio = optional.get();
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setImagenes(dto.getImagenes());
        servicio.setTipo(dto.getTipo());

        return servicioMapper.mapToDTO(servicioRepository.save(servicio));
    }

    @Override
    public void eliminarServicio(Integer id) {
        Optional<Servicio> optional = servicioRepository.findById(id);
        if (optional.isPresent()) {
            Servicio servicio = optional.get();
            servicio.setActivo(false);
            servicio.setFechaDesactivacion(LocalDateTime.now());
            servicioRepository.save(servicio);
        }
    }

    @Override
    public void reactivarServicio(Integer idServicio) {
        Servicio servicio = servicioRepository.findById(idServicio).orElse(null);
        if (servicio != null) {
            servicio.setActivo(true);
            servicio.setFechaDesactivacion(null);
            servicioRepository.save(servicio);
        }
    }

    @Override
    public List<ServicioVistaAdminDTO> listarServiciosTodos() {
        return servicioRepository.findAll().stream()
                .map(servicioMapper::mapToVistaAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioVistaAdminDTO buscarServicioPorId(Integer id) {
        return servicioRepository.findById(id)
                .map(servicioMapper::mapToVistaAdminDTO)
                .orElse(null);
    }
}
