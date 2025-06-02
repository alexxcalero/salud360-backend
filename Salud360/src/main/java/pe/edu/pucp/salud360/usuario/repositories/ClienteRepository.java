package pe.edu.pucp.salud360.usuario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    @Query("SELECT c FROM Cliente c WHERE CONCAT(LOWER(c.nombres), ' ', LOWER(c.apellidos)) LIKE LOWER(CONCAT('%', :cadena, '%'))")
    List<Cliente> buscarPorNombreCompleto(@Param("cadena") String cadena);
    Optional<Cliente> findByUsuario(Usuario usuario);
}
