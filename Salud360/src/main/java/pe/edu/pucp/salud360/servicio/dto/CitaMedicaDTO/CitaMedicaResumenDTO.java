package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CitaMedicaResumenDTO {
    private Integer idCitaMedica;
    private LocalTime hora;
    private LocalDate fecha;
    private String estado;

    private ClienteResumenDTO paciente;
    private MedicoResumenDTO medico;
}

