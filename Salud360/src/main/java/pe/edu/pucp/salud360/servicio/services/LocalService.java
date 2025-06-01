package pe.edu.pucp.salud360.servicio.services;

import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocalService {
    LocalVistaAdminDTO crearLocal(LocalVistaAdminDTO localVistaAdminDTO);
    LocalVistaAdminDTO actualizarLocal(Integer idLocal, LocalVistaAdminDTO localVistaAdminDTO);
    void eliminarLocal(Integer idLocal);
    void reactivarLocal(Integer idLocal);
    List<LocalVistaAdminDTO> listarLocalesTodos();
    List<LocalDTO> listarLocalesResumen();
    Page<LocalVistaAdminDTO> listarLocalesPaginado(Pageable pageable);
    LocalVistaAdminDTO buscarLocalPorId(Integer idLocal);
}
