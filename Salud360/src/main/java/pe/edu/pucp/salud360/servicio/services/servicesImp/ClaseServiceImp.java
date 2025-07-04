package pe.edu.pucp.salud360.servicio.services.servicesImp;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.pucp.salud360.control.models.ReglasDeNegocio;
import pe.edu.pucp.salud360.control.repositories.ReglasDeNegocioRepository;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
import pe.edu.pucp.salud360.servicio.repositories.LocalRepository;
import pe.edu.pucp.salud360.servicio.repositories.ReservaRepository;
import pe.edu.pucp.salud360.servicio.services.ClaseService;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaseServiceImp implements ClaseService {

    private final ClaseRepository claseRepository;
    private final ClaseMapper claseMapper;
    private final LocalRepository localRepository;
    private final ReglasDeNegocioRepository reglasDeNegocioRepository;

    @Override
    @Transactional
    public ClaseDTO crearClase(ClaseDTO dto) {
        Local local = localRepository.findById(dto.getLocal().getIdLocal())
                .orElseThrow(() -> new RuntimeException("Local no encontrado"));

        ReglasDeNegocio reglas = reglasDeNegocioRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Regla de negocio no encontrada"));

        List<Clase> clasesDelLocal = local.getClases();

        Clase claseCreada = claseMapper.mapToModel(dto);
        LocalDate fechaClaseCreada = claseCreada.getFecha();
        LocalTime horaInicioClaseCreada = claseCreada.getHoraInicio();
        LocalTime horaFinClaseCreada = claseCreada.getHoraFin();

        for(Clase clase : clasesDelLocal) {
            if(clase.getEstado().equals("Finalizada")) continue;

            LocalDate fechaClase = clase.getFecha();
            LocalTime horaInicio = clase.getHoraInicio();
            LocalTime horaFin = clase.getHoraFin();
            if(fechaClase.equals(fechaClaseCreada)) {
                if(existeCruceDeHorarios(horaInicio, horaFin, horaInicioClaseCreada, horaFinClaseCreada))
                    throw new IllegalStateException("El local ya tiene una clase asignada para esa hora.");
            }
        }

        claseCreada.setCapacidad(reglas.getMaxCapacidad());
        claseCreada.setCantAsistentes(0);
        claseCreada.setEstado("Disponible");
        claseCreada.setActivo(true);
        claseCreada.setFechaCreacion(LocalDateTime.now());
        claseCreada.setLocal(local);
        claseCreada.setClientes(new ArrayList<>());
        claseCreada.setReservas(new ArrayList<>());

        return claseMapper.mapToDTO(claseRepository.save(claseCreada));
    }

    @Override
    @Transactional
    public ClaseDTO actualizarClase(Integer idClase, ClaseDTO dto) {
        Optional<Clase> optional = claseRepository.findById(idClase);
        if (optional.isEmpty()) return null;

        Clase clase = optional.get();

        for(Reserva r : clase.getReservas()) {
            if(r.getEstado().equals("Confirmada"))
                throw new IllegalStateException("No se puede actualizar esta clase, debido a que ya ha sido reservada por un cliente.");
        }

        Local local = clase.getLocal();
        List<Clase> clasesDelLocal = local.getClases();
        for(Clase c : clasesDelLocal) {
            if(c.getIdClase().equals(clase.getIdClase())) continue;

            if(c.getEstado().equals("Finalizada")) continue;

            LocalDate fechaClase = c.getFecha();
            LocalTime horaInicio = c.getHoraInicio();
            LocalTime horaFin = c.getHoraFin();
            if(fechaClase == dto.getFecha())
                if(existeCruceDeHorarios(horaInicio, horaFin, dto.getHoraInicio(), dto.getHoraFin()))
                    throw new IllegalStateException("El local ya tiene una clase asignada para esa hora.");
        }

        clase.setNombre(dto.getNombre());
        clase.setDescripcion(dto.getDescripcion());
        clase.setFecha(dto.getFecha());
        clase.setHoraInicio(dto.getHoraInicio());
        clase.setHoraFin(dto.getHoraFin());
        //clase.setCapacidad(dto.getCapacidad());  // No se debe modificar la capacidad
        //clase.setEstado(dto.getEstado());

        return claseMapper.mapToDTO(claseRepository.save(clase));
    }

    private boolean existeCruceDeHorarios(LocalTime horaInicio, LocalTime horaFin, LocalTime horaInicioNueva, LocalTime horaFinNueva) {
        return horaInicioNueva.isBefore(horaFin) && horaFinNueva.isAfter(horaInicio);
    }

    @Override
    public void eliminarClase(Integer idClase) {
        Optional<Clase> optional = claseRepository.findById(idClase);
        if (optional.isPresent()) {
            Clase clase = optional.get();

            for(Reserva r : clase.getReservas()) {
                if(r.getEstado().equals("Confirmada"))
                    throw new IllegalStateException("No se puede eliminar esta clase, debido a que ya ha sido reservada por un cliente.");
            }

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

    @Override
    @Transactional
    public Boolean cargarMasivamante(MultipartFile file) throws IOException {
        //LOGICA DEL VIDEO
        List<Clase> listaClases= new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        List<Record> parseAllRecords= parser.parseAllRecords(inputStream);


        //Como partimos de una logica de negocio, necesitamos buscarla o madnar error si no la hemos cargado:
        ReglasDeNegocio regla = reglasDeNegocioRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("No se encontró la regla de negocio con ID 1"));
        Integer capacidadMaxima = regla.getMaxCapacidad();

        parseAllRecords.forEach(record -> {
            Clase clase = new Clase();
            //Lectura de los parametros que aparecen en el CSV
            //nombre,direccion,telefono,tipo_servicio,id_servicio,descripcion
            clase.setNombre(record.getString("nombre"));
            clase.setDescripcion(record.getString("descripcion"));
            clase.setFecha(LocalDate.parse(record.getString("fecha"))); // formato: YYYY-MM-DD
            clase.setHoraInicio(LocalTime.parse(record.getString("hora_inicio"))); // formato: HH:mm:ss
            clase.setHoraFin(LocalTime.parse(record.getString("hora_fin")));

//            // COMO TENEMOS QUE ASOCIAR UN ID DE UN LOCAL EXISTENTE, LO BUSCAMOS
            Integer idLocal = Integer.parseInt(record.getString("id_local"));
            Local local = localRepository.findById(idLocal)
                    .orElseThrow(() -> new RuntimeException("Local con ID " + idLocal + " no encontrado"));
            clase.setLocal(local);


            //Datos crudos que debemos insertar
            clase.setActivo(true);
            clase.setCapacidad(capacidadMaxima);
            clase.setCantAsistentes(0);
            clase.setEstado("Disponible");
            clase.setFechaCreacion(LocalDateTime.now());

            //Realizamos validaciones para el cruce de horario de clases
            //DETECTA CRUCES DENTRO DEL CSV
            for (Clase otra : listaClases) {
                if (clase.getFecha().equals(otra.getFecha())
                        && clase.getLocal().getIdLocal().equals(otra.getLocal().getIdLocal())
                        && seCruzan(clase.getHoraInicio(), clase.getHoraFin(), otra.getHoraInicio(), otra.getHoraFin())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Cruce entre clases nuevas en el archivo CSV en la fecha " + clase.getFecha() + " para el local " + clase.getLocal().getNombre());
                }
            }

            // DETECTA CRUCES TENIENDO EN CUENTA CLASES YA EXISTENTES
            List<Clase> clasesExistentes = claseRepository.findByLocalIdLocalAndFecha(idLocal, clase.getFecha());
            for (Clase existente : clasesExistentes) {
                if (seCruzan(clase.getHoraInicio(), clase.getHoraFin(), existente.getHoraInicio(), existente.getHoraFin())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "La clase '" + clase.getNombre() + "' se cruza con una ya existente en la fecha " + clase.getFecha() + " para el local " + local.getNombre());
                }
            }

            //REVISAMOS SI LAS DURACIONES DE LAS CLASES SON DE 1 HORA EXACTAMENTE
            Duration duracion = Duration.between(clase.getHoraInicio(), clase.getHoraFin());
            if (!duracion.equals(Duration.ofHours(1))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La clase '" + clase.getNombre() + "' debe durar exactamente 1 hora.");
            }



            //Agregamos el local
            listaClases.add(clase);
        });
        //El safeAll
        claseRepository.saveAll(listaClases);
        return true;
    }
    //PARA VERIFICAR CRUCES DE HORAS EXISTENTES CON LOS DE CARGA MASIVA
    private boolean seCruzan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

}
