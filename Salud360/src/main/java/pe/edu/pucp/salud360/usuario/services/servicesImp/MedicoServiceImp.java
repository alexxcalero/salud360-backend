package pe.edu.pucp.salud360.usuario.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.mappers.MedicoMapper;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.models.Medico;
import pe.edu.pucp.salud360.usuario.repositories.MedicoRepository;
import pe.edu.pucp.salud360.usuario.services.MedicoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoServiceImp implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final TipoDocumentoMapper tipoDocumentoMapper;

    @Override
    public MedicoVistaAdminDTO crearMedico(MedicoRegistroDTO medicoDTO) {
        Medico medico = medicoMapper.mapToModel(medicoDTO);
        medico.setActivo(true);
        medico.setFechaCreacion(LocalDateTime.now());
        medico.setCitasMedicas(new ArrayList<>());
        Medico medicoCreado = medicoRepository.save(medico);
        return medicoMapper.mapToVistaAdminDTO(medicoCreado);
    }

    @Override
    public MedicoVistaAdminDTO actualizarMedico(Integer idMedico, MedicoVistaAdminDTO medicoDTO) {
        Optional<Medico> medicoSeleccionado = medicoRepository.findById(idMedico);
        if(medicoSeleccionado.isPresent()){
            Medico medico = medicoSeleccionado.get();
            medico.setNombres(medicoDTO.getNombres());
            medico.setApellidos(medicoDTO.getApellidos());
            medico.setNumeroDocumento(medicoDTO.getNumeroDocumento());
            medico.setTipoDocumento(tipoDocumentoMapper.mapToModel(medicoDTO.getTipoDocumento()));
            medico.setSexo(medicoDTO.getSexo());
            medico.setEspecialidad(medicoDTO.getEspecialidad());
            medico.setDescripcion(medicoDTO.getDescripcion());
            medico.setFotoPerfil(medicoDTO.getFotoPerfil());
            Medico medicoActualizado = medicoRepository.save(medico);
            return medicoMapper.mapToVistaAdminDTO(medicoActualizado);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarMedico(Integer idMedico) {
        Optional<Medico> medicoSeleccionado = medicoRepository.findById(idMedico);
        if(medicoSeleccionado.isPresent()) {
            Medico medicoEliminar = medicoSeleccionado.get();
            medicoEliminar.setActivo(false);
            medicoEliminar.setFechaDesactivacion(LocalDateTime.now());
            medicoRepository.save(medicoEliminar);
        }
    }

    @Override
    public void reactivarMedico(Integer idMedico) {
        Optional<Medico> medicoSeleccionado = medicoRepository.findById(idMedico);
        if(medicoSeleccionado.isPresent()) {
            Medico medicoReactivar = medicoSeleccionado.get();
            medicoReactivar.setActivo(true);
            medicoReactivar.setFechaDesactivacion(null);
            medicoRepository.save(medicoReactivar);
        }
    }

    @Override
    public List<MedicoVistaAdminDTO> listarMedicos(String nombreCompleto, String especialidad) {
        List<Medico> medicos;

        boolean filtroNombre = nombreCompleto != null && !nombreCompleto.isBlank();
        boolean filtroEspecialidad = especialidad != null && !especialidad.isBlank();

        if(filtroNombre && filtroEspecialidad) {
            medicos = medicoRepository.buscarPorNombreYEspecialidad(nombreCompleto, especialidad);
        } else if(filtroNombre) {
            medicos = medicoRepository.buscarPorNombreCompleto(nombreCompleto);
        } else if(filtroEspecialidad) {
            medicos = medicoRepository.findAllByEspecialidadContainingIgnoreCaseOrderByIdMedicoAsc(especialidad);
        } else {
            medicos = medicoRepository.findAllByOrderByIdMedicoAsc();
        }

        if(!(medicos.isEmpty())) {
            return medicos.stream().map(medicoMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public MedicoVistaAdminDTO buscarMedicoPorId(Integer idMedico) {
        Optional<Medico> medicoBuscado = medicoRepository.findById(idMedico);
        if(medicoBuscado.isPresent()) {
            Medico medico = medicoBuscado.get();
            return medicoMapper.mapToVistaAdminDTO(medico);
        } else {
            return null;
        }
    }
}
