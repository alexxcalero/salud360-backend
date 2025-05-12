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
public class ReservaResumenDTO {
    private Integer idReserva;
    private String estado;
    private LocalDateTime fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}

