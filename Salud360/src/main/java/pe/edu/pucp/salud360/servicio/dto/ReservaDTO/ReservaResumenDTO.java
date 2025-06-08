package pe.edu.pucp.salud360.servicio.dto.ReservaDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResumenDTO {
    private Integer idReserva;
    private String estado;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaCancelacion;
    private ClienteResumenDTO cliente;
}
