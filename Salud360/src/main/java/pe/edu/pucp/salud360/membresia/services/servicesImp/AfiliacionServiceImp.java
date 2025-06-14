package pe.edu.pucp.salud360.membresia.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.AfiliacionMapper;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
import pe.edu.pucp.salud360.membresia.services.AfiliacionService;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.membresia.repositories.MedioDePagoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AfiliacionServiceImp implements AfiliacionService {

    @Autowired
    private AfiliacionRepository afiliacionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MedioDePagoRepository medioDePagoRepository;

    @Autowired
    private AfiliacionMapper afiliacionMapper;

    @Override
    public AfiliacionResumenDTO crearAfiliacion(AfiliacionDTO dto) {
        Afiliacion afiliacion = new Afiliacion();
        afiliacion.setEstado(dto.getEstado());
        afiliacion.setFechaAfiliacion(dto.getFechaAfiliacion());
        afiliacion.setFechaDesafiliacion(dto.getFechaDesafiliacion());
        afiliacion.setFechaReactivacion(dto.getFechaReactivacion());

        afiliacion.setCliente(clienteRepository.findById(dto.getUsuario().getIdUsuario()).orElse(null));
        afiliacion.setMedioDePago(medioDePagoRepository.findById(dto.getMedioDePago().getIdMedioDePago()).orElse(null));

        return afiliacionMapper.mapToAfiliacionDTO(afiliacionRepository.save(afiliacion));
    }

    @Override
    public List<AfiliacionResumenDTO> listarAfiliaciones() {
        return afiliacionRepository.findAll().stream()
                .map(afiliacionMapper::mapToAfiliacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean eliminarAfiliacion(Integer id) {
        if (afiliacionRepository.existsById(id)) {
            afiliacionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean desafiliar(Integer id){
        if(afiliacionRepository.existsById(id)){
            Optional<Afiliacion> afiliacion = afiliacionRepository.findById(id);
            Afiliacion af = afiliacion.get();
            af.setEstado("DESACTIVADO");
            afiliacionRepository.save(af);
            return true;
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
            afiliacion.setCliente(clienteRepository.findById(dto.getUsuario().getIdUsuario()).orElse(null));
        if(dto.getMedioDePago() != null)
            afiliacion.setMedioDePago(medioDePagoRepository.findById(dto.getMedioDePago().getIdMedioDePago()).orElse(null));

        return afiliacionMapper.mapToAfiliacionDTO(afiliacionRepository.save(afiliacion));
    }
}

