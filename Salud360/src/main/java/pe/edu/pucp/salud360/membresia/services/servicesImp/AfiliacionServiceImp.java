package pe.edu.pucp.salud360.membresia.services.servicesImp;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.membresia.dtos.PeriodoDTO;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.AfiliacionMapper;
import pe.edu.pucp.salud360.membresia.mappers.MedioDePagoMapper;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.models.Periodo;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
import pe.edu.pucp.salud360.membresia.repositories.MembresiaRepository;
import pe.edu.pucp.salud360.membresia.repositories.PeriodoRepository;
import pe.edu.pucp.salud360.membresia.services.AfiliacionService;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.servicio.models.*;
import pe.edu.pucp.salud360.servicio.repositories.CitaMedicaRepository;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
import pe.edu.pucp.salud360.servicio.repositories.ReservaRepository;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.membresia.repositories.MedioDePagoRepository;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AfiliacionServiceImp implements AfiliacionService {

    private final AfiliacionRepository afiliacionRepository;
    private final AfiliacionMapper afiliacionMapper;

    private final PeriodoRepository periodoRepository;

    private final MembresiaRepository membresiaRepository;

    private final ComunidadRepository comunidadRepository;

    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;

    private final MedioDePagoRepository medioDePagoRepository;

    private final ReservaMapper reservaMapper;
    private final ReservaRepository reservaRepository;

    private final ClaseRepository claseRepository;
    private final CitaMedicaRepository citaMedicaRepository;

    @Transactional
    @Override
    public AfiliacionResumenDTO crearAfiliacion(AfiliacionDTO dto) {
        // Toy dudando, pero creo que si va a funcar
        Cliente cliente = clienteRepository.findById(dto.getUsuario().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comunidad com = comunidadRepository.findById(dto.getComunidad().getIdComunidad())
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        List<Afiliacion> afiliacionesExistentes = cliente.getAfiliaciones();
        for(Afiliacion afi : afiliacionesExistentes) {
            if(afi.getMembresia().getComunidad().getIdComunidad().equals(com.getIdComunidad())) {
                if(afi.getEstado().equals("Activado") || afi.getEstado().equals("Suspendido")) {
                    throw new IllegalStateException("Ya posees una membresia para esta comunidad.");
                }
            }
        }
        // Con fe

        Afiliacion afiliacion = new Afiliacion();
        afiliacion.setEstado(dto.getEstado());
        afiliacion.setFechaAfiliacion(dto.getFechaAfiliacion());
        afiliacion.setFechaDesafiliacion(dto.getFechaDesafiliacion());
        afiliacion.setFechaReactivacion(dto.getFechaReactivacion());
        afiliacion.setCliente(clienteRepository.findById(dto.getUsuario().getIdCliente()).orElse(null));
        afiliacion.setMedioDePago(medioDePagoRepository.findById(dto.getMedioDePago().getIdMedioDePago()).orElse(null));
        Optional<Membresia> m = membresiaRepository.findById(dto.getMembresia().getIdMembresia());
        Comunidad c = new Comunidad();
        if (m.isPresent()) {
            afiliacion.setMembresia(membresiaRepository.findById(dto.getMembresia().getIdMembresia()).get());
            c = afiliacion.getMembresia().getComunidad();
        }
        List<Afiliacion> afs = afiliacionRepository.findAll();
        for(Afiliacion af : afs){
            if(Objects.equals(af.getMembresia().getComunidad().getIdComunidad(), c.getIdComunidad()) &&
                    Objects.equals(af.getCliente().getIdCliente(), afiliacion.getCliente().getIdCliente()) &&
                    Objects.equals(af.getEstado(), "Activado")){
                return afiliacionMapper.mapToAfiliacionDTO(af);
            }
        }
        // Para mantener la relación comunidad - cliente
        c.getClientes().add(afiliacion.getCliente());
        c.setCantMiembros(c.getCantMiembros()+1);
        if (m.isPresent()){
            Membresia neom = m.get();
            neom.setCantUsuarios(neom.getCantUsuarios()+1);
            membresiaRepository.save(neom);
        }
        comunidadRepository.save(c);
        List<Comunidad> cs = cliente.getComunidades();
        cs.add(c);
        cliente.setComunidades(cs);
        clienteRepository.save(cliente);

        // LA FE ES LO ULTIMO QUE SE PIERDE Y YA NO TENGO NADA MAS
        Periodo nuevoPeriodo = new Periodo();
        nuevoPeriodo.setFechaInicio(LocalDate.now());
        nuevoPeriodo.setFechaFin(LocalDate.now().plusMonths(1));
        nuevoPeriodo.setCantReservas(0);
        nuevoPeriodo.setHaSidoSuspendida(false);
        nuevoPeriodo.setAfiliacion(afiliacion);

        Periodo periodo = periodoRepository.save(nuevoPeriodo);
        afiliacion.setPeriodo(new ArrayList<Periodo>());
        afiliacion.getPeriodo().add(periodo);
        return afiliacionMapper.mapToAfiliacionDTO(afiliacionRepository.save(afiliacion));
    }

    @Override
    public List<AfiliacionResumenDTO> listarAfiliaciones() {
        List<Afiliacion> af = afiliacionRepository.findAll();
        for(Afiliacion afs: af){
            if(!afs.getEstado().equals("Suspendido")) continue;
            LocalDate fec = afs.getFechaReactivacion();
            if(fec != null){
                if(fec.isBefore(LocalDate.now()) && afs.getEstado().equals("Suspendido")){
                    afs.setEstado("Activado");
                    afiliacionRepository.save(afs);
                }
            }
        }
        return afiliacionRepository.findAll().stream()
                .map(afiliacionMapper::mapToAfiliacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean eliminarAfiliacion(Integer idAfiliacion) {
        Afiliacion af = afiliacionRepository.findById(idAfiliacion)
                .orElseThrow(() -> new EntityNotFoundException("Afiliación no encontrada"));

        List<Periodo> periodos = af.getPeriodo();
        if(periodos == null || periodos.isEmpty()) {
            throw new IllegalStateException("La afiliación no tiene periodos asociados.");
        }

        Periodo periodoActual = periodos.getLast();

        // Verificar la cantidad de dias que va a suspender la membresia
        LocalDate fechaFinPeriodo = periodoActual.getFechaFin();
        LocalDate fechaActual = LocalDate.now();
        if(fechaFinPeriodo.isBefore(fechaActual)) {
            throw new IllegalStateException("La membresía ya ha vencido.");
        }

        // Validar si ya esta cancelada
        if (af.getEstado().equals("Cancelado")) {
            throw new IllegalStateException("La afiliación ya se encuentra cancelada.");
        }

        // Si pasa todo esto, se cancela la afiliacion
        af.setEstado("Cancelado");
        af.setFechaDesafiliacion(LocalDateTime.now());

        Membresia membresia = af.getMembresia();
        Comunidad comunidad = membresia.getComunidad();

        membresia.setCantUsuarios(membresia.getCantUsuarios() - 1);
        comunidad.setCantMiembros(comunidad.getCantMiembros() - 1);

        // Ahora voy a manejar las reservas existentes para la afiliacion
        Cliente cliente = clienteRepository.findById(af.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Reserva> reservas = reservaRepository.findByClienteAndComunidad(cliente.getIdCliente(), comunidad.getIdComunidad());
        for(Reserva r : reservas) {
            r.setEstado("Cancelada");
            r.setFechaCancelacion(LocalDateTime.now());
            if(r.getCitaMedica() != null) {
                r.getCitaMedica().setEstado("Disponible");
                r.getCitaMedica().setCliente(null);
                cliente.getCitasMedicas().remove(r.getCitaMedica());
                citaMedicaRepository.save(r.getCitaMedica());
            } else {
                r.getClase().setCantAsistentes(r.getClase().getCantAsistentes() - 1);

                if(r.getClase().getEstado().equals("Completa"))
                    r.getClase().setEstado("Disponible");

                r.getClase().getClientes().remove(cliente);
                cliente.getClases().remove(r.getClase());
                claseRepository.save(r.getClase());
            }
            reservaRepository.save(r);
        }

        comunidad.getClientes().remove(cliente);
        cliente.getComunidades().remove(comunidad);
        cliente.getAfiliaciones().remove(af);

        comunidadRepository.save(comunidad);
        membresiaRepository.save(membresia);
        afiliacionRepository.save(af);

        return true;
    }

    @Override
    public boolean desafiliar(Integer id, Integer ndias){
        if(afiliacionRepository.existsById(id)){
            Optional<Afiliacion> afiliacion = afiliacionRepository.findById(id);
            if (afiliacion.isPresent()) {
                Afiliacion af = afiliacion.get();

                List<Periodo> periodos = af.getPeriodo();
                if(periodos == null || periodos.isEmpty()) {
                    throw new IllegalStateException("La afiliación no tiene periodos asociados.");
                }

                Periodo periodoActual = periodos.getLast();
                // Para que no suspendan a cada rato la membresia
                if(periodoActual.getHaSidoSuspendida()) {
                    throw new IllegalStateException("La membresía solo puede ser suspendida una vez en un periodo.");
                }

                // Verificar la cantidad de dias que va a suspender la membresia
                LocalDate fechaFinPeriodo = periodoActual.getFechaFin();
                LocalDate fechaActual = LocalDate.now();
                if(fechaFinPeriodo.isBefore(fechaActual)) {
                    throw new IllegalStateException("La membresía ya ha vencido.");
                }

                long diasRestantes = ChronoUnit.DAYS.between(fechaActual, fechaFinPeriodo);
                if (ndias > diasRestantes) {
                    throw new IllegalStateException("No se puede suspender por más días de los que quedan en la membresía actual (" + diasRestantes + " días).");
                }

                // Validar si ya está suspendido
                if (af.getEstado().equals("Suspendido")) {
                    throw new IllegalStateException("La afiliación ya se encuentra suspendida.");
                }

                // Si pasa todo esto, se suspende la membresia
                af.setEstado("Suspendido");
                af.setFechaReactivacion(LocalDate.now().plusDays(ndias));
                periodoActual.setFechaFin(periodoActual.getFechaFin().plusDays(ndias));  // Agrego dias al periodo actual
                periodoActual.setHaSidoSuspendida(true);  // Cambio el estado de la variable que indica si la afiliacion ha sido suspendida
                periodoRepository.save(periodoActual);
                afiliacionRepository.save(af);

                // Ahora voy a manejar las reservas existentes para la afiliacion
                Cliente cliente = clienteRepository.findById(af.getCliente().getIdCliente())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                Membresia membresia = af.getMembresia();
                Comunidad comunidad = membresia.getComunidad();

                List<Reserva> reservas = reservaRepository.findByClienteAndComunidad(cliente.getIdCliente(), comunidad.getIdComunidad());
                for(Reserva r : reservas) {
                    r.setEstado("Cancelada");
                    r.setFechaCancelacion(LocalDateTime.now());
                    if(r.getCitaMedica() != null) {
                        r.getCitaMedica().setEstado("Disponible");
                        r.getCitaMedica().setCliente(null);
                        cliente.getCitasMedicas().remove(r.getCitaMedica());
                        citaMedicaRepository.save(r.getCitaMedica());
                    } else {
                        r.getClase().setCantAsistentes(r.getClase().getCantAsistentes() - 1);

                        if(r.getClase().getEstado().equals("Completa"))
                            r.getClase().setEstado("Disponible");

                        r.getClase().getClientes().remove(cliente);
                        cliente.getClases().remove(r.getClase());
                        claseRepository.save(r.getClase());
                    }
                    reservaRepository.save(r);
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public AfiliacionResumenDTO buscarAfiliacionPorId(Integer id) {
        return afiliacionRepository.findById(id)
                .map(afiliacionMapper::mapToAfiliacionDTO)
                .orElse(null);
    }

    @Override
    public List<AfiliacionResumenDTO> listarAfiliacionesPorCliente(Integer idCliente) {
        List<Afiliacion> afiliaciones = afiliacionRepository.findAfiliacionesActivasConComunidadByCliente(idCliente);
        return afiliaciones.stream()
                .map(afiliacionMapper::mapToAfiliacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AfiliacionResumenDTO actualizarAfiliacion(Integer id, AfiliacionDTO dto) {
        if (!afiliacionRepository.existsById(id)) return null;
        Optional<Afiliacion> oafiliacion = afiliacionRepository.findById(id);
        Afiliacion afiliacion = oafiliacion.get();
        afiliacion.setIdAfiliacion(id);
        afiliacion.setEstado(dto.getEstado());
        afiliacion.setFechaAfiliacion(dto.getFechaAfiliacion());
        afiliacion.setFechaDesafiliacion(dto.getFechaDesafiliacion());
        afiliacion.setFechaReactivacion(dto.getFechaReactivacion());
        if(dto.getUsuario() != null)
            afiliacion.setCliente(clienteRepository.findById(dto.getUsuario().getIdCliente()).orElse(null));
        if(dto.getMedioDePago() != null)
            afiliacion.setMedioDePago(medioDePagoRepository.findById(dto.getMedioDePago().getIdMedioDePago()).orElse(null));

        return afiliacionMapper.mapToAfiliacionDTO(afiliacionRepository.save(afiliacion));
    }

    @Override
    public boolean reactivarAfiliacion(Integer idAfiliacion) {
        Afiliacion af = afiliacionRepository.findById(idAfiliacion)
                .orElseThrow(() -> new EntityNotFoundException("Afiliación no encontrada"));

        af.setEstado("Activado");
        af.setFechaReactivacion(LocalDate.now()); // o LocalDateTime.now() si prefieres
        afiliacionRepository.save(af);
        return true;
    }

    @Override
    @Transactional
    public Boolean cargarMasivamanteAfiliacion(MultipartFile file) throws IOException {
        List<Afiliacion> listaAfiliaciones = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setDelimiter(',');
        settings.setIgnoreLeadingWhitespaces(true);
        settings.setIgnoreTrailingWhitespaces(true);
        CsvParser parser = new CsvParser(settings);
        List<Record> parseAllRecords = parser.parseAllRecords(inputStream);

        parseAllRecords.forEach(record -> {
            Afiliacion afiliacion = new Afiliacion();

            afiliacion.setEstado(record.getString("estado"));
            afiliacion.setFechaAfiliacion(LocalDateTime.parse(record.getString("fechaAfiliacion")));

            String fechaDesafiliacion = record.getString("fechaDesafiliacion");
            if (fechaDesafiliacion != null && !fechaDesafiliacion.isEmpty()) {
                afiliacion.setFechaDesafiliacion(LocalDateTime.parse(fechaDesafiliacion));
            }

            String fechaReactivacion = record.getString("fechaReactivacion");
            if (fechaReactivacion != null && !fechaReactivacion.isEmpty()) {
                afiliacion.setFechaReactivacion(LocalDate.parse(fechaReactivacion));
            }

            // Asociar Membresia
            Integer idMembresia = Integer.parseInt(record.getString("idMembresia"));
            Membresia membresia = membresiaRepository.findById(idMembresia)
                    .orElseThrow(() -> new RuntimeException("Membresía con ID " + idMembresia + " no encontrada"));
            afiliacion.setMembresia(membresia);

            // Asociar MedioDePago
            Integer idMedioDePago = Integer.parseInt(record.getString("idMedioDePago"));
            MedioDePago medio = medioDePagoRepository.findById(idMedioDePago)
                    .orElseThrow(() -> new RuntimeException("MedioDePago con ID " + idMedioDePago + " no encontrado"));
            afiliacion.setMedioDePago(medio);

            // Asociar Cliente
            Integer idCliente = Integer.parseInt(record.getString("idCliente"));
            Cliente cliente = clienteRepository.findById(idCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente con ID " + idCliente + " no encontrado"));
            afiliacion.setCliente(cliente);

            listaAfiliaciones.add(afiliacion);
        });

        afiliacionRepository.saveAll(listaAfiliaciones);
        return true;
    }

}

