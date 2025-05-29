package pe.edu.pucp.salud360.usuario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.usuario.models.Cliente;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Integer idNotificacion;
    private String mensaje;
    private LocalDateTime fecha;
    private String tipo;
    private Cliente cliente;
    private Reserva reserva;
}
