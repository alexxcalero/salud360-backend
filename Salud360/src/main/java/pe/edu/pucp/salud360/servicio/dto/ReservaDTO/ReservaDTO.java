package pe.edu.pucp.salud360.servicio.dto.ReservaDTO;

import lombok.*;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Integer idReserva;
    private LocalDate fechaMaxCancelacion;
    private LocalTime horaMaxCancelacion;
    private String estado;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaCancelacion;
    private ClienteResumenDTO cliente;
    private ClaseResumenDTO clase;
    private CitaMedicaResumenDTO citaMedica;
    private ComunidadResumenDTO comunidad;
    //Con fe
    private String descripcion;
    private String nombreArchivo;
}
