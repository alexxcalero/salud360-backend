package pe.edu.pucp.salud360.usuario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.usuario.models.Rol;

import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    List<Rol> findAllByOrderByIdRolAsc();
    List<Rol> findAllByNombreContainingIgnoreCaseOrderByIdRolAsc(String nombre);
}
