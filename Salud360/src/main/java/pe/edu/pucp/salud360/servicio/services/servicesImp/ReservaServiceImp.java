package pe.edu.pucp.salud360.servicio.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservaServiceImp implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    private final ClienteRepository clienteRepository;
    private final ClaseRepository claseRepository;
    private final CitaMedicaRepository citaMedicaRepository;
    private final MembresiaMapper membresiaMapper;
    private final ComunidadRepository comunidadRepository;
    private final AfiliacionRepository afiliacionRepository;

    @Override
    @Transactional
    public ReservaDTO crearReserva(ReservaDTO dto) {
        Reserva reserva = reservaMapper.mapToModel(dto);

        // Asignar relaciones
        Cliente cliente = clienteRepository.findById(dto.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalTime horaInicioNueva = null;
        LocalTime horaFinNueva = null;

        Clase clase = null;
        if(dto.getClase() != null) {
            clase = claseRepository.findById(dto.getClase().getIdClase())
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
            horaInicioNueva = clase.getHoraInicio();
            horaFinNueva = clase.getHoraFin();
        }

        CitaMedica cita = null;
        if(dto.getCitaMedica() != null) {
            cita = citaMedicaRepository.findById(dto.getCitaMedica().getIdCitaMedica())
                    .orElseThrow(() -> new RuntimeException("Cita médica no encontrada"));
            horaInicioNueva = cita.getHoraInicio();
            horaFinNueva = cita.getHoraFin();
        }

        List<Membresia> membresiasDelCliente = new ArrayList<>();
        for(Afiliacion afiliacion : cliente.getAfiliaciones()) {
            membresiasDelCliente.add(afiliacion.getMembresia());
        }

        Comunidad comunidad = comunidadRepository.findById(dto.getComunidad().getIdComunidad())
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        List<Membresia> membresiasDeLaComunidad = comunidad.getMembresias();

        Optional<Membresia> membresiaMatch = membresiasDeLaComunidad.stream()
                .filter(mC -> membresiasDelCliente.stream()
                .anyMatch(mCli -> Objects.equals(mCli.getIdMembresia(), mC.getIdMembresia())))
                .findFirst();

        if(membresiaMatch.isEmpty()) {
            throw new IllegalStateException("El cliente no posee una membresía asociada a la comunidad.");
        }

        Membresia membresiaEspecifica = membresiaMatch.get();

        Afiliacion afiliacionEspecifica = null;
        for(Afiliacion afiliacion : membresiaEspecifica.getAfiliacion()) {
            if(Objects.equals(afiliacion.getCliente().getIdCliente(), cliente.getIdCliente())) {
                afiliacionEspecifica = afiliacion;
            }
        }

        // Verificación obligatoria
        if (afiliacionEspecifica == null) {
            throw new IllegalStateException("El cliente no posee una afiliación específica de la membresía.");
        }

        if(membresiaEspecifica.getConTope()) {
            if(afiliacionEspecifica.getPeriodo().getLast().getCantReservas() + 1 > membresiaEspecifica.getMaxReservas()) {
                throw new IllegalStateException("El cliente no tiene reservas disponibles.");
            }
        }

        List<Reserva> reservasDelCliente = new ArrayList<>();
        for(Reserva r : cliente.getReservas()) {
            LocalTime horaInicio = null;
            LocalTime horaFin = null;
            if(r.getClase() != null) {
                horaInicio = r.getClase().getHoraInicio();
                horaFin = r.getClase().getHoraFin();
            }
            if(r.getCitaMedica() != null) {
                horaInicio = r.getCitaMedica().getHoraInicio();
                horaFin = r.getCitaMedica().getHoraFin();
            }

            if(horaInicioNueva.equals(horaInicio)) {
                throw new IllegalStateException("El cliente ya tiene una clase reservada para esa hora.");
            } else if(horaInicioNueva.isBefore(horaInicio)) {
                if(horaFinNueva.isAfter(horaInicio)) {
                    throw new IllegalStateException("El cliente ya tiene una clase reservada para esa hora.");
                }
            } else {
                if(horaInicioNueva.isBefore(horaFin)) {
                    throw new IllegalStateException("El cliente ya tiene una clase reservada para esa hora.");
                }
            }
        }

        if(clase != null) {
            if(clase.getCantAsistentes() + 1 > clase.getCapacidad()) {
                throw new IllegalStateException("La clase ya alcanzó el máximo de participantes.");
            }
            if(clase.getCantAsistentes() + 1 == clase.getCapacidad()) {
                clase.setEstado("Completa");
            }
            reserva.setClase(clase);
            cliente.getClases().add(clase);
            claseRepository.save(clase);
        }

        if(cita != null) {
            if(cita.getEstado().equals("Reservada")) {
                throw new IllegalStateException("La cita ya está reservada.");
            }
            cita.setEstado("Reservada");
            reserva.setCitaMedica(cita);
            cliente.getCitasMedicas().add(cita);
            citaMedicaRepository.save(cita);
        }

        reserva.setEstado("Reservada");
        reserva.setCliente(cliente);
        cliente.getReservas().add(reserva);
        reserva.setFechaReserva(LocalDateTime.now());
        afiliacionEspecifica.getPeriodo().getLast().setCantReservas(afiliacionEspecifica.getPeriodo().getLast().getCantReservas() + 1);
        afiliacionRepository.save(afiliacionEspecifica);

        return reservaMapper.mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public void cancelarReserva(Integer id) {
        Optional<Reserva> optional = reservaRepository.findById(id);
        if(optional.isPresent()) {
            Reserva reserva = optional.get();
            reserva.setEstado("Cancelada");
            reserva.setFechaCancelacion(LocalDateTime.now());

            Cliente cliente = reserva.getCliente();

            if(reserva.getClase() != null) {
                reserva.getClase().setCantAsistentes(reserva.getClase().getCantAsistentes() - 1);
                if(reserva.getClase().getCantAsistentes() == reserva.getClase().getCapacidad() - 1) {
                    reserva.getClase().setEstado("Disponible");
                }
                cliente.getClases().remove(reserva.getClase());
            }

            if(reserva.getCitaMedica() != null) {
                reserva.getCitaMedica().setEstado("Disponible");
                cliente.getCitasMedicas().remove(reserva.getCitaMedica());
            }

            reservaRepository.save(reserva);
        }
    }

    @Override
    public ReservaDTO buscarReservaPorId(Integer id) {
        return reservaRepository.findById(id)
                .map(reservaMapper::mapToDTO)
                .orElse(null);
    }
}
