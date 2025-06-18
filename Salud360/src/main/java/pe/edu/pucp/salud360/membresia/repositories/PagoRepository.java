package pe.edu.pucp.salud360.membresia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.salud360.membresia.models.Pago;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    @Query(value = """
SELECT p.*
FROM ingesoft.pago p
JOIN ingesoft.afiliacion a ON p.id_afiliacion = a.id_afiliacion
WHERE a.id_cliente = :idCliente
""", nativeQuery = true)
    List<Pago> findPagosByIdCliente(@Param("idCliente") Integer idCliente);

}
