package pe.edu.pucp.salud360.usuario.services;

import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;

import java.util.List;

public interface MedicoService {
    MedicoVistaAdminDTO crearMedico(MedicoRegistroDTO medicoDTO);
    MedicoVistaAdminDTO actualizarMedico(Integer idMedico, MedicoVistaAdminDTO medicoDTO);
    void eliminarMedico(Integer idMedico);
    List<MedicoVistaAdminDTO> listarMedicosTodos();
    MedicoVistaAdminDTO buscarMedicoPorId(Integer idMedico);
}
