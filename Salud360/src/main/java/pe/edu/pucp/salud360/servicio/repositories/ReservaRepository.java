package pe.edu.pucp.salud360.servicio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.salud360.servicio.models.Reserva;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    @Query("SELECT r FROM Reserva r WHERE r.cliente.idCliente = :idCliente AND r.comunidad.idComunidad = :idComunidad AND r.estado = 'Confirmada'")
    List<Reserva> findByClienteAndComunidad(@Param("idCliente") Integer idCliente, @Param("idComunidad") Integer idComunidad);
}
