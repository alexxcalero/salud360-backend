package pe.edu.pucp.salud360.servicio.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.models.Servicio;

import java.util.List;


public interface LocalRepository extends JpaRepository<Local, Integer> {
    Page<Local> findByActivoTrue(Pageable pageable);
    boolean existsByNombreAndServicio_IdServicio(String nombre, Integer idServicio);
    List<Local> findByNombreAndDireccion(String nombre, String direccion);
}
