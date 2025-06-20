package pe.edu.pucp.salud360.servicio.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaClienteDTO;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.servicio.services.ServicioService;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;

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

    @Autowired
    private S3UrlGenerator s3UrlGenerator;

    @Override
    public ServicioDTO crearServicio(ServicioDTO dto) {
        Servicio servicio = servicioMapper.mapToModel(dto);
        servicio.setActivo(true);
        // Procesar imágenes
        servicio.setImagen(dto.getImagen());
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
        servicio.setImagen(dto.getImagen());
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
                .peek(servicio -> {
                    if (servicio.getImagen() != null) {
                        servicio.setImagen(s3UrlGenerator.generarUrlLectura(servicio.getImagen()));
                    }
                })
                .map(servicioMapper::mapToVistaAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServicioVistaClienteDTO> listarServiciosClientes() {
        return servicioRepository.findAll().stream()
                .filter(servicio -> Boolean.TRUE.equals(servicio.getActivo()))
                .peek(servicio -> {
                    if (servicio.getImagen() != null) {
                        servicio.setImagen(s3UrlGenerator.generarUrlLectura(servicio.getImagen()));
                    }
                })
                .map(servicioMapper::mapToVistaClienteDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ServicioVistaAdminDTO buscarServicioPorId(Integer id) {
        Servicio servicio = servicioRepository.findById(id).orElse(null);
        if (servicio == null) return null;

        if (servicio.getImagen() != null) {
            servicio.setImagen(s3UrlGenerator.generarUrlLectura(servicio.getImagen()));
        }

        return servicioMapper.mapToVistaAdminDTO(servicio);
    }
}
