package pe.edu.pucp.salud360.control.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.control.models.ReglasDeNegocio;

public interface ReglasDeNegocioRepository extends JpaRepository<ReglasDeNegocio, Integer> {
}
