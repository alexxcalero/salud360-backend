package pe.edu.pucp.salud360.comunidad.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.repositories.MembresiaRepository;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;
import pe.edu.pucp.salud360.servicio.models.Servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComunidadServiceImp implements ComunidadService {

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Autowired
    private ComunidadMapper comunidadMapper;

    @Autowired//listarxservicio
    private ServicioMapper servicioMapper;

    @Autowired//listarxmembre
    private MembresiaRepository membresiaRepository;

    @Autowired
    private MembresiaMapper membresiaMapper;

    @Autowired
    private S3UrlGenerator s3UrlGenerator;

    @Override
    public ComunidadDTO crearComunidad(ComunidadDTO dto) {
        List<String> urls = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        Comunidad comunidad = comunidadMapper.mapToModel(dto);

        // Procesar imágenes
        if (comunidad.getImagenes() != null) {
            for (String imagen : comunidad.getImagenes()) {
                String url = s3UrlGenerator.generarUrl(imagen);//Genera urls
                urls.add(url);
                keys.add(s3UrlGenerator.extraerKeyDeUrl(url));//Saca la key del archivo
            }
        }
        comunidad.setImagenes(keys); //Guarda las keys para la bd
        comunidad.setFechaCreacion(LocalDateTime.now());
        comunidad.setActivo(true);
        Comunidad guardada = comunidadRepository.save(comunidad);

        // Guardar membresías asociadas
        if (dto.getMembresias() != null && !dto.getMembresias().isEmpty()) {
            for (MembresiaResumenDTO m : dto.getMembresias()) {
                Membresia membresia = Membresia.builder()
                    .nombre(m.getNombre())
                    .tipo(m.getTipo())
                    .cantUsuarios(m.getCantUsuarios())
                    .maxReservas(m.getMaxReservas())
                    .precio(m.getPrecio())
                    .descripcion(m.getDescripcion())
                    .icono(m.getIcono())
                    .comunidad(guardada)
                    .activo(true)
                    .fechaCreacion(LocalDateTime.now())
                    .build();
                membresiaRepository.save(membresia);
            }
        }

        guardada.setImagenes(urls);
        return comunidadMapper.mapToDTO(guardada);
    }


    @Override
    public ComunidadDTO actualizarComunidad(Integer id, ComunidadDTO dto) {
        Optional<Comunidad> optional = comunidadRepository.findById(id);
        if (optional.isEmpty()) return null;

        Comunidad comunidad = optional.get();
        comunidad.setNombre(dto.getNombre());
        comunidad.setDescripcion(dto.getDescripcion());
        comunidad.setProposito(dto.getProposito());
        comunidad.setImagenes(dto.getImagenes());
        comunidad.setCantMiembros(dto.getCantMiembros());
        comunidad.setCalificacion(dto.getCalificacion());

        return comunidadMapper.mapToDTO(comunidadRepository.save(comunidad));
    }

    @Override
    public boolean eliminarComunidad(Integer id) {
        Optional<Comunidad> optional = comunidadRepository.findById(id);
        if (optional.isEmpty()) return false;

        Comunidad comunidad = optional.get();
        comunidad.setActivo(false);
        comunidad.setFechaDesactivacion(LocalDateTime.now());
        comunidadRepository.save(comunidad);
        return true;
    }

    @Override
    public ComunidadDTO obtenerComunidadPorId(Integer id) {
        Comunidad comunidad = comunidadRepository.findById(id).orElse(null);
        List<String> urls = new ArrayList<>(), imagenes = comunidad.getImagenes();
        if(imagenes != null) {
            for(String key : imagenes) urls.add(s3UrlGenerator.generarUrlLectura(key));
            comunidad.setImagenes(urls);
        }
        return comunidadMapper.mapToDTO(comunidad);
    }

    @Override
    public List<ComunidadDTO> listarComunidades() {
        List<Comunidad> comunidades = comunidadRepository.findAll();
        if(!(comunidades.isEmpty())) {
            return comunidades.stream().map(comunidadMapper::mapToDTO).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean restaurarComunidad(Integer id) {
        Optional<Comunidad> optional = comunidadRepository.findById(id);
        if (optional.isEmpty()) return false;

        Comunidad comunidad = optional.get();
        comunidad.setActivo(true);
        comunidad.setFechaDesactivacion(null);
        comunidadRepository.save(comunidad);
        return true;
    }
}

