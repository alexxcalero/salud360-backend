package pe.edu.pucp.salud360.usuario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.salud360.usuario.models.Medico;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    List<Medico> findAllByOrderByIdMedicoAsc();
    List<Medico> findAllByEspecialidadContainingIgnoreCaseOrderByIdMedicoAsc(String especialidad);

    Optional<Medico> findByNumeroDocumento(String numeroDocumento);

    @Query("SELECT m FROM Medico m WHERE CONCAT(LOWER(m.nombres), ' ', LOWER(m.apellidos)) LIKE LOWER(CONCAT('%', :nombreCompleto, '%')) ORDER BY m.idMedico")
    List<Medico> buscarPorNombreCompleto(@Param("nombreCompleto") String nombreCompleto);

    @Query("SELECT m FROM Medico m WHERE CONCAT(LOWER(m.nombres), ' ', LOWER(m.apellidos)) LIKE LOWER(CONCAT('%', :nombreCompleto, '%')) AND LOWER(m.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%')) ORDER BY m.idMedico")
    List<Medico> buscarPorNombreYEspecialidad(@Param("nombreCompleto") String nombreCompleto, @Param("especialidad") String especialidad);
}
