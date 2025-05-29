package pe.edu.pucp.salud360.usuario.services;

import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaAdminDTO;

import java.util.List;

public interface RolService {
    RolVistaAdminDTO crearRol(RolVistaAdminDTO rolDTO);
    RolVistaAdminDTO actualizarRol(Integer idRol, RolVistaAdminDTO rolDTO);
    void eliminarRol(Integer idRol);
    void reactivarRol(Integer idRol);
    List<RolVistaAdminDTO> listarRoles(String nombre);
    RolVistaAdminDTO buscarRolPorId(Integer idRol);
    //RolVistaAdminDTO editarPermisos(Integer idRol, List<PermisoResumenDTO> permisos);
}
