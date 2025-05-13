package pe.edu.pucp.salud360.membresia.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoDTO;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.MedioDePagoMapper;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;
import pe.edu.pucp.salud360.membresia.repositories.MedioDePagoRepository;
import pe.edu.pucp.salud360.membresia.services.MedioDePagoService;
import pe.edu.pucp.salud360.usuario.models.Persona;
import pe.edu.pucp.salud360.usuario.repositories.PersonaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedioDePagoServiceImp implements MedioDePagoService {

    @Autowired
    private MedioDePagoRepository medioDePagoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private MedioDePagoMapper medioDePagoMapper;

    @Override
    public List<MedioDePagoResumenDTO> listar() {
        return medioDePagoRepository.findAll().stream()
                .map(medioDePagoMapper::mapToMedioDePagoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedioDePagoDTO crear(MedioDePagoDTO dto) {
        Persona persona = personaRepository.findById(dto.getUsuario().getIdUsuario()).orElse(null);
        MedioDePago m = medioDePagoMapper.mapToModel(dto);
        return medioDePagoMapper.mapToDTO(medioDePagoRepository.save(m));
    }

    @Override
    public MedioDePagoResumenDTO obtenerPorId(Integer id) {
        return medioDePagoRepository.findById(id)
                .map(medioDePagoMapper::mapToMedioDePagoDTO)
                .orElse(null);
    }

    @Override
    public boolean eliminar(Integer id) {
        if (medioDePagoRepository.existsById(id)) {
            medioDePagoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public MedioDePagoDTO actualizar(Integer id, MedioDePagoDTO dto) {
        if (!medioDePagoRepository.existsById(id)) return null;
        Persona persona = personaRepository.findById(dto.getUsuario().getIdUsuario()).orElse(null);
        dto.setIdMedioDePago(id);
        MedioDePago m = medioDePagoMapper.mapToModel(dto);
        return medioDePagoMapper.mapToDTO(medioDePagoRepository.save(m));
    }
}

