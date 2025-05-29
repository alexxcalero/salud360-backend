package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.mappers.TestimonioMapper;
import pe.edu.pucp.salud360.membresia.mappers.AfiliacionMapper;
import pe.edu.pucp.salud360.membresia.mappers.MedioDePagoMapper;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.servicio.mappers.ClaseMapper;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteRegistroDTO;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteVistaAdminDTO;

@Mapper(componentModel = "spring",
        uses = {TipoDocumentoMapper.class,
                ComunidadMapper.class,
                AfiliacionMapper.class,
                ReservaMapper.class,
                ClaseMapper.class,
                CitaMedicaMapper.class,
                NotificacionMapper.class,
                MedioDePagoMapper.class,
                TestimonioMapper.class})
public interface ClienteMapper {
    ClienteLogueadoDTO mapToLogueadoDTO(Cliente cliente);
    Cliente mapToModel(ClienteLogueadoDTO clienteDTO);

    ClienteVistaAdminDTO mapToVistaAdminDTO(Cliente cliente);
    Cliente mapToModel(ClienteVistaAdminDTO clienteDTO);

    ClienteResumenDTO mapToResumenDTO(Cliente cliente);
    Cliente mapToModel(ClienteResumenDTO clienteDTO);

    Cliente mapToModel(ClienteRegistroDTO clienteDTO);
}
