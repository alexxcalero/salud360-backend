package pe.edu.pucp.salud360.servicio.dto.ReservaDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResumenDTO {
    private Integer idReserva;
    private LocalDate fechaMaxCancelacion;
    private LocalTime horaMaxCancelacion;
    private String estado;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaCancelacion;
    private ClienteResumenDTO cliente;
}
