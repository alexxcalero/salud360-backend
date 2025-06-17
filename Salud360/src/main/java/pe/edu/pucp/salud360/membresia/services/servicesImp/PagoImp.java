package pe.edu.pucp.salud360.membresia.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;
import pe.edu.pucp.salud360.membresia.mappers.MedioDePagoMapper;
import pe.edu.pucp.salud360.membresia.mappers.PagoMapper;
import pe.edu.pucp.salud360.membresia.models.Pago;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
import pe.edu.pucp.salud360.membresia.repositories.MedioDePagoRepository;
import pe.edu.pucp.salud360.membresia.repositories.PagoRepository;
import pe.edu.pucp.salud360.membresia.services.PagoService;

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
        pago.setAfiliacion(afiliacionRepository.findById(dto.getIdAfiliacion()).orElse(null));
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
                pago.getAfiliacion() != null ? pago.getAfiliacion().getIdAfiliacion() : null,
                pago.getMedioDePago() != null ? medioDePagoMapper.mapToMedioDePagoDTO(pago.getMedioDePago()) : null
        );
    }

    private Pago convertirAEntidad(PagoDTO dto) {
        Pago pago = new Pago();
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());
        pago.setAfiliacion(afiliacionRepository.findById(dto.getIdAfiliacion()).orElse(null));
        pago.setMedioDePago(medioDePagoRepository.findById(dto.getMedioDePago().getIdMedioDePago()).orElse(null));
        return pago;
    }
}
