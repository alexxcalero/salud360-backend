package pe.edu.pucp.salud360.servicio.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.servicio.repositories.ReservaRepository;
import pe.edu.pucp.salud360.servicio.services.ReservaService;
import pe.edu.pucp.salud360.usuario.models.Persona;
import pe.edu.pucp.salud360.usuario.repositories.PersonaRepository;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.repositories.CitaMedicaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImp implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaMapper reservaMapper;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private CitaMedicaRepository citaMedicaRepository;

    @Override
    public ReservaDTO crearReserva(ReservaDTO dto) {
        Reserva reserva = reservaMapper.mapToModel(dto);

        // Asignar relaciones
        Persona usuario = personaRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        reserva.setPersona(usuario);

        if (dto.getIdClase() != null) {
            Clase clase = claseRepository.findById(dto.getIdClase())
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
            reserva.setClase(clase);
        }

        if (dto.getIdCitaMedica() != null) {
            CitaMedica cita = citaMedicaRepository.findById(dto.getIdCitaMedica())
                    .orElseThrow(() -> new RuntimeException("Cita médica no encontrada"));
            reserva.setCitaMedica(cita);
        }

        reserva.setActivo(true);
        return reservaMapper.mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    public ReservaDTO actualizarReserva(Integer id, ReservaDTO dto) {
        Optional<Reserva> optional = reservaRepository.findById(id);
        if (optional.isEmpty()) return null;

        Reserva reserva = optional.get();

        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setHoraInicio(dto.getHoraInicio());
        reserva.setHoraFin(dto.getHoraFin());
        reserva.setHoraNotificacion(dto.getHoraNotificacion());
        reserva.setEstado(dto.getEstado());
        reserva.setFechaReprogramacion(dto.getFechaReprogramacion());

        return reservaMapper.mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    public void eliminarReserva(Integer id) {
        Optional<Reserva> optional = reservaRepository.findById(id);
        if (optional.isPresent()) {
            Reserva reserva = optional.get();
            reserva.setActivo(false);
            reserva.setFechaCancelacion(reserva.getFechaCancelacion() != null
                    ? reserva.getFechaCancelacion()
                    : java.time.LocalDateTime.now());
            reservaRepository.save(reserva);
        }
    }

    @Override
    public List<ReservaDTO> listarReservasTodas() {
        return reservaRepository.findAll().stream()
                .filter(Reserva::getActivo)
                .map(reservaMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservaDTO buscarReservaPorId(Integer id) {
        return reservaRepository.findById(id)
                .filter(Reserva::getActivo)
                .map(reservaMapper::mapToDTO)
                .orElse(null);
    }
}

