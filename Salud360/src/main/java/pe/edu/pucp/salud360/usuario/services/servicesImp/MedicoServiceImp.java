package pe.edu.pucp.salud360.usuario.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.mappers.MedicoMapper;
import pe.edu.pucp.salud360.usuario.models.Medico;
import pe.edu.pucp.salud360.usuario.repositories.MedicoRepository;
import pe.edu.pucp.salud360.usuario.services.MedicoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoServiceImp implements MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MedicoMapper medicoMapper;

    @Override
    public MedicoVistaAdminDTO crearMedico(MedicoRegistroDTO medicoDTO) {
        Medico medico = medicoMapper.mapToModel(medicoDTO);
        medico.setActivo(true);
        medico.setFechaCreacion(LocalDateTime.now());
        medico.setFechaDesactivacion(null);
        Medico medicoCreado = medicoRepository.save(medico);
        return medicoMapper.mapToVistaAdminDTO(medicoCreado);
    }

    @Override
    public MedicoVistaAdminDTO actualizarMedico(Integer idMedico, MedicoVistaAdminDTO medicoDTO) {
        if(medicoRepository.findById(idMedico).isPresent()){
            Medico medico = medicoRepository.findById(idMedico).get();
            medico.setNombres(medicoDTO.getNombres());
            medico.setApellidos(medicoDTO.getApellidos());
            medico.setNumeroDocumento(medicoDTO.getNumeroDocumento());
            //medico.setTipoDocumento(TipoDocumentoMapper.mapToModel(medicoDTO.getTipoDocumento()));
            //medico.setRol(RolMapper.mapToModel(medicoDTO.getRol()));
            medico.setEspecialidad(medicoDTO.getEspecialidad());
            medico.setDescripcion(medicoDTO.getDescripcion());
            Medico medicoActualizado = medicoRepository.save(medico);
            return medicoMapper.mapToVistaAdminDTO(medicoActualizado);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarMedico(Integer idMedico) {
        Optional<Medico> medico = medicoRepository.findById(idMedico);
        if(medico.isPresent()) {
            Medico medicoEliminar = medico.get();
            medicoEliminar.setActivo(false);
            medicoEliminar.setFechaDesactivacion(LocalDateTime.now());
            medicoRepository.save(medicoEliminar);
        }
    }

    @Override
    public List<MedicoVistaAdminDTO> listarMedicosTodos() {
        List<Medico> medicos = medicoRepository.findAll();
        return medicos.stream().map(medico -> {
            MedicoVistaAdminDTO dto = medicoMapper.mapToVistaAdminDTO(medico);
            System.out.println("Especialidad: " + dto.getEspecialidad()); // 👈 revisa en consola
            return dto;
        }).toList();
    }

    @Override
    public MedicoVistaAdminDTO buscarMedicoPorId(Integer idMedico) {
        if(medicoRepository.findById(idMedico).isPresent()) {
            Medico medicoBuscado = medicoRepository.findById(idMedico).get();
            return medicoMapper.mapToVistaAdminDTO(medicoBuscado);
        } else {
            return null;
        }
    }
}
