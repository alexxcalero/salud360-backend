package pe.edu.pucp.salud360.membresia.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;

import java.time.LocalDate;

@Data
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PeriodoDTO {
    private Integer idPeriodo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cantReservas;
    private AfiliacionResumenDTO afiliacion;
}