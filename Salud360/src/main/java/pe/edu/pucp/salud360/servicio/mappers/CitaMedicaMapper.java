package pe.edu.pucp.salud360.servicio.mappers;


import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface CitaMedicaMapper {
    CitaMedicaDTO mapToDTO(CitaMedica citaMedica);
    CitaMedica mapToModel(CitaMedicaDTO dto);

    CitaMedicaResumenDTO mapToResumenDTO(CitaMedica citaMedica);
    CitaMedica mapToModel(CitaMedicaResumenDTO resumenDTO);

    List<CitaMedicaDTO> mapToDTOList(List<CitaMedica> citas);
    List<CitaMedica> mapToModelList(List<CitaMedicaDTO> citasDTO);
}

