package pe.edu.pucp.salud360.membresia.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.membresia.dtos.PeriodoDTO;
import pe.edu.pucp.salud360.membresia.mappers.AfiliacionMapper;
import pe.edu.pucp.salud360.membresia.models.Periodo;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
import pe.edu.pucp.salud360.membresia.repositories.PeriodoRepository;
import pe.edu.pucp.salud360.membresia.services.PeriodoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeriodoImp implements PeriodoService {

    @Autowired
    private final PeriodoRepository periodoRepository;

    @Autowired
    private final AfiliacionRepository afiliacionRepository;

    @Autowired
    private final AfiliacionMapper afiliacionMapper;

    @Override
    public List<PeriodoDTO> listarPeriodos() {
        return periodoRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    @Override
    public PeriodoDTO obtenerPeriodoPorId(Integer id) {
        return periodoRepository.findById(id).map(this::convertirADTO).orElse(null);
    }

    @Override
    public PeriodoDTO crearPeriodo(PeriodoDTO dto) {
        Periodo periodo = convertirAEntidad(dto);
        return convertirADTO(periodoRepository.save(periodo));
    }

    @Override
    public PeriodoDTO actualizarPeriodo(Integer id, PeriodoDTO dto) {
        Optional<Periodo> periodoOptional = periodoRepository.findById(id);
        if (periodoOptional.isEmpty()) return null;

        Periodo periodo = periodoOptional.get();
        periodo.setFechaFin(dto.getFechaFin());
        periodo.setCantReservas(dto.getCantReservas());
        periodo.setAfiliacion(afiliacionRepository.findById(dto.getAfiliacion().getIdAfiliacion()).orElse(null));

        return convertirADTO(periodoRepository.save(periodo));
    }

    @Override
    public void eliminarPeriodo(Integer id) {
        periodoRepository.deleteById(id);
    }

    private PeriodoDTO convertirADTO(Periodo periodo) {
        return new PeriodoDTO(
                periodo.getIdPeriodo(),
                periodo.getFechaInicio(),
                periodo.getFechaFin(),
                periodo.getCantReservas(),
                periodo.getAfiliacion() != null ? afiliacionMapper.mapToAfiliacionDTO(periodo.getAfiliacion()) : null
        );
    }

    private Periodo convertirAEntidad(PeriodoDTO dto) {
        Periodo periodo = new Periodo();
        periodo.setFechaInicio(dto.getFechaInicio());
        periodo.setFechaFin(dto.getFechaFin());
        periodo.setCantReservas(dto.getCantReservas());
        periodo.setAfiliacion(afiliacionRepository.findById(dto.getAfiliacion().getIdAfiliacion()).orElse(null));
        return periodo;
    }
}
