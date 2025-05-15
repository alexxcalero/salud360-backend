package pe.edu.pucp.salud360.control.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReglasDeNegocioDTO {
    private Integer idRegla;
    private Integer maxReservas;
    private Integer maxCapacidad;
    private Integer maxDiasSuspension;
    private Integer maxTiempoCancelacion;
}
