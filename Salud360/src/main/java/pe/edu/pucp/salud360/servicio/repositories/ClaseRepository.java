package pe.edu.pucp.salud360.servicio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.servicio.models.Clase;

import java.time.LocalDate;
import java.util.List;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {
    List<Clase> findByLocalIdLocalAndFecha(Integer idLocal, LocalDate fecha);
}
