package pe.edu.pucp.salud360.servicio.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaClienteDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaMedicoDTO;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.usuario.mappers.ClienteMapper;
import pe.edu.pucp.salud360.usuario.mappers.MedicoMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ServicioMapper.class, MedicoMapper.class, ClienteMapper.class, DocumentoMedicoMapper.class})
public class CitaMedicaMapperHelper {

    public static CitaMedicaDTO mapToDTOIncluyendoArchivo(CitaMedicaMapper mapper, CitaMedica citaMedica) {
        CitaMedicaDTO dto = mapper.mapToDTO(citaMedica);

        if (citaMedica.getReservas() != null) {
            citaMedica.getReservas().stream()
                    .filter(r -> "Confirmada".equalsIgnoreCase(r.getEstado()))
                    .findFirst()
                    .ifPresent(r -> dto.setNombreArchivo(r.getNombreArchivo())); // CAMBIO AQUÍ
        }

        return dto;
    }

}
