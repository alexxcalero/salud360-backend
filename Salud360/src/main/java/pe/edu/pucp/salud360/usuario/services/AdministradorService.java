package pe.edu.pucp.salud360.usuario.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorResumenDTO;

import java.io.IOException;
import java.util.List;

public interface AdministradorService {
    AdministradorResumenDTO crearAdministrador(AdministradorRegistroDTO administradorDTO);
    AdministradorLogueadoDTO actualizarAdministrador(Integer idAdministrador, AdministradorLogueadoDTO administradorDTO);
    void eliminarAdministrador(Integer idAdministrador);
    void reactivarAdministrador(Integer idAdministrador);
    List<AdministradorResumenDTO> listarAdministradores();
    AdministradorResumenDTO buscarAdministradorPorId(Integer idAdministrador);
    AdministradorLogueadoDTO buscarAdminPorId(Integer idAdministrador);
    Boolean cargarMasivamanteAdmin(MultipartFile file) throws IOException;
}
