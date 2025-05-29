package pe.edu.pucp.salud360.membresia.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoDTO;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface MedioDePagoMapper {
    MedioDePagoResumenDTO mapToMedioDePagoDTO(MedioDePago medioDePago);
    MedioDePago mapToModel(MedioDePagoResumenDTO afiliacionDTO);

    MedioDePagoDTO mapToDTO(MedioDePago afiliacion);
    MedioDePago mapToModel(MedioDePagoDTO afiliacionDTO);

    List<MedioDePago> mapToModelList(List<MedioDePagoResumenDTO> afiliacionesDTO);
}
