package pe.edu.pucp.salud360.comunidad.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.repositories.MembresiaRepository;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;
import pe.edu.pucp.salud360.servicio.models.*;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComunidadServiceImp implements ComunidadService {

    private final ComunidadRepository comunidadRepository;
    private final ComunidadMapper comunidadMapper;
    private final ServicioMapper servicioMapper;
    private final MembresiaRepository membresiaRepository;
    private final MembresiaMapper membresiaMapper;
    private final S3UrlGenerator s3UrlGenerator;
    private final ReservaMapper reservaMapper;
    private final ServicioRepository servicioRepository;

    @Override
    public ComunidadDTO crearComunidad(ComunidadDTO dto) {
        List<String> urls = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        Comunidad comunidad = comunidadMapper.mapToModel(dto);

        // Procesar imágenes
        if (comunidad.getImagenes() != null) {
            for (String imagen : comunidad.getImagenes()) {
                String url = s3UrlGenerator.generarUrl(imagen);
                urls.add(url);
                keys.add(s3UrlGenerator.extraerKeyDeUrl(url));
            }
        }
        comunidad.setImagenes(keys); //Guarda las keys para la bd
        comunidad.setFechaCreacion(LocalDateTime.now());
        comunidad.setActivo(true);
        Comunidad guardada = comunidadRepository.save(comunidad);

        if (dto.getServicios() != null && !dto.getServicios().isEmpty()) {
            List<Servicio> servicios = dto.getServicios().stream()
                .map(s -> servicioRepository.findById(s.getIdServicio())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + s.getIdServicio())))
                .collect(Collectors.toList());

            guardada.setServicios(servicios);
        }

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
                    .conTope(m.getConTope())
                    .comunidad(guardada)
                    .activo(true)
                    .fechaCreacion(LocalDateTime.now())
                    .build();
                membresiaRepository.save(membresia);
            }
        }

        guardada = comunidadRepository.save(guardada);
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

        // 1. Actualizar membresías (elimina previas no incluidas si es reemplazo total)
        if (dto.getMembresias() != null) {
            List<Membresia> membresiasActualizadas = new ArrayList<>();
            List<Integer> idsDesdeFrontend = new ArrayList<>();

            for (MembresiaResumenDTO m : dto.getMembresias()) {
                Membresia membresia;

                if (m.getIdMembresia() != null) {
                    // Ya existe, buscar
                    membresia = membresiaRepository.findById(m.getIdMembresia()).orElse(null);
                    if (membresia == null) continue; // puede lanzar error si prefieres

                    idsDesdeFrontend.add(m.getIdMembresia());
                } else {
                    // Nueva membresía
                    membresia = new Membresia();
                    membresia.setFechaCreacion(LocalDateTime.now());
                    membresia.setComunidad(comunidad);
                    membresia.setActivo(true);
                }

                membresia.setNombre(m.getNombre());
                membresia.setTipo(m.getTipo());
                membresia.setConTope(m.getConTope());
                membresia.setCantUsuarios(m.getCantUsuarios());
                membresia.setMaxReservas(m.getMaxReservas());
                membresia.setPrecio(m.getPrecio());
                membresia.setDescripcion(m.getDescripcion());
                membresia.setIcono(m.getIcono());

                membresiasActualizadas.add(membresia);
            }

            // Buscar las actuales en BD asociadas a esta comunidad
            List<Membresia> existentes = membresiaRepository.findByComunidad(comunidad);

            // Filtrar las que ya no están en el frontend
            for (Membresia existente : existentes) {
                if (existente.getIdMembresia() != null && !idsDesdeFrontend.contains(existente.getIdMembresia())) {
                    membresiaRepository.delete(existente);
                }
            }

            membresiaRepository.saveAll(membresiasActualizadas);
            comunidad.setMembresias(membresiasActualizadas); // opcional: si usas lazy, puede omitirse
        }


        // 2. Actualizar servicios
        if (dto.getServicios() != null) {
            List<Servicio> nuevosServicios = dto.getServicios().stream().map(s ->
                    Servicio.builder()
                            .idServicio(s.getIdServicio())
                            .build()
            ).toList();

            comunidad.getServicios().clear();
            comunidad.getServicios().addAll(nuevosServicios);
        }

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
        /*Comentar*/
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
    public List<ComunidadDTO> listarComunidadesActivas() {
        List<Comunidad> comunidades = comunidadRepository.findAll();
        return comunidades.stream()
                .filter(Comunidad::getActivo)
                .map(comunidadMapper::mapToDTO)
                .collect(Collectors.toList());
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
  
    @Override
    public ComunidadDTO obtenerComunidadAleatoriaExcluyendoCliente(Integer idCliente) {
        Comunidad entidad = comunidadRepository.findComunidadAleatoriaExcluyendoCliente(idCliente);
        return entidad != null ? comunidadMapper.mapToDTO(entidad) : null;
    }

    @Override
    public List<ReservaDTO> listarReservasPorComunidad(Integer idComunidad) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        List<Reserva> reservas = new ArrayList<>();

        for(Servicio servicio : comunidad.getServicios()) {

            // Reservas por clases de locales
            for(Local local : servicio.getLocales()) {
                for(Clase clase : local.getClases()) {
                    reservas.addAll(clase.getReservas());
                }
            }

            // Reservas por citas médicas del servicio directamente
            for(CitaMedica cita : servicio.getCitasMedicas()) {
                reservas.addAll(cita.getReservas());
            }
        }

        return reservas.stream()
                .map(reservaMapper::mapToDTO)
                .toList();
    }
}
