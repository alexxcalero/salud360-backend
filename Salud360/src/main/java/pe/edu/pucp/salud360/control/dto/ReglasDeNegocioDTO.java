package pe.edu.pucp.salud360.control.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReglasDeNegocioDTO {
    Integer idRegla;
    Integer maxReservas;
    Integer maxCapacidad;
    Integer maxDiasSuspension;
    Integer maxTiempoCancelacion;
}
