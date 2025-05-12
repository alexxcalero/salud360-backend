package pe.edu.pucp.salud360.servicio.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseResumenDTO;
import pe.edu.pucp.salud360.servicio.models.Clase;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClaseMapper {

    // Mapear entre entidad completa y DTO completo
    ClaseDTO mapToDTO(Clase clase);
    Clase mapToModel(ClaseDTO dto);

    // Mapear entre entidad y resumen DTO
    ClaseResumenDTO mapToResumenDTO(Clase clase);
    Clase mapToModel(ClaseResumenDTO resumenDTO);

    // Mapear listas completas
    List<ClaseDTO> mapToDTOList(List<Clase> clases);
    List<Clase> mapToModelList(List<ClaseDTO> clasesDTO);
}

