package pe.edu.pucp.salud360.servicio.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalDTO;


import java.io.IOException;
import java.util.List;

public interface LocalService {
    LocalVistaAdminDTO crearLocal(LocalVistaAdminDTO localVistaAdminDTO);
    LocalVistaAdminDTO actualizarLocal(Integer idLocal, LocalVistaAdminDTO localVistaAdminDTO);
    void eliminarLocal(Integer idLocal);
    void reactivarLocal(Integer idLocal);
    List<LocalVistaAdminDTO> listarLocalesTodos();
    List<LocalDTO> listarLocalesResumen();
    LocalVistaAdminDTO buscarLocalPorId(Integer idLocal);

    //CON FE
    Boolean cargarMasivamante(MultipartFile file)throws IOException;
}
