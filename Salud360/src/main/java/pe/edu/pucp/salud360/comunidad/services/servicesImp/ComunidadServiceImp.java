package pe.edu.pucp.salud360.comunidad.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Foro;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.repositories.ForoRepository;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ComunidadServiceImp implements ComunidadService {

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Autowired
    private ForoRepository foroRepository;

    @Autowired
    private S3UrlGenerator s3UrlGenerator;

    @Override
    public ComunidadDTO crearComunidad(ComunidadDTO dto) {
        List<String> urls = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        Foro foro = foroRepository.findById(dto.getIdForo()).orElse(null);
        Comunidad comunidad = ComunidadMapper.mapToModel(dto, foro);
        for(String imagen : comunidad.getImagenes()) {
            String url = s3UrlGenerator.generarUrl(imagen); //Genera urls
            urls.add(url); //Añade las url
            keys.add(s3UrlGenerator.extraerKeyDeUrl(url)); //Saca la key del archivo
        }
        comunidad.setImagenes(keys); //Guarda las keys para la bd
        comunidad.setFechaCreacion(LocalDateTime.now());
        Comunidad guardada = comunidadRepository.save(comunidad); //Guarda la comunidad
        guardada.setImagenes(urls); //Manda las urls por DTO
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
        List<String> urls = new ArrayList<>(), imagenes = comunidad.getImagenes();
        if(imagenes != null) {
            for(String key : imagenes) urls.add(s3UrlGenerator.generarUrlLectura(key));
            comunidad.setImagenes(urls);
        }
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
