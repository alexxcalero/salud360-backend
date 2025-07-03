package pe.edu.pucp.salud360.membresia.services.servicesImp;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;
import pe.edu.pucp.salud360.membresia.mappers.AfiliacionMapper;
import pe.edu.pucp.salud360.membresia.mappers.MedioDePagoMapper;
import pe.edu.pucp.salud360.membresia.mappers.PagoMapper;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;
import pe.edu.pucp.salud360.membresia.models.Pago;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
import pe.edu.pucp.salud360.membresia.repositories.MedioDePagoRepository;
import pe.edu.pucp.salud360.membresia.repositories.PagoRepository;
import pe.edu.pucp.salud360.membresia.services.PagoService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoImp implements PagoService {

    @Autowired
    private final PagoRepository pagoRepository;

    @Autowired
    private PagoMapper pagoMapper;

    @Autowired
    private final AfiliacionRepository afiliacionRepository;

    @Autowired
    private final AfiliacionMapper afiliacionMapper;

    @Autowired
    private final MedioDePagoRepository medioDePagoRepository;

    @Autowired
    private final MedioDePagoMapper medioDePagoMapper;



    @Override
    public List<PagoDTO> listarPagos() {
        return pagoRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    @Override
    public List<PagoDTO> obtenerPagosPorCliente(Integer idCliente) {
        return pagoRepository.findPagosByIdCliente(idCliente)
                .stream()
                .map(pagoMapper::mapToDTO)
                .toList();
    }


    @Override
    public PagoDTO obtenerPagoPorId(Integer id) {
        return pagoRepository.findById(id).map(this::convertirADTO).orElse(null);
    }

    @Override
    public PagoDTO crearPago(PagoDTO dto) {
        Pago pago = convertirAEntidad(dto);
        return convertirADTO(pagoRepository.save(pago));
    }

    @Override
    public PagoDTO actualizarPago(Integer id, PagoDTO dto) {
        Optional<Pago> pagoOptional = pagoRepository.findById(id);
        if (pagoOptional.isEmpty()) return null;

        Pago pago = pagoOptional.get();
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());
        pago.setAfiliacion(afiliacionRepository.findById(dto.getAfiliacion().getIdAfiliacion()).orElse(null));
        pago.setMedioDePago(medioDePagoRepository.findById(dto.getMedioDePago().getIdMedioDePago()).orElse(null));

        return convertirADTO(pagoRepository.save(pago));
    }

    @Override
    public void eliminarPago(Integer id) {
        pagoRepository.deleteById(id);
    }

    private PagoDTO convertirADTO(Pago pago) {
        return new PagoDTO(
                pago.getIdPago(),
                pago.getMonto(),
                pago.getFechaPago(),
                pago.getMedioDePago() != null ? medioDePagoMapper.mapToMedioDePagoDTO(pago.getMedioDePago()) : null,
                pago.getAfiliacion() != null ? afiliacionMapper.mapToAfiliacionDTO(pago.getAfiliacion()) : null
        );
    }

    private Pago convertirAEntidad(PagoDTO dto) {
        Pago pago = new Pago();
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());
        pago.setAfiliacion(afiliacionRepository.findById(dto.getAfiliacion().getIdAfiliacion()).orElse(null));
        pago.setMedioDePago(medioDePagoRepository.findById(dto.getMedioDePago().getIdMedioDePago()).orElse(null));
        return pago;
    }

    @Override
    @Transactional
    public Boolean cargarMasivamantePago(MultipartFile file) throws IOException {
        List<Pago> listaPagos = new ArrayList<>();
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
            Pago pago = new Pago();

            pago.setMonto(Double.parseDouble(record.getString("monto")));
            pago.setFechaPago(LocalDateTime.parse(record.getString("fechaPago")));

            // Asociar Afiliacion
            Integer idAfiliacion = Integer.parseInt(record.getString("idAfiliacion"));
            Afiliacion afiliacion = afiliacionRepository.findById(idAfiliacion)
                    .orElseThrow(() -> new RuntimeException("Afiliacion con ID " + idAfiliacion + " no encontrada"));
            pago.setAfiliacion(afiliacion);

            // Asociar MedioDePago
            Integer idMedioDePago = Integer.parseInt(record.getString("idMedioDePago"));
            MedioDePago medio = medioDePagoRepository.findById(idMedioDePago)
                    .orElseThrow(() -> new RuntimeException("MedioDePago con ID " + idMedioDePago + " no encontrado"));
            pago.setMedioDePago(medio);

            listaPagos.add(pago);
        });

        pagoRepository.saveAll(listaPagos);
        return true;
    }
}
