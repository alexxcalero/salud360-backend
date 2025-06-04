package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaMedicaResumenDTO {
    private Integer idCitaMedica;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalDate fecha;
    private String estado;

    //private ClienteResumenDTO cliente;
    private MedicoResumenDTO medico;
}
