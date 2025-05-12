package pe.edu.pucp.salud360.servicio.dto.ReservaDTO;



import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaVistaAdminDTO {
    private Integer idReserva;
    private LocalDateTime fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalDateTime horaNotificacion;
    private String estado;

    private LocalDateTime fechaCancelacion;
    private LocalDateTime fechaReprogramacion;
    private Boolean activo;

    // Relaciones detalladas para mostrar info en admin
    private UsuarioResumenDTO usuario;
    private ClaseResumenDTO clase;
    private CitaMedicaResumenDTO citaMedica;
}

