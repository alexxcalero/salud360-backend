package pe.edu.pucp.salud360.usuario.dtos.usuarioDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaClienteDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResumenDTO {
    private Integer idUsuario;
    private String correo;
    private RolVistaClienteDTO rol;
}
