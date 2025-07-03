package pe.edu.pucp.salud360.membresia.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;

import java.io.IOException;
import java.util.List;

public interface AfiliacionService {
    AfiliacionResumenDTO crearAfiliacion(AfiliacionDTO dto);
    List<AfiliacionResumenDTO> listarAfiliaciones();
    List<AfiliacionResumenDTO> listarAfiliacionesPorCliente(Integer idCliente);


    boolean eliminarAfiliacion(Integer id);
    AfiliacionResumenDTO buscarAfiliacionPorId(Integer id);
    AfiliacionResumenDTO actualizarAfiliacion(Integer id, AfiliacionDTO dto);
    boolean desafiliar(Integer id, Integer ndias);
    boolean reactivarAfiliacion(Integer idAfiliacion);
    Boolean cargarMasivamanteAfiliacion(MultipartFile file) throws IOException;

}

