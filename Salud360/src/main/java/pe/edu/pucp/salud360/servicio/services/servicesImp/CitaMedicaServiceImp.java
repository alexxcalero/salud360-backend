package pe.edu.pucp.salud360.servicio.services.servicesImp;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapperHelper;
import pe.edu.pucp.salud360.servicio.models.*;
import pe.edu.pucp.salud360.servicio.repositories.CitaMedicaRepository;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.usuario.models.Medico;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.usuario.repositories.MedicoRepository;
import pe.edu.pucp.salud360.servicio.services.CitaMedicaService;

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
public class CitaMedicaServiceImp implements CitaMedicaService {

    @Autowired
    private CitaMedicaMapper citaMedicaMapper;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private CitaMedicaRepository citaMedicaRepository;

    @Override
    public CitaMedicaDTO crearCitaMedica(CitaMedicaDTO dto) {
        // Buscar las entidades necesarias
        Servicio servicio = servicioRepository.findById(dto.getServicio().getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedico().getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<CitaMedica> citasDelMedico = medico.getCitasMedicas();

        // Mapear el DTO a entidad
        CitaMedica citaCreada = citaMedicaMapper.mapToModel(dto);
        LocalDate fechaCitaCreada = citaCreada.getFecha();
        LocalTime horaInicioCitaCreada = citaCreada.getHoraInicio();
        LocalTime horaFinCitaCreada = citaCreada.getHoraFin();

        for(CitaMedica cita : citasDelMedico) {
            // Si una de las citas ya ha sido finalizada o atendida no se va a considerar para la evaluacion
            if(cita.getEstado().equals("Finalizada")) continue;

            LocalDate fechaCita = cita.getFecha();
            LocalTime horaInicio = cita.getHoraInicio();
            LocalTime horaFin = cita.getHoraFin();
            if(fechaCita.equals(fechaCitaCreada)) {
                if(existeCruceDeHorarios(horaInicio, horaFin, horaInicioCitaCreada, horaFinCitaCreada))
                    throw new IllegalStateException("El médico ya tiene una cita asignada para esa hora.");
            }
        }

        // Asignar relaciones no incluidas directamente en el mapeo
        citaCreada.setEstado("Disponible");
        citaCreada.setActivo(true);
        citaCreada.setFechaCreacion(LocalDateTime.now());
        citaCreada.setReservas(new ArrayList<>());
        citaCreada.setServicio(servicio);
        citaCreada.setMedico(medico);

        // Guardar y devolver DTO
        return citaMedicaMapper.mapToDTO(citaMedicaRepository.save(citaCreada));
    }

    @Override
    public CitaMedicaDTO actualizarCitaMedica(Integer id, CitaMedicaDTO dto) {
        Optional<CitaMedica> optional = citaMedicaRepository.findById(id);
        if (optional.isEmpty()) return null;

        CitaMedica cita = optional.get();

        // No se va a permitir la actualizacion si la cita ya ha sido reservada
        if(cita.getEstado().equals("Reservada"))
            throw new IllegalStateException("No se puede actualizar esta cita, debido a que ya ha sido reservada por un cliente.");

//        for(Reserva r : cita.getReservas()) {
//            if(r.getEstado().equals("Confirmada"))
//                throw new IllegalStateException("No se puede actualizar esta cita, debido a que ya ha sido reservada por un cliente.");
//        }


        // Se va hacer una verificacion al momento de actualizar los datos de la cita igual que cuando se crea
        Medico medico = cita.getMedico();
        List<CitaMedica> citasDelMedico = medico.getCitasMedicas();
        for(CitaMedica c : citasDelMedico) {
            // Se obvia la misma cita al momento de comparar
            if(c.getIdCitaMedica().equals(cita.getIdCitaMedica())) continue;

            // Si una de las citas ya ha sido finalizada o atendida no se va a considerar para la evaluacion
            if(c.getEstado().equals("Finalizada")) continue;

            LocalDate fechaCita = c.getFecha();
            LocalTime horaInicio = c.getHoraInicio();
            LocalTime horaFin = c.getHoraFin();
            if(fechaCita == dto.getFecha())
                if(existeCruceDeHorarios(horaInicio, horaFin, dto.getHoraInicio(), dto.getHoraFin()))
                    throw new IllegalStateException("El médico ya tiene una cita asignada para esa hora.");
        }

        // Solo se actualizan los campos editables (activo no se toca)
        cita.setFecha(dto.getFecha());
        cita.setHoraInicio(dto.getHoraInicio()); // asumimos que cambió de `horaInicio` a `hora` en el DTO
        cita.setHoraFin(dto.getHoraFin());
        //cita.setEstado(dto.getEstado());

        return citaMedicaMapper.mapToDTO(citaMedicaRepository.save(cita));
    }

    private boolean existeCruceDeHorarios(LocalTime horaInicio, LocalTime horaFin, LocalTime horaInicioNueva, LocalTime horaFinNueva) {
        return horaInicioNueva.isBefore(horaFin) && horaFinNueva.isAfter(horaInicio);
    }

    @Override
    public void eliminarCitaMedica(Integer id) {
        Optional<CitaMedica> optional = citaMedicaRepository.findById(id);
        if (optional.isPresent()) {
            CitaMedica cita = optional.get();

            // No se va a permitir la actualizacion si la cita ya ha sido reservada
            if(cita.getEstado().equals("Reservada"))
                throw new IllegalStateException("No se puede eliminar esta cita, debido a que ya ha sido reservada por un cliente.");

//            for(Reserva r : cita.getReservas()) {
//                if(r.getEstado().equals("Confirmada"))
//                    throw new IllegalStateException("No se puede eliminar esta cita, debido a que ya ha sido reservada por un cliente.");
//            }

            cita.setActivo(false);
            cita.setFechaDesactivacion(LocalDateTime.now());
            citaMedicaRepository.save(cita);
        }
    }

    @Override
    public void reactivarCitaMedica(Integer id) {
        Optional<CitaMedica> optional = citaMedicaRepository.findById(id);
        if (optional.isPresent()) {
            CitaMedica cita = optional.get();
            cita.setActivo(true);
            cita.setFechaDesactivacion(null);
            citaMedicaRepository.save(cita);
        }
    }

//    @Override
//    public List<CitaMedicaDTO> listarCitasMedicasTodas() {
//        return citaMedicaRepository.findAll().stream()
//                .map(citaMedicaMapper::mapToDTO)
//                .collect(Collectors.toList());
//    }
//
//
//    @Override
//    public CitaMedicaDTO buscarCitaMedicaPorId(Integer id) {
//        return citaMedicaRepository.findById(id)
//                .map(citaMedicaMapper::mapToDTO)
//                .orElse(null);
//    }
@Override
public List<CitaMedicaDTO> listarCitasMedicasTodas() {
    return citaMedicaRepository.findAll().stream()
            .map(c -> CitaMedicaMapperHelper.mapToDTOIncluyendoArchivo(citaMedicaMapper, c))
            .collect(Collectors.toList());
}

    @Override
    public CitaMedicaDTO buscarCitaMedicaPorId(Integer id) {
        return citaMedicaRepository.findById(id)
                .map(c -> CitaMedicaMapperHelper.mapToDTOIncluyendoArchivo(citaMedicaMapper, c))
                .orElse(null);
    }

    @Override
    @Transactional
    public Boolean cargarMasivamante(MultipartFile file) throws IOException {
        //LOGICA DEL VIDEO
        List<CitaMedica> listaCitasMedicas= new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        List<Record> parseAllRecords= parser.parseAllRecords(inputStream);
        parseAllRecords.forEach(record -> {
            CitaMedica citaMedica = new CitaMedica();
            //Lectura de los parametros que aparecen en el CSV

            citaMedica.setFecha(LocalDate.parse(record.getString("fecha"))); // formato: YYYY-MM-DD
            citaMedica.setHoraInicio(LocalTime.parse(record.getString("hora_inicio"))); // formato: HH:mm:ss
            citaMedica.setHoraFin(LocalTime.parse(record.getString("hora_fin")));

            // COMO TENEMOS QUE ASOCIAR UN ID DE UN MEDICO Y SERVICIO LO BUSCAMOS:
            // Buscar médico
            Integer idMedico = Integer.parseInt(record.getString("id_medico"));
            Medico medico = medicoRepository.findById(idMedico)
                    .orElseThrow(() -> new RuntimeException("Médico con ID " + idMedico + " no encontrado"));
            citaMedica.setMedico(medico);

            // Buscar servicio
            Integer idServicio = Integer.parseInt(record.getString("id_servicio"));
            Servicio servicio = servicioRepository.findById(idServicio)
                    .orElseThrow(() -> new RuntimeException("Servicio con ID " + idServicio + " no encontrado"));
            citaMedica.setServicio(servicio);

            //Datos crudos que debemos insertar
            citaMedica.setActivo(true);
            citaMedica.setFechaCreacion(LocalDateTime.now());
            citaMedica.setEstado("Disponible");
            //Realizamos validaciones para el cruce de horario de clases
            //DETECTA CRUCES DENTRO DEL CSV
            for (CitaMedica otra : listaCitasMedicas) {
                if (citaMedica.getFecha().equals(otra.getFecha()) &&
                        citaMedica.getMedico().getIdMedico().equals(otra.getMedico().getIdMedico()) &&
                        seCruzan(
                                citaMedica.getHoraInicio(),
                                citaMedica.getHoraFin(),
                                otra.getHoraInicio(),
                                otra.getHoraFin())) {

                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Cruce entre citas nuevas en el archivo CSV en la fecha " + citaMedica.getFecha() + " para el médico " + citaMedica.getMedico().getNombres());
                }
            }

            // DETECTA CRUCES TENIENDO EN CUENTA CLASES YA EXISTENTES
            List<CitaMedica> citasExistentes = citaMedicaRepository
                    .findByMedicoIdMedicoAndFecha(idMedico, citaMedica.getFecha());

            for (CitaMedica existente : citasExistentes) {
                if (seCruzan(
                        citaMedica.getHoraInicio(),
                        citaMedica.getHoraFin(),
                        existente.getHoraInicio(),
                        existente.getHoraFin())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "La cita del médico '" + citaMedica.getMedico().getNombres() +
                                    "' se cruza con una ya existente en la fecha " + citaMedica.getFecha());
                }
            }

            //REVISAMOS SI LAS DURACIONES DE LAS CITAS SON DE 1 HORA EXACTAMENTE
            Duration duracion = Duration.between(citaMedica.getHoraInicio(), citaMedica.getHoraFin());
            if (!duracion.equals(Duration.ofHours(1))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Las citas médicas deben durar exactamente 1 hora. Verifique la fecha "
                                + citaMedica.getFecha() + " entre " + citaMedica.getHoraInicio() + " y " + citaMedica.getHoraFin());
            }


            //Agregamos la cita
            listaCitasMedicas.add(citaMedica);
        });
        //El safeAll
        citaMedicaRepository.saveAll(listaCitasMedicas);
        return true;
    }

    //PARA VERIFICAR CRUCES DE HORAS EXISTENTES CON LOS DE CARGA MASIVA
    private boolean seCruzan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }


}
