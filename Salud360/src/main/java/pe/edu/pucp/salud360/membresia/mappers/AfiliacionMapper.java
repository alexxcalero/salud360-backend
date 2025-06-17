package pe.edu.pucp.salud360.membresia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MedioDePagoMapper.class, UsuarioMapper.class, MembresiaMapper.class, ComunidadMapper.class})
public interface AfiliacionMapper {

    @Mapping(target = "comunidad", source = "membresia.comunidad")
    AfiliacionResumenDTO mapToAfiliacionDTO(Afiliacion afiliacion);
    Afiliacion mapToModel(AfiliacionResumenDTO afiliacionDTO);

    AfiliacionDTO mapToDTO(Afiliacion afiliacion);
    Afiliacion mapToModel(AfiliacionDTO afiliacionDTO);

    List<Afiliacion> mapToModelList(List<AfiliacionResumenDTO> afiliacionesDTO);

}

