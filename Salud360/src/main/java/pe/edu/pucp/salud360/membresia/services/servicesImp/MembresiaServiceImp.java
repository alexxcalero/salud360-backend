package pe.edu.pucp.salud360.membresia.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.membresia.repositories.MembresiaRepository;
import pe.edu.pucp.salud360.membresia.services.MembresiaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembresiaServiceImp implements MembresiaService {

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Autowired
    private MembresiaMapper membresiaMapper;

    @Override
    public MembresiaDTO crearMembresia(MembresiaDTO dto) {
        Comunidad comunidad = comunidadRepository.findById(dto.getComunidad().getIdComunidad()).orElse(null);
        Membresia m = membresiaMapper.mapToModel(dto);
        m.setComunidad(comunidad);
        return membresiaMapper.mapToDTO(membresiaRepository.save(m));
    }

    @Override
    public List<MembresiaDTO> listarMembresias() {
        return membresiaRepository.findAll().stream().map(membresiaMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public MembresiaResumenDTO buscarMembresiaPorId(Integer id) {
        return membresiaRepository.findById(id).map(membresiaMapper::mapToMembresiaDTO).orElse(null);
    }

    @Override
    public MembresiaResumenDTO actualizarMembresia(Integer id, MembresiaDTO dto) {
        if (!membresiaRepository.existsById(id)) return null;
        dto.setIdMembresia(id);
        Comunidad comunidad = comunidadRepository.findById(dto.getComunidad().getIdComunidad()).orElse(null);
        Membresia m = membresiaMapper.mapToModel(dto);
        return membresiaMapper.mapToMembresiaDTO(membresiaRepository.save(m));
    }

    @Override
    public boolean eliminarMembresia(Integer id) {
        if (!membresiaRepository.existsById(id)) return false;
        membresiaRepository.deleteById(id);
        return true;
    }
}
