package pe.edu.pucp.salud360.servicio.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.mappers.LocalMapper;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.LocalRepository;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.servicio.services.LocalService;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
//Para la carga masiva
import java.io.InputStream;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.common.record.Record;
import java.io.IOException;

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
        String nombre = dto.getNombre().trim();
        Integer idServicio = dto.getServicio().getIdServicio();

        // Validar si ya existe un local con ese nombre y servicio
        boolean existe = localRepository.existsByNombreAndServicio_IdServicio(nombre, idServicio);
        if (existe) {
            throw new RuntimeException("Ya existe un local con ese nombre para el servicio seleccionado.");
        }

        // Convertir DTO a entidad
        Local local = localMapper.mapToModel(dto);

        // Buscar el servicio
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Setear datos del local
        local.setServicio(servicio);
        local.setFechaCreacion(LocalDateTime.now());
        local.setActivo(true);

        // Guardar y devolver el DTO de respuesta
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
        local.setImagen(dto.getImagen());
        local.setTipoServicio(dto.getTipoServicio());
        local.setDescripcion(dto.getDescripcion());

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
    public void reactivarLocal(Integer idLocal) {
        Local local = localRepository.findById(idLocal).orElse(null);
        if (local != null) {
            local.setActivo(true);
            local.setFechaDesactivacion(null);
            localRepository.save(local);
        }
    }

    @Override
    public List<LocalVistaAdminDTO> listarLocalesTodos() {
        return localRepository.findAll().stream()
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
                .map(localMapper::mapToVistaAdminDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public Boolean cargarMasivamante(MultipartFile file) throws IOException {
        //LOGICA DEL VIDEO
        List<Local> listaLocales= new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        List<Record> parseAllRecords= parser.parseAllRecords(inputStream);
        parseAllRecords.forEach(record -> {
            Local local = new Local();
            //Lectura de los parametros que aparecen en el CSV
            //nombre,direccion,telefono,tipo_servicio,id_servicio,descripcion
            local.setNombre(record.getString("nombre"));
            local.setDireccion(record.getString("direccion"));
            local.setTelefono(record.getString("telefono"));
            local.setTipoServicio(record.getString("tipo_servicio"));
            // COMO TENEMOS QUE ASOCIAR UN ID DE UN SERVICIO EXISTENTE, LO BUSCAMOS
            Integer idServicio = Integer.parseInt(record.getString("id_servicio"));
            Servicio servicio = servicioRepository.findById(idServicio)
                    .orElseThrow(() -> new RuntimeException("Servicio con ID " + idServicio + " no encontrado"));
            local.setServicio(servicio);

            local.setDescripcion(record.getString("descripcion"));

            //Datos crudos que debemos insertar
            local.setActivo(true);
            local.setFechaCreacion(LocalDateTime.now());
            //Agregamos el local
            listaLocales.add(local);
        });
        //El safeAll
        localRepository.saveAll(listaLocales);
        return true;
    }

}
