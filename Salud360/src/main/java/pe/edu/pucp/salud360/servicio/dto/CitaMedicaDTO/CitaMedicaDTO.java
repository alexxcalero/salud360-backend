package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaResumenDTO;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CitaMedicaDTO {
    private Integer idCitaMedica;
    private LocalTime hora;
    private LocalDate fecha;
    private String estado;
    private String motivo;
    private Boolean activo;
    private LocalDate fechaCreacion;
    private LocalDate fechaDesactivacion;
    private Integer idServicio;

    private PersonaResumenDTO paciente;
    private MedicoResumenDTO medico;
}
