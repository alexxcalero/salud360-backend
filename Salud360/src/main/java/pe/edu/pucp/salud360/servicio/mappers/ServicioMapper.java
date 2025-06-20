package pe.edu.pucp.salud360.servicio.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaClienteDTO;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaAdminDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ComunidadMapper.class, LocalMapper.class, CitaMedicaMapper.class})
public interface ServicioMapper {

    // CRUD: crear / editar
    @Mapping(source = "imagen", target = "imagen")
    ServicioDTO mapToDTO(Servicio servicio);
    Servicio mapToModel(ServicioDTO dto);
    List<ServicioDTO> mapToDTOList(List<Servicio> servicios); //listarservicioporcomunidad
    // Vista admin
    ServicioVistaAdminDTO mapToVistaAdminDTO(Servicio servicio);
    Servicio mapToModel(ServicioVistaAdminDTO dto);

    List<ServicioVistaAdminDTO> mapToVistaAdminList(List<Servicio> servicios);

    // Listados
    @Mapping(source = "imagen", target = "imagen")
    ServicioResumenDTO mapToResumenDTO(Servicio servicio);
    @Mapping(source = "imagen", target = "imagen")
    Servicio mapToModel(ServicioResumenDTO dto);

    List<ServicioResumenDTO> mapToResumenList(List<Servicio> servicios);

    ServicioVistaClienteDTO mapToVistaClienteDTO(Servicio servicio);
    Servicio mapToModel(ServicioVistaClienteDTO dto);
}
