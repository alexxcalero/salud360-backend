package pe.edu.pucp.salud360.usuario.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.salud360.usuario.dtos.permisoDTO.PermisoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.mappers.PermisoMapper;
import pe.edu.pucp.salud360.usuario.mappers.RolMapper;
import pe.edu.pucp.salud360.usuario.models.Permiso;
import pe.edu.pucp.salud360.usuario.models.Rol;
import pe.edu.pucp.salud360.usuario.repositories.RolRepository;
import pe.edu.pucp.salud360.usuario.services.RolService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolServiceImp implements RolService {
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMapper rolMapper;

    @Autowired
    private PermisoMapper permisoMapper;

    @Override
    public RolVistaAdminDTO crearRol(RolVistaAdminDTO rolDTO) {
        Rol rol = rolMapper.mapToModel(rolDTO);
        rol.setActivo(true);
        rol.setFechaCreacion(LocalDateTime.now());
        rol.setFechaDesactivacion(null);
        Rol rolCreado = rolRepository.save(rol);
        return rolMapper.mapToVistaAdminDTO(rolCreado);
    }

    @Override
    public RolVistaAdminDTO actualizarRol(Integer idRol, RolVistaAdminDTO rolDTO) {
        if(rolRepository.findById(idRol).isPresent()) {
            Rol rol = rolRepository.findById(idRol).get();
            rol.setNombre(rolDTO.getNombre());
            rol.setDescripcion(rolDTO.getDescripcion());
            Rol rolActualizado = rolRepository.save(rol);
            return rolMapper.mapToVistaAdminDTO(rolActualizado);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void eliminarRol(Integer idRol) {
        if(rolRepository.findById(idRol).isPresent()) {
            Rol rolEliminar = rolRepository.findById(idRol).get();

            for(Permiso permiso : rolEliminar.getPermisos()) {
                permiso.getRoles().remove(rolEliminar);  // Elimino al rol de la lista del permiso
            }

            // Desasocio los permisos de un rol que se va a eliminar
            // de esta manera se eliminan los registros de la tabla intermedia
            rolEliminar.getPermisos().clear();

            rolEliminar.setActivo(false);
            rolEliminar.setFechaDesactivacion(LocalDateTime.now());
            rolRepository.save(rolEliminar);
        }
    }

    @Override
    public List<RolVistaAdminDTO> listarRolesTodos() {
        List<Rol> roles = rolRepository.findAll();
        if(!(roles.isEmpty())) {
            return roles.stream().map(rolMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public RolVistaAdminDTO buscarRolPorId(Integer idRol) {
        if(rolRepository.findById(idRol).isPresent()) {
            Rol rolBuscado = rolRepository.findById(idRol).get();
            return rolMapper.mapToVistaAdminDTO(rolBuscado);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public RolVistaAdminDTO editarPermisos(Integer idRol, List<PermisoResumenDTO> permisos) {
        if(rolRepository.findById(idRol).isPresent()) {
            Rol rol = rolRepository.findById(idRol).get();

            for(Permiso permiso : rol.getPermisos()) {
                permiso.getRoles().remove(rol);  // Elimino al rol de la lista del permiso
            }

            rol.getPermisos().clear();  // Limpio la lista de permisos asociados

            List<Permiso> nuevosPermisos = permisoMapper.mapToModelList(permisos);
            if(nuevosPermisos != null) {
                for(Permiso permiso : nuevosPermisos) {
                    rol.getPermisos().add(permiso);  // Agrego el permiso a la nueva lista de permisos que contendra este rol
                    permiso.getRoles().add(rol);  // Mantengo la relación en el otro lado (el arreglo de roles en un permiso)
                }
            }

            Rol rolActualizado = rolRepository.save(rol);
            return rolMapper.mapToVistaAdminDTO(rolActualizado);
        } else {
            return null;
        }
    }
}
