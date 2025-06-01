package pe.edu.pucp.salud360.servicio.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaClienteDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaMedicoDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.usuario.mappers.MedicoMapper;
import pe.edu.pucp.salud360.usuario.mappers.ClienteMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MedicoMapper.class, ClienteMapper.class})
public interface CitaMedicaMapper {
    CitaMedicaDTO mapToDTO(CitaMedica citaMedica);
    CitaMedica mapToModel(CitaMedicaDTO dto);

    CitaMedicaResumenDTO mapToResumenDTO(CitaMedica citaMedica);
    CitaMedica mapToModel(CitaMedicaResumenDTO resumenDTO);

    CitaMedicaVistaMedicoDTO mapToVistaMedicoDTO(CitaMedica citaMedica);
    CitaMedica mapToModel(CitaMedicaVistaMedicoDTO citaMedicaDTO);

    CitaMedicaVistaClienteDTO mapToVistaClienteDTO(CitaMedica citaMedica);
    CitaMedica mapToModel(CitaMedicaVistaClienteDTO citaMedicaDTO);

    List<CitaMedicaDTO> mapToDTOList(List<CitaMedica> citas);
    List<CitaMedica> mapToModelList(List<CitaMedicaDTO> citasDTO);

    List<CitaMedicaResumenDTO> mapToResumenDTOList(List<CitaMedica> citas);

}
