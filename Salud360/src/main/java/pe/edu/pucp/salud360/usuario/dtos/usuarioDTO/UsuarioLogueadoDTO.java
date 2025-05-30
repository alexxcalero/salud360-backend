package pe.edu.pucp.salud360.usuario.dtos.usuarioDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaClienteDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLogueadoDTO {
    private Integer idUsuario;
    private String correo;
    private RolVistaClienteDTO rol;
    private ClienteLogueadoDTO cliente;
    private AdministradorLogueadoDTO administrador;
}
