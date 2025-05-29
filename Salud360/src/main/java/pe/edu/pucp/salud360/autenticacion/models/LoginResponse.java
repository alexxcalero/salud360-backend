package pe.edu.pucp.salud360.autenticacion.models;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private UsuarioResumenDTO usuario;
}
