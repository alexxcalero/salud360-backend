package pe.edu.pucp.salud360.control.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.control.dto.ReglasDeNegocioDTO;
import pe.edu.pucp.salud360.control.models.ReglasDeNegocio;

@Mapper(componentModel = "spring")
public interface ReglasDeNegocioMapper {
    ReglasDeNegocioDTO mapToDTO(ReglasDeNegocio reglas);
    ReglasDeNegocio mapToModel(ReglasDeNegocioDTO reglasDTO);
}
