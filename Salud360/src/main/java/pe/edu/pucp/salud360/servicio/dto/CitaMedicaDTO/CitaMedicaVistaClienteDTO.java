package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CitaMedicaVistaClienteDTO {
    private Integer idCitaMedica;
    private LocalTime hora;
    private LocalDate fecha;
    private String estado;

    private MedicoResumenDTO medico;
}
