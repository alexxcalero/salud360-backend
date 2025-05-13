package pe.edu.pucp.salud360.membresia.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;

@Data
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
    private Integer idSolicitud;
    private Integer cantDias;
    private String estado;
    private AfiliacionResumenDTO afiliacion;
}
