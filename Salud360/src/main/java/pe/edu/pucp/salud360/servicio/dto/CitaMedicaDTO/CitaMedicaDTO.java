package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;

import lombok.*;

import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaMedicaDTO {
    private Integer idCitaMedica;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private ServicioDTO servicio;
    private ClienteResumenDTO cliente;
    private MedicoResumenDTO medico;
    //Para que funque en la vista de admin
    private String nombreArchivo;

}
