package pe.edu.pucp.salud360.membresia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    private Integer idPago;
    private Double monto;
    private LocalDateTime fechaPago;
    private Integer idAfiliacion;
    private MedioDePagoResumenDTO medioDePago;
}

