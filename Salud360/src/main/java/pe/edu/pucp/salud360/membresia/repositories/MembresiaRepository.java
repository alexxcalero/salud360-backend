package pe.edu.pucp.salud360.membresia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.salud360.membresia.models.Membresia;

import java.util.List;

public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {
}
