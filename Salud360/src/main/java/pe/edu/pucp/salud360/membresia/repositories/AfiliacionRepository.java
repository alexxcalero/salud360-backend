package pe.edu.pucp.salud360.membresia.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;

import java.util.Optional;

public interface AfiliacionRepository extends JpaRepository<Afiliacion, Integer> {
    @EntityGraph(attributePaths = { "membresia", "membresia.comunidad" })
    Optional<Afiliacion> findById(Integer id);
}

