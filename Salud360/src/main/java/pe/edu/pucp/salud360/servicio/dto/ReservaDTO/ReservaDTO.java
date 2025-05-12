package pe.edu.pucp.salud360.servicio.dto.ReservaDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Integer idReserva;
    private LocalDateTime fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalDateTime horaNotificacion;
    private String estado;

    private LocalDateTime fechaCancelacion;
    private LocalDateTime fechaReprogramacion;
    private Boolean activo;

    private Integer idClase;         // Relación simplificada
    private Integer idCitaMedica;    // Relación simplificada
    private Integer idUsuario;       // Persona que reserva
}

