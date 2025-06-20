package pe.edu.pucp.salud360.membresia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;
import pe.edu.pucp.salud360.membresia.models.Pago;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MedioDePagoMapper.class, AfiliacionMapper.class})
public interface PagoMapper {

    @Mapping(target = "afiliacion", source = "afiliacion")
    PagoDTO mapToDTO(Pago pago);

    @Mapping(target = "afiliacion", source = "afiliacion")
    Pago mapToModel(PagoDTO pagoDTO);

    List<PagoDTO> mapToDTOList(List<Pago> pagos);

    List<Pago> mapToModelList(List<PagoDTO> pagosDTO);
}