package pe.edu.pucp.salud360.membresia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;
import pe.edu.pucp.salud360.membresia.models.Pago;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MedioDePagoMapper.class})
public interface PagoMapper {

    @Mapping(target = "idAfiliacion", source = "afiliacion.idAfiliacion")
    PagoDTO mapToDTO(Pago pago);

    @Mapping(target = "afiliacion.idAfiliacion", source = "idAfiliacion")
    Pago mapToModel(PagoDTO pagoDTO);

    List<PagoDTO> mapToDTOList(List<Pago> pagos);

    List<Pago> mapToModelList(List<PagoDTO> pagosDTO);
}