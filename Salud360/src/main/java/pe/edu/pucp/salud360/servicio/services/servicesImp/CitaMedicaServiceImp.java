package pe.edu.pucp.salud360.servicio.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.CitaMedicaRepository;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.usuario.models.Medico;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.usuario.repositories.MedicoRepository;
import pe.edu.pucp.salud360.servicio.services.CitaMedicaService;

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
            if(fechaCita == fechaCitaCreada) {
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
        if(horaInicioNueva.equals(horaInicio))
            return true;
        else if(horaInicioNueva.isBefore(horaInicio))
            if(horaFinNueva.isAfter(horaInicio))
                return true;
        else
            if(horaInicioNueva.isBefore(horaFin))
                return true;

        return false;
    }

    @Override
    public void eliminarCitaMedica(Integer id) {
        Optional<CitaMedica> optional = citaMedicaRepository.findById(id);
        if (optional.isPresent()) {
            CitaMedica cita = optional.get();

            // No se va a permitir la actualizacion si la cita ya ha sido reservada
            if(cita.getEstado().equals("Reservada"))
                throw new IllegalStateException("No se puede actualizar esta cita, debido a que ya ha sido reservada por un cliente.");

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

    @Override
    public List<CitaMedicaDTO> listarCitasMedicasTodas() {
        return citaMedicaRepository.findAll().stream()
                .map(citaMedicaMapper::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public CitaMedicaDTO buscarCitaMedicaPorId(Integer id) {
        return citaMedicaRepository.findById(id)
                .map(citaMedicaMapper::mapToDTO)
                .orElse(null);
    }
}
