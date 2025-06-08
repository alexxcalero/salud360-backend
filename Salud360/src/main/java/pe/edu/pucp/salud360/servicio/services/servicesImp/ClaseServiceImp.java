package pe.edu.pucp.salud360.servicio.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;
import pe.edu.pucp.salud360.servicio.mappers.LocalMapper;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
import pe.edu.pucp.salud360.servicio.repositories.LocalRepository;
import pe.edu.pucp.salud360.servicio.services.ClaseService;

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
    private final LocalMapper localMapper;
    private final LocalRepository localRepository;

    @Override
    public ClaseDTO crearClase(ClaseDTO dto) {
        Local local = localRepository.findById(dto.getLocal().getIdLocal())
                .orElseThrow(() -> new RuntimeException("Local no encontrado"));

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
            if(fechaClase == fechaClaseCreada) {
                if(existeCruceDeHorarios(horaInicio, horaFin, horaInicioClaseCreada, horaFinClaseCreada))
                    throw new IllegalStateException("El local ya tiene una clase asignada para esa hora.");
            }
        }

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
        clase.setCapacidad(dto.getCapacidad());
        //clase.setEstado(dto.getEstado());

        return claseMapper.mapToDTO(claseRepository.save(clase));
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
    public void eliminarClase(Integer idClase) {
        Optional<Clase> optional = claseRepository.findById(idClase);
        if (optional.isPresent()) {
            Clase clase = optional.get();

            for(Reserva r : clase.getReservas()) {
                if(r.getEstado().equals("Confirmada"))
                    throw new IllegalStateException("No se puede actualizar esta clase, debido a que ya ha sido reservada por un cliente.");
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
}
