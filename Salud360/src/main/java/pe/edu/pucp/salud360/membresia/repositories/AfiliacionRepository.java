package pe.edu.pucp.salud360.membresia.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;

import java.util.List;
import java.util.Optional;

public interface AfiliacionRepository extends JpaRepository<Afiliacion, Integer> {
    @EntityGraph(attributePaths = { "membresia", "membresia.comunidad" })
    Optional<Afiliacion> findById(Integer id);

    @Query(value = """
SELECT a.* FROM ingesoft.afiliacion a
JOIN ingesoft.membresia m ON a.id_membresia = m.id_membresia
WHERE a.id_cliente = :idCliente  AND m.id_comunidad IS NOT NULL
""", nativeQuery = true)
    List<Afiliacion> findAfiliacionesActivasConComunidadByCliente(@Param("idCliente") Integer idCliente);


}

