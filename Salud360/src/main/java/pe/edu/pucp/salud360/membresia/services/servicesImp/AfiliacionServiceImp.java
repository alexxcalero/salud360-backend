package pe.edu.pucp.salud360.membresia.services.servicesImp;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.AfiliacionMapper;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
import pe.edu.pucp.salud360.membresia.repositories.MembresiaRepository;
import pe.edu.pucp.salud360.membresia.services.AfiliacionService;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.membresia.repositories.MedioDePagoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Override
    public AfiliacionResumenDTO crearAfiliacion(AfiliacionDTO dto) {
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
        c.getClientes().add(afiliacion.getCliente());
        comunidadRepository.save(c);
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
        Afiliacion afiliacion = afiliacionRepository.findById(idAfiliacion)
                .orElseThrow(() -> new EntityNotFoundException("Afiliación no encontrada"));

        afiliacion.setEstado("Cancelado");
        afiliacion.setFechaDesafiliacion(LocalDateTime.now());
        afiliacionRepository.save(afiliacion);

        return true;
    }


    @Override
    public boolean desafiliar(Integer id, Integer ndias){
        if(afiliacionRepository.existsById(id)){
            Optional<Afiliacion> afiliacion = afiliacionRepository.findById(id);
            Afiliacion af = afiliacion.get();
            af.setEstado("Suspendido");
            af.setFechaReactivacion(LocalDate.now().plusDays(ndias));
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

}

