package pe.edu.pucp.salud360.servicio.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.servicio.repositories.ReservaRepository;
import pe.edu.pucp.salud360.servicio.services.ReservaService;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.repositories.CitaMedicaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaServiceImp implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    private final ClienteRepository clienteRepository;
    private final ClaseRepository claseRepository;
    private final CitaMedicaRepository citaMedicaRepository;

    @Override
    public ReservaDTO crearReserva(ReservaDTO dto) {
        Reserva reserva = reservaMapper.mapToModel(dto);

        // Asignar relaciones
        Cliente cliente = clienteRepository.findById(dto.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        reserva.setCliente(cliente);

        if (dto.getClase() != null) {
            Clase clase = claseRepository.findById(dto.getClase().getIdClase())
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
            reserva.setClase(clase);
        }

        if (dto.getCitaMedica() != null) {
            CitaMedica cita = citaMedicaRepository.findById(dto.getCitaMedica().getIdCitaMedica())
                    .orElseThrow(() -> new RuntimeException("Cita médica no encontrada"));
            if(cita.getEstado().equals("Reservada")) {
                throw new IllegalStateException("La cita médica ya ha sido reservada.");
            }
            reserva.setCitaMedica(cita);
        }

        reserva.setFechaReserva(LocalDateTime.now());

        return reservaMapper.mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    public ReservaDTO actualizarReserva(Integer id, ReservaDTO dto) {
        Optional<Reserva> optional = reservaRepository.findById(id);
        if (optional.isEmpty()) return null;

        Reserva reserva = optional.get();

        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setEstado(dto.getEstado());

        return reservaMapper.mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    public void eliminarReserva(Integer id) {
        Optional<Reserva> optional = reservaRepository.findById(id);
        if (optional.isPresent()) {
            Reserva reserva = optional.get();
            reserva.setFechaCancelacion(reserva.getFechaCancelacion() != null
                    ? reserva.getFechaCancelacion()
                    : java.time.LocalDateTime.now());
            reservaRepository.save(reserva);
        }
    }

    @Override
    public List<ReservaDTO> listarReservasTodas() {
        return reservaRepository.findAll().stream()
                .map(reservaMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservaDTO buscarReservaPorId(Integer id) {
        return reservaRepository.findById(id)
                .map(reservaMapper::mapToDTO)
                .orElse(null);
    }
}
