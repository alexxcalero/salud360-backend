package pe.edu.pucp.salud360.comunidad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.comunidad.models.Testimonio;

import java.util.Optional;

public interface TestimonioRepository extends JpaRepository<Testimonio, Integer> {
    Optional<Testimonio> findById(Integer id);
}