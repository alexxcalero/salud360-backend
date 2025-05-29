package pe.edu.pucp.salud360.comunidad.mappers;

import org.mapstruct.*;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadVistaClienteDTO;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.membresia.mappers.MembresiaMapper;
import pe.edu.pucp.salud360.servicio.mappers.ServicioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ServicioMapper.class,
        MembresiaMapper.class,
        TestimonioMapper.class
})
public interface ComunidadMapper {

    ComunidadDTO mapToDTO(Comunidad comunidad);

    @Mapping(target = "servicios", ignore = true)
    @Mapping(target = "membresias", ignore = true)
    @Mapping(target = "testimonios", ignore = true)
    @Mapping(target = "clientes", ignore = true)
    Comunidad mapToModel(ComunidadDTO dto);

    ComunidadResumenDTO mapToResumenDTO(Comunidad comunidad);
    List<ComunidadResumenDTO> mapToResumenList(List<Comunidad> comunidades);

    List<ComunidadDTO> mapToDTOList(List<Comunidad> comunidades);

    ComunidadVistaClienteDTO mapToVistaClienteDTO(Comunidad comunidad);
    Comunidad mapToModel(ComunidadVistaClienteDTO dto);
}
