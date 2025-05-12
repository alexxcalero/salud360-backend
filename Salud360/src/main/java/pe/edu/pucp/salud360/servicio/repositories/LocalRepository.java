package pe.edu.pucp.salud360.servicio.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.servicio.models.Local;

import java.awt.print.Pageable;

public interface LocalRepository extends JpaRepository<Local, Integer> {
    Page<Local> findByActivoTrue(Pageable pageable);
}
