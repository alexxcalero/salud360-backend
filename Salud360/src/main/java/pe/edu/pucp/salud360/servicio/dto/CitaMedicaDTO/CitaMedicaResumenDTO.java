package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.personaDTO.PersonaResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

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

    private PersonaResumenDTO paciente;
    private MedicoResumenDTO medico;
}

