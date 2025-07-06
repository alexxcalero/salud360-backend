package pe.edu.pucp.salud360.comunidad.services.servicesImp;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;
import pe.edu.pucp.salud360.control.models.ReglasDeNegocio;
import pe.edu.pucp.salud360.control.repositories.ReglasDeNegocioRepository;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.repositories.MembresiaRepository;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;
import pe.edu.pucp.salud360.servicio.models.*;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
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
    private final ClaseMapper claseMapper;
    private final CitaMedicaMapper citaMedicaMapper;
    private final ReglasDeNegocioRepository reglasDeNegocioRepository;

    @Override
    @Transactional
    public ComunidadDTO crearComunidad(ComunidadDTO dto) {
        List<String> urls = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        Comunidad comunidad = comunidadMapper.mapToModel(dto);
        ReglasDeNegocio reglas = reglasDeNegocioRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Regla de negocio no encontrada"));


        // Procesar imágenes
        comunidad.setImagen(dto.getImagen());
        comunidad.setCantMiembros(0);
        comunidad.setCalificacion(0.0);
        comunidad.setFechaCreacion(LocalDateTime.now());
        comunidad.setActivo(true);
        comunidad.setTestimonios(new ArrayList<>());
        comunidad.setClientes(new ArrayList<>());
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
                    .cantUsuarios(0)
                    .precio(m.getPrecio())
                    .descripcion(m.getDescripcion())
                    .conTope(m.getConTope())
                    .comunidad(guardada)
                    .activo(true)
                    .fechaCreacion(LocalDateTime.now())
                    .build();
                if(membresia.getConTope())
                    membresia.setMaxReservas(reglas.getMaxReservas());  // Aca se anhade el valor que este en la tabla de reglas generales
                else
                    membresia.setMaxReservas(-1);  // Valor por defecto cuando la membresia sea sin tope
                membresiaRepository.save(membresia);
            }
        }

        guardada = comunidadRepository.save(guardada);
        return comunidadMapper.mapToDTO(guardada);
    }


    @Override
    @Transactional
    public ComunidadDTO actualizarComunidad(Integer id, ComunidadDTO dto) {
        Optional<Comunidad> optional = comunidadRepository.findById(id);
        if (optional.isEmpty()) return null;

        Comunidad comunidad = optional.get();
        comunidad.setNombre(dto.getNombre());
        comunidad.setDescripcion(dto.getDescripcion());
        comunidad.setProposito(dto.getProposito());
        comunidad.setImagen(dto.getImagen());
        //comunidad.setCantMiembros(dto.getCantMiembros());
        //comunidad.setCalificacion(dto.getCalificacion());

        if (dto.getMembresias() != null) {
            List<Membresia> membresiasActualizadas = new ArrayList<>();
            List<Integer> idsDesdeFrontend = new ArrayList<>();

            ReglasDeNegocio reglas = reglasDeNegocioRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Regla de negocio no encontrada"));

            for (MembresiaResumenDTO m : dto.getMembresias()) {
                Membresia membresia;

                if (m.getIdMembresia() != null) {
                    // Ya existe, buscar
                    membresia = membresiaRepository.findById(m.getIdMembresia()).orElse(null);
                    if (membresia == null) continue;

                    idsDesdeFrontend.add(m.getIdMembresia());

                    // Bloquear edicion si tiene usuarios asociados
                    if (membresia.getCantUsuarios() != null && membresia.getCantUsuarios() > 0) {
                        throw new IllegalStateException("No se puede editar la membresía '" + membresia.getNombre() + "' porque tiene usuarios asociados.");
                    }
                } else {
                    // Nueva membresía
                    membresia = new Membresia();
                    membresia.setFechaCreacion(LocalDateTime.now());
                    membresia.setComunidad(comunidad);
                    membresia.setActivo(true);
                    membresia.setCantUsuarios(0); // al inicio debe ser 0

                    if (m.getConTope()) {
                        membresia.setConTope(true);
                        membresia.setMaxReservas(reglas.getMaxReservas());
                    } else {
                        membresia.setConTope(false);
                        membresia.setMaxReservas(-1);
                    }
                }

                membresia.setNombre(m.getNombre());
                membresia.setTipo(m.getTipo());
                membresia.setPrecio(m.getPrecio());
                membresia.setDescripcion(m.getDescripcion());

                membresiasActualizadas.add(membresia);
            }

            // Eliminar las membresías no incluidas
            List<Membresia> existentes = membresiaRepository.findByComunidad(comunidad);
            for (Membresia existente : existentes) {
                if (existente.getIdMembresia() != null && !idsDesdeFrontend.contains(existente.getIdMembresia())) {
                    // Verificar si la membresiaa tiene usuarios asociados
                    if (existente.getCantUsuarios() != null && existente.getCantUsuarios() > 0) {
                        throw new IllegalStateException("No se puede eliminar la membresía '" + existente.getNombre() + "' porque tiene usuarios asociados.");
                    }

                    membresiaRepository.delete(existente);
                }
            }

            membresiaRepository.saveAll(membresiasActualizadas);
            comunidad.setMembresias(membresiasActualizadas);
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

        if(comunidad.getClientes().isEmpty()) {
            comunidad.setActivo(false);
            comunidad.setFechaDesactivacion(LocalDateTime.now());
            comunidadRepository.save(comunidad);
            return true;
        } else {
            throw new IllegalStateException("No se puede eliminar esta comunidad, debido a que tiene miembros en ella.");
        }
    }

    @Override
    public ComunidadDTO obtenerComunidadPorId(Integer id) {
        Comunidad comunidad = comunidadRepository.findById(id).orElse(null);
        if (comunidad == null) return null;

        // Generar URL de lectura para la imagen única
        if (comunidad.getImagen() != null) {
            comunidad.setImagen(s3UrlGenerator.generarUrlLectura(comunidad.getImagen()));
        }
        return comunidadMapper.mapToDTO(comunidad);
    }

    @Override
    public List<ComunidadDTO> listarComunidades() {
        List<Comunidad> comunidades = comunidadRepository.findAll();
        for (Comunidad comunidad : comunidades) {
            if (comunidad.getImagen() != null) {
                comunidad.setImagen(s3UrlGenerator.generarUrlLectura(comunidad.getImagen()));
            }
        }

        return comunidades.stream()
                .map(comunidadMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComunidadDTO> listarComunidadesActivas() {
        List<Comunidad> comunidades = comunidadRepository.findAll();
        return comunidades.stream()
                .filter(Comunidad::getActivo)
                .peek(c -> {
                    if (c.getImagen() != null) {
                        c.setImagen(s3UrlGenerator.generarUrlLectura(c.getImagen()));
                    }
                })
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
        if (entidad != null && entidad.getImagen() != null) {
            entidad.setImagen(s3UrlGenerator.generarUrlLectura(entidad.getImagen()));
        }

        return entidad != null ? comunidadMapper.mapToDTO(entidad) : null;
    }

    @Override
    public List<ComunidadDTO> obtenerComunidadesExcluyendoCliente(Integer idCliente) {
        List<Comunidad> entidades = comunidadRepository.findComunidadesExcluyendoCliente(idCliente);
        return entidades.stream()
                .map(comunidadMapper::mapToDTO)
                .toList();
    }

    @Override
    public List<ComunidadDTO> obtenerComunidadesInactivasCliente(Integer idCliente) {
        return comunidadRepository.findComunidadesInactivasDeCliente(idCliente)
                .stream().map(comunidadMapper::mapToDTO).toList();
    }

    @Override
    public List<ComunidadDTO> obtenerComunidadesActivasCliente(Integer idCliente) {
        return comunidadRepository.findComunidadesActivasDeCliente(idCliente)
                .stream().map(comunidadMapper::mapToDTO).toList();
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

    @Override
    public List<ClaseResumenDTO> listarClasesPorComunidad(Integer idComunidad) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        List<Clase> clases = new ArrayList<>();

        for(Servicio servicio : comunidad.getServicios()) {
            for(Local local : servicio.getLocales()) {
                for(Clase clase : local.getClases()) {
                    if(clase.getActivo())  // Solo voy a mostrar las clases activas al publico
                        clases.add(clase);
                }
            }
        }

        return clases.stream()
                .map(claseMapper::mapToResumenDTO)
                .toList();
    }

    @Override
    public List<CitaMedicaResumenDTO> listarCitasMedicasPorComunidad(Integer idComunidad) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        List<CitaMedica> citas = new ArrayList<>();

        for(Servicio servicio : comunidad.getServicios()) {
            for(CitaMedica cita : servicio.getCitasMedicas()) {
                if(cita.getActivo())  // Solo voy a mostrar las citas activas al publico
                    citas.add(cita);
            }
        }

        return citas.stream()
                .map(citaMedicaMapper::mapToResumenDTO)
                .toList();
    }

    @Override
    @Transactional
    public Boolean cargarMasivamante(MultipartFile file) throws IOException {
        //LOGICA DEL VIDEO
        List<Comunidad> listaComunidades= new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        List<Record> parseAllRecords= parser.parseAllRecords(inputStream);
        parseAllRecords.forEach(record -> {
            Comunidad comunidad = new Comunidad();
            //Lectura de los parametros que aparecen en el CSV
            //nombre,direccion,telefono,tipo_servicio,id_servicio,descripcion
            comunidad.setNombre(record.getString("nombre"));
            comunidad.setDescripcion(record.getString("descripcion"));
            comunidad.setProposito(record.getString("proposito"));

            ReglasDeNegocio regla = reglasDeNegocioRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("No se encontró la regla de negocio con ID 1"));
            Integer capacidadMaxima = regla.getMaxCapacidad();
            //VERIFICAMOS QUE NO EXISTAN DATOS DUPLICADOS EN EL CSV
            for (Comunidad otraComunidad : listaComunidades) {
                if (comunidad.getNombre().equals(otraComunidad.getNombre()) &&
                        comunidad.getDescripcion().equals(otraComunidad.getDescripcion()) &&
                        comunidad.getProposito().equals(otraComunidad.getProposito())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Comunidad duplicado en el archivo CSV con nombre " + comunidad.getNombre());
                }
            }
            // VERIFICAMOS SI NO HAY DUPLICIDAD DE DATOS CON AQUELLOS REGISTRADOS EN LA BD
            List<Comunidad> comunidadesExistentes = comunidadRepository.
                    findByNombreAndDescripcionAndProposito(comunidad.getNombre(),comunidad.getDescripcion(),comunidad.getProposito());

            for (Comunidad comunidadExistente : comunidadesExistentes) {
                if (comunidad.getNombre().equals(comunidadExistente.getNombre()) &&
                        comunidad.getDescripcion().equals(comunidadExistente.getDescripcion()) &&
                        comunidad.getProposito().equals(comunidadExistente.getProposito())){
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "La comunidad '" + comunidad.getNombre() +
                                    "' ya se encuentra registrado en la base de datos");
                }
            }

//          COMO PODEMOS ASOCIAR VARIOS SERVICIOS A UNA COMUNIDAD:
            String idsServiciosStr = record.getString("id_servicios"); // ej. "1,2,3"
            if (idsServiciosStr != null && !idsServiciosStr.isBlank()) {
                String[] idStrings = idsServiciosStr.split("\\|");
                Set<Servicio> servicios = Arrays.stream(idStrings)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .map(id -> servicioRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Servicio con ID " + id + " no encontrado")))
                        .collect(Collectors.toSet());

                comunidad.setServicios(new ArrayList<>(servicios));
            }

            // ASOCIAMOS MEMBRESIAS DESDE EL CSV
            String membresiasStr = record.getString("membresias");
            if (membresiasStr == null || membresiasStr.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cada comunidad debe tener al menos una membresía asociada.");
            }

            List<Membresia> membresias = Arrays.stream(membresiasStr.split("\\|"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(m -> {
                        String[] partes = m.split("-");
                        if (partes.length < 5) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Formato inválido en membresía: se esperan 5 campos separados por '-'");
                        }

                        Membresia mem = new Membresia();
                        mem.setNombre(partes[0].trim());
                        mem.setTipo(partes[1].trim());
                        mem.setConTope(partes[2].trim().equalsIgnoreCase("Si"));



                        mem.setPrecio(Double.parseDouble(partes[3].trim()));
                        mem.setDescripcion(partes[4].trim());
                        mem.setFechaCreacion(LocalDateTime.now());
                        mem.setActivo(true);
                        mem.setCantUsuarios(0);

                        if(partes[2].trim().equalsIgnoreCase("Si")){
                            mem.setMaxReservas(capacidadMaxima);
                        }else
                            mem.setMaxReservas(-1);

                        mem.setComunidad(comunidad); // Importante para establecer relación bidireccional

                        return mem;
                    })
                    .collect(Collectors.toList());
            //Lo añadimos
            comunidad.setMembresias(membresias);


            //Datos crudos que debemos insertar
            comunidad.setActivo(true);
            comunidad.setFechaCreacion(LocalDateTime.now());
            comunidad.setCalificacion(0.0);
            comunidad.setCantMiembros(0);
            //Agregamos el local
            listaComunidades.add(comunidad);
        });
        //El safeAll
        comunidadRepository.saveAll(listaComunidades);
        // Guardar membresías por comunidad
        for (Comunidad comunidad : listaComunidades) {
            List<Membresia> membresias = comunidad.getMembresias();
            if (membresias != null && !membresias.isEmpty()) {
                membresiaRepository.saveAll(membresias);
            }
        }

        return true;
    }


}
