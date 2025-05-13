package pe.edu.pucp.salud360.membresia.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ComunidadMapper.class, AfiliacionMapper.class})
public interface MembresiaMapper {
    MembresiaResumenDTO mapToMembresiaDTO(Membresia membresia);
    Membresia mapToModel(MembresiaResumenDTO membresiaDTO);

    MembresiaDTO mapToDTO(Membresia membresia);
    Membresia mapToModel(MembresiaDTO membresiaDTO);

    List<Membresia> mapToModelList(List<MembresiaResumenDTO> membresiaDTO);
}
