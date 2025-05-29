package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;

import lombok.*;

import pe.edu.pucp.salud360.servicio.dto.DocumentoMedicoDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaMedicaVistaClienteDTO {
    private Integer idCitaMedica;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private ServicioResumenDTO servicio;
    private List<DocumentoMedicoDTO> documentosMedicos;
    private MedicoResumenDTO medico;
}
