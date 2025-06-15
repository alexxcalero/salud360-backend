package pe.edu.pucp.salud360.membresia.dtos.afiliacion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;

import java.time.LocalDateTime;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AfiliacionResumenResumenDTO {
    private MembresiaResumenDTO membresia;
    private Integer idAfiliacion;
    private Integer idComunidad;
    private String estado;
    private LocalDateTime fechaAfiliacion;
    private LocalDateTime fechaDesafiliacion;
}
