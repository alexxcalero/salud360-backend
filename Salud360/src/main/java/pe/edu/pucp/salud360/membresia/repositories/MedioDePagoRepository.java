package pe.edu.pucp.salud360.membresia.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;

import java.util.List;

public interface MedioDePagoRepository extends JpaRepository<MedioDePago, Integer> {
    List<MedioDePago> findByCliente_IdCliente(Integer idCliente);
}

