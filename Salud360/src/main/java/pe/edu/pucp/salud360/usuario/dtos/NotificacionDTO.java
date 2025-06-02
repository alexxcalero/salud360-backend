package pe.edu.pucp.salud360.usuario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaResumenDTO;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.models.Cliente;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class    NotificacionDTO {
    private Integer idNotificacion;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String tipo;
    private ClienteResumenDTO cliente;
    private ReservaResumenDTO reserva;
}
