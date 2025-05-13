package pe.edu.pucp.salud360.membresia.dtos.mediopago;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MedioDePagoDTO {
    private Integer idMedioDePago;
    private String tipo;
    private Integer ncuenta;
    private LocalDateTime vencimiento;
    private Integer cvv;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private UsuarioResumenDTO usuario;
}
