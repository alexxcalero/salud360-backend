package pe.edu.pucp.salud360.servicio.services;

import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocalService {
    LocalVistaAdminDTO crearLocal(LocalVistaAdminDTO localVistaAdminDTO);
    LocalVistaAdminDTO actualizarLocal(Integer idLocal, LocalVistaAdminDTO localVistaAdminDTO);
    void eliminarLocal(Integer idLocal);
    List<LocalVistaAdminDTO> listarLocalesTodos();
    Page<LocalVistaAdminDTO> listarLocalesPaginado(Pageable pageable);
    LocalVistaAdminDTO buscarLocalPorId(Integer idLocal);
}
