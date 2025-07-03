package pe.edu.pucp.salud360.servicio.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;

import java.io.IOException;
import java.util.List;

public interface ClaseService {
    ClaseDTO crearClase(ClaseDTO claseDTO);
    ClaseDTO actualizarClase(Integer idClase, ClaseDTO claseDTO);
    void eliminarClase(Integer idClase);
    void reactivarClase(Integer idClase);
    List<ClaseDTO> listarClasesTodas();
    ClaseDTO buscarClasePorId(Integer idClase);

    //CON FE
    Boolean cargarMasivamante(MultipartFile file)throws IOException;
}
