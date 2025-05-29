package pe.edu.pucp.salud360.usuario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.salud360.usuario.models.TipoDocumento;

import java.util.List;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
    List<TipoDocumento> findAllByOrderByIdTipoDocumentoAsc();
    List<TipoDocumento> findAllByNombreContainingIgnoreCaseOrderByIdTipoDocumentoAsc(String nombre);
}
