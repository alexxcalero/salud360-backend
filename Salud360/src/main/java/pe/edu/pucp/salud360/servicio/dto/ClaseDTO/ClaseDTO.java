package pe.edu.pucp.salud360.servicio.dto.ClaseDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ClaseDTO {
    private Integer idClase;
    private String nombre;
    private String descripcion;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalDate fecha;
    private Integer capacidad;
    private Integer cantAsistentes;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
}

