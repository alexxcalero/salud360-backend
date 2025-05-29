package pe.edu.pucp.salud360.usuario.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.mappers.RolMapper;
import pe.edu.pucp.salud360.usuario.models.Rol;
import pe.edu.pucp.salud360.usuario.repositories.RolRepository;
import pe.edu.pucp.salud360.usuario.services.RolService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolServiceImp implements RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    public RolVistaAdminDTO crearRol(RolVistaAdminDTO rolDTO) {
        Rol rol = rolMapper.mapToModel(rolDTO);
        rol.setActivo(true);
        rol.setFechaCreacion(LocalDateTime.now());
        rol.setUsuarios(new ArrayList<>());
        Rol rolCreado = rolRepository.save(rol);
        return rolMapper.mapToVistaAdminDTO(rolCreado);
    }

    @Override
    public RolVistaAdminDTO actualizarRol(Integer idRol, RolVistaAdminDTO rolDTO) {
        Optional<Rol> rolSeleccionado = rolRepository.findById(idRol);
        if(rolSeleccionado.isPresent()) {
            Rol rol = rolSeleccionado.get();
            rol.setNombre(rolDTO.getNombre());
            rol.setDescripcion(rolDTO.getDescripcion());
            Rol rolActualizado = rolRepository.save(rol);
            return rolMapper.mapToVistaAdminDTO(rolActualizado);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarRol(Integer idRol) {
        Optional<Rol> rolSeleccionado = rolRepository.findById(idRol);
        if(rolSeleccionado.isPresent()) {
            Rol rolEliminar = rolSeleccionado.get();

            /*
            for(Permiso permiso : rolEliminar.getPermisos()) {
                permiso.getRoles().remove(rolEliminar);  // Elimino al rol de la lista del permiso
            }

            // Desasocio los permisos de un rol que se va a eliminar
            // de esta manera se eliminan los registros de la tabla intermedia
            rolEliminar.getPermisos().clear();
            */

            rolEliminar.setActivo(false);
            rolEliminar.setFechaDesactivacion(LocalDateTime.now());
            rolRepository.save(rolEliminar);
        }
    }

    @Override
    public void reactivarRol(Integer idRol) {
        Optional<Rol> rolSeleccionado = rolRepository.findById(idRol);
        if(rolSeleccionado.isPresent()) {
            Rol rolReactivar = rolSeleccionado.get();
            rolReactivar.setActivo(true);
            rolReactivar.setFechaDesactivacion(null);
            rolRepository.save(rolReactivar);
        }
    }

    @Override
    public List<RolVistaAdminDTO> listarRoles(String nombre) {
        List<Rol> roles;

        if(nombre == null || nombre.isBlank()) {
            roles = rolRepository.findAllByOrderByIdRolAsc();
        } else {
            roles = rolRepository.findAllByNombreContainingIgnoreCaseOrderByIdRolAsc(nombre);
        }

        if(!(roles.isEmpty())) {
            return roles.stream().map(rolMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public RolVistaAdminDTO buscarRolPorId(Integer idRol) {
        Optional<Rol> rolBuscado = rolRepository.findById(idRol);
        if(rolBuscado.isPresent()) {
            Rol rol = rolBuscado.get();
            return rolMapper.mapToVistaAdminDTO(rol);
        } else {
            return null;
        }
    }

    /*
    @Override
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
    */
}
