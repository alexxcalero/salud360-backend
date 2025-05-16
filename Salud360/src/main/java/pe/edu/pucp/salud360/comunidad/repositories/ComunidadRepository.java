package pe.edu.pucp.salud360.comunidad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.servicio.models.Servicio;

import java.util.List;

@Repository
public interface ComunidadRepository extends JpaRepository<Comunidad, Integer> {
}
