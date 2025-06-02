package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
                TestimonioMapper.class,
                RolMapper.class})
public interface ClienteMapper {
    @Mapping(source = "usuario.correo", target = "correo")
    @Mapping(source = "usuario.rol", target = "rol")
    ClienteLogueadoDTO mapToLogueadoDTO(Cliente cliente);

    @Mapping(target = "usuario.correo", source = "correo")
    @Mapping(target = "usuario.rol", source = "rol")
    Cliente mapToModel(ClienteLogueadoDTO clienteDTO);

    @Mapping(source = "usuario.correo", target = "correo")
    @Mapping(source = "usuario.rol", target = "rol")
    ClienteVistaAdminDTO mapToVistaAdminDTO(Cliente cliente);

    @Mapping(target = "usuario.correo", source = "correo")
    @Mapping(target = "usuario.rol", source = "rol")
    Cliente mapToModel(ClienteVistaAdminDTO clienteDTO);

    @Mapping(source = "usuario.correo", target = "correo")
    @Mapping(source = "usuario.rol", target = "rol")
    ClienteResumenDTO mapToResumenDTO(Cliente cliente);

    @Mapping(target = "usuario.correo", source = "correo")
    @Mapping(target = "usuario.rol", source = "rol")
    Cliente mapToModel(ClienteResumenDTO clienteDTO);

    Cliente mapToModel(ClienteRegistroDTO clienteDTO);
}
