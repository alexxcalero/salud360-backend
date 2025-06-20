package pe.edu.pucp.salud360.servicio.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.models.Periodo;
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

import java.time.LocalDate;
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
        Reserva reserva;

        // Asignar relaciones
        Cliente cliente = clienteRepository.findById(dto.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDate fechaReserva;
        LocalTime horaInicioReserva;
        LocalTime horaFinReserva;
        Clase claseReservada;
        CitaMedica citaMedicaReservada;

        if(dto.getClase() != null) {
            claseReservada = claseRepository.findById(dto.getClase().getIdClase())
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
            fechaReserva = claseReservada.getFecha();
            horaInicioReserva = claseReservada.getHoraInicio();
            horaFinReserva = claseReservada.getHoraFin();

            citaMedicaReservada = null;
        } else {
            citaMedicaReservada = citaMedicaRepository.findById(dto.getCitaMedica().getIdCitaMedica())
                    .orElseThrow(() -> new RuntimeException("Cita médica no encontrada"));
            fechaReserva = citaMedicaReservada.getFecha();
            horaInicioReserva = citaMedicaReservada.getHoraInicio();
            horaFinReserva = citaMedicaReservada.getHoraFin();

            claseReservada = null;
        }

        Reserva reservaCancelada = cliente.getReservas().stream()
                .filter(r -> r.getEstado().equals("Cancelada"))
                .filter(r -> (claseReservada != null && r.getClase() != null &&
                        r.getClase().getIdClase().equals(claseReservada.getIdClase())) ||
                        (citaMedicaReservada != null && r.getCitaMedica() != null &&
                        r.getCitaMedica().getIdCitaMedica().equals(citaMedicaReservada.getIdCitaMedica())))
                .findFirst()
                .orElse(null);

        boolean puedeReutilizarReserva = false;

        if(claseReservada != null && reservaCancelada != null) {
            // Si hay una reserva cancelada para esta clase, verificar capacidad
            puedeReutilizarReserva = !(claseReservada.getEstado().equals("Completa"));
        } else if (citaMedicaReservada != null && reservaCancelada != null) {
            // Si hay una reserva cancelada para esta cita, verificar que nadie más la haya tomado
            boolean yaReservada = citaMedicaReservada.getReservas().stream()
                    .anyMatch(r -> r.getEstado().equals("Confirmada"));
            puedeReutilizarReserva = !yaReservada;
        }

        if(reservaCancelada != null && puedeReutilizarReserva)
            // Reutilizar reserva cancelada
            reserva = reservaCancelada;
        else
            // Crear nueva reserva
            reserva = reservaMapper.mapToModel(dto);

        List<Membresia> membresiasDelCliente = new ArrayList<>();
        for(Afiliacion afiliacion : cliente.getAfiliaciones()) {
            membresiasDelCliente.add(afiliacion.getMembresia());
        }

        Comunidad comunidad = comunidadRepository.findById(dto.getComunidad().getIdComunidad())
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        //        List<Membresia> membresiasDeLaComunidad = comunidad.getMembresias();
//
//        Optional<Membresia> membresia = membresiasDeLaComunidad.stream()
//                .filter(mC -> membresiasDelCliente.stream()
//                .anyMatch(mCli -> Objects.equals(mCli.getIdMembresia(), mC.getIdMembresia())))
//                .findFirst();
//
//        if(membresia.isEmpty()) {
//            throw new IllegalStateException("El cliente no posee una membresía asociada a la comunidad.");
//        }
//
//        Membresia membresiaEspecifica = membresia.get();
//
//        Afiliacion afiliacionEspecifica = null;
//        for(Afiliacion afiliacion : membresiaEspecifica.getAfiliacion()) {
//            if(Objects.equals(afiliacion.getCliente().getIdCliente(), cliente.getIdCliente())) {
//                afiliacionEspecifica = afiliacion;
//            }
//        }

        Afiliacion afiliacion = buscarAfiliacionDelCliente(cliente, comunidad.getIdComunidad());

        // Verifico si se encontro afiliacion del cliente a la comunidad
        if(afiliacion == null || afiliacion.getEstado().equals("Cancelado")) {
            throw new IllegalStateException("El cliente no posee una afiliación para esta comunidad.");
        }

        if(afiliacion.getEstado().equals("Suspendida")) {
            throw new IllegalStateException("El cliente tiene la membresía suspendida, por ende no puede hacer uso de los servicios de esta comunidad.");
        }

        // Si la membresía es con tope, se verifica si todavia puede reservar o no un servicio
        Membresia membresiaEspecifica = afiliacion.getMembresia();
        if(membresiaEspecifica.getConTope()) {
            Periodo periodoActual = afiliacion.getPeriodo().getLast();
            if(periodoActual.getCantReservas().equals(membresiaEspecifica.getMaxReservas())) {
                throw new IllegalStateException("El cliente no tiene reservas disponibles.");
            }
        }

        List<Reserva> reservasDelCliente = cliente.getReservas();
        for(Reserva r : reservasDelCliente) {
            // No considero reservas canceladas
            if(r.getEstado().equals("Cancelada")) continue;

            LocalDate fecha;
            LocalTime horaInicio;
            LocalTime horaFin;

            if(r.getClase() != null) {
                fecha = r.getClase().getFecha();
                horaInicio = r.getClase().getHoraInicio();
                horaFin = r.getClase().getHoraFin();

            } else {
                fecha = r.getCitaMedica().getFecha();
                horaInicio = r.getCitaMedica().getHoraInicio();
                horaFin = r.getCitaMedica().getHoraFin();
            }

            if(fecha.equals(fechaReserva))
                if(existeCruceDeHorarios(horaInicio, horaFin, horaInicioReserva, horaFinReserva))
                    throw new IllegalStateException("El cliente ya tiene una clase o cita reservada para esa hora.");
        }

        if(claseReservada != null) {
            if(claseReservada.getEstado().equals("Completa")) {
                throw new IllegalStateException("La clase ya alcanzó el máximo de participantes.");
            }
            if(claseReservada.getCantAsistentes() + 1 == claseReservada.getCapacidad()) {
                claseReservada.setEstado("Completa");
            }
            claseReservada.setCantAsistentes(claseReservada.getCantAsistentes() + 1);
            if(!puedeReutilizarReserva)  // Cuando no se reutilice la reserva cancelada previamente
                claseReservada.getReservas().add(reserva);  // Guardo la reserva que estoy generando en el arreglo de reservas de la clase
            claseReservada.getClientes().add(cliente);
            reserva.setClase(claseReservada);
            cliente.getClases().add(claseReservada);
            claseRepository.save(claseReservada);
        } else {
            if(citaMedicaReservada.getEstado().equals("Reservada")) {
                throw new IllegalStateException("La cita ya está reservada.");
            }
            citaMedicaReservada.setEstado("Reservada");
            if(!puedeReutilizarReserva)  // Cuando no se reutilice la reserva cancelada previamente
                citaMedicaReservada.getReservas().add(reserva);  // Guardo la reserva que estoy generando en el arreglo de reservas de la cita
            citaMedicaReservada.setCliente(cliente);
            reserva.setCitaMedica(citaMedicaReservada);
            cliente.getCitasMedicas().add(citaMedicaReservada);
            citaMedicaRepository.save(citaMedicaReservada);
        }

        reserva.setEstado("Confirmada");
        reserva.setDescripcion(dto.getDescripcion());
        reserva.setNombreArchivo(dto.getNombreArchivo());
        reserva.setCliente(cliente);
        reserva.setComunidad(comunidad);  // Porciaca lo seteo, pero creo que el mapper ya lo asigna
        cliente.getReservas().add(reserva);
        reserva.setFechaReserva(LocalDateTime.now());
        afiliacion.getPeriodo().getLast().setCantReservas(afiliacion.getPeriodo().getLast().getCantReservas() + 1);
        if(puedeReutilizarReserva)  // Si se reutiliza la reserva preciamente cancelada, se coloca en null la fecha de cancelacion
            reserva.setFechaCancelacion(null);
        afiliacionRepository.save(afiliacion);

        return reservaMapper.mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public void cancelarReserva(Integer id) {
        Optional<Reserva> optional = reservaRepository.findById(id);
        if(optional.isPresent()) {
            Reserva reserva = optional.get();

            LocalTime horaInicio;
            LocalDate fechaReserva;
            Clase clase;
            CitaMedica citaMedica;

            if(reserva.getClase() != null) {
                clase = claseRepository.findById(reserva.getClase().getIdClase())
                        .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
                fechaReserva = clase.getFecha();
                horaInicio = clase.getHoraInicio();
                citaMedica = null;
            } else {
                citaMedica = citaMedicaRepository.findById(reserva.getCitaMedica().getIdCitaMedica())
                        .orElseThrow(() -> new RuntimeException("Cita médica no encontrada"));
                fechaReserva = citaMedica.getFecha();
                horaInicio = citaMedica.getHoraInicio();
                clase = null;
            }

            // No se va a permitir la cancelacion de la reserva si es faltan menos de 30 minutos para que empiece la clase o cita
            LocalDate fechaActual = LocalDate.now();
            if(fechaActual.equals(fechaReserva)) {
                LocalTime horaActual = LocalTime.now();
                if(horaActual.isAfter(horaInicio.minusMinutes(30)))
                    throw new IllegalStateException("No se puede cancelar la reserva. Solo se permiten cancelaciones con al menos 30 minutos de anticipación.");
            }

            // Si llegamos a este punto, es xq faltan mas de 30 minutos para que empiece la clase o cita, por ende, se puede cancelar
            reserva.setEstado("Cancelada");
            reserva.setFechaCancelacion(LocalDateTime.now());

            Cliente cliente = clienteRepository.findById(reserva.getCliente().getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if(clase != null) {
                clase.setCantAsistentes(clase.getCantAsistentes() - 1);

                if(clase.getEstado().equals("Completa"))
                    clase.setEstado("Disponible");

                clase.getClientes().remove(cliente);
                cliente.getClases().remove(clase);
                claseRepository.save(clase);
            } else {
                citaMedica.setEstado("Disponible");
                citaMedica.setCliente(null);
                cliente.getCitasMedicas().remove(citaMedica);
                citaMedicaRepository.save(citaMedica);
            }

            // Actualizar contador si la membresía tiene tope
            Afiliacion afiliacion = buscarAfiliacionDelCliente(cliente, reserva.getComunidad().getIdComunidad());
            if(afiliacion != null && afiliacion.getMembresia().getConTope()) {
                Periodo periodoActual = afiliacion.getPeriodo().getLast();
                int nuevasReservas = periodoActual.getCantReservas() - 1;
                periodoActual.setCantReservas(Math.max(nuevasReservas, 0));  // evitar negativos
                afiliacionRepository.save(afiliacion);
            }

            reservaRepository.save(reserva);
        }
    }

    private boolean existeCruceDeHorarios(LocalTime horaInicio, LocalTime horaFin, LocalTime horaInicioNueva, LocalTime horaFinNueva) {
        return horaInicioNueva.isBefore(horaFin) && horaFinNueva.isAfter(horaInicio);
    }

    private Afiliacion buscarAfiliacionDelCliente(Cliente cliente, Integer idComunidad) {
        for(Afiliacion af : cliente.getAfiliaciones()) {
            Membresia membresia = af.getMembresia();
            if(membresia.getComunidad().getIdComunidad().equals(idComunidad)) {
                Periodo periodoActual = af.getPeriodo().getLast();
                LocalDate fechaActual = LocalDate.now();
                if(!fechaActual.isBefore(periodoActual.getFechaInicio()) && !fechaActual.isAfter(periodoActual.getFechaFin()))
                    return af;
            }
        }
        return null;
    }

    @Override
    public ReservaDTO buscarReservaPorId(Integer id) {
        return reservaRepository.findById(id)
                .map(reservaMapper::mapToDTO)
                .orElse(null);
    }
}
