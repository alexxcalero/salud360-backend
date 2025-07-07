package pe.edu.pucp.salud360.usuario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.salud360.usuario.models.Administrador;
import pe.edu.pucp.salud360.usuario.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    @Query("SELECT a FROM Administrador a WHERE CONCAT(LOWER(a.nombres), ' ', LOWER(a.apellidos)) LIKE LOWER(CONCAT('%', :cadena, '%'))")
    List<Administrador> buscarPorNombreCompleto(@Param("cadena") String cadena);

    Optional<Administrador> findByUsuario(Usuario usuario);
    List<Administrador> findByNombresAndNumeroDocumento(String nombres, String numeroDocumento);

}
