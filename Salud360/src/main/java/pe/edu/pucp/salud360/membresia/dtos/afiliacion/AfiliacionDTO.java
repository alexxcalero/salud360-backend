package pe.edu.pucp.salud360.membresia.dtos.afiliacion;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AfiliacionDTO {
    private MembresiaResumenDTO membresia;
    private Integer idAfiliacion;
    private String estado;
    private LocalDateTime fechaAfiliacion;
    private LocalDateTime fechaDesafiliacion;
    private LocalDate fechaReactivacion;
    private MedioDePagoResumenDTO medioDePago;
    private UsuarioResumenDTO usuario;
}


