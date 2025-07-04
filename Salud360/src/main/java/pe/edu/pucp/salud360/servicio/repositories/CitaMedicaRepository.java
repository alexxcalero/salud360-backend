package pe.edu.pucp.salud360.servicio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;

import java.time.LocalDate;
import java.util.List;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Integer> {
    List<CitaMedica> findByMedicoIdMedicoAndFecha(Integer idMedico, LocalDate fecha);

}
