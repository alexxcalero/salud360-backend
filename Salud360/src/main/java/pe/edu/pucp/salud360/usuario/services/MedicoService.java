package pe.edu.pucp.salud360.usuario.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;

import java.io.IOException;
import java.util.List;

public interface MedicoService {
    MedicoVistaAdminDTO crearMedico(MedicoRegistroDTO medicoDTO);
    MedicoVistaAdminDTO actualizarMedico(Integer idMedico, MedicoVistaAdminDTO medicoDTO);
    void eliminarMedico(Integer idMedico);
    void reactivarMedico(Integer idMedico);
    List<MedicoVistaAdminDTO> listarMedicos(String nombreCompleto, String especialidad);
    MedicoVistaAdminDTO buscarMedicoPorId(Integer idMedico);
    Boolean cargarMasivamanteMedico(MultipartFile file) throws IOException;
}
