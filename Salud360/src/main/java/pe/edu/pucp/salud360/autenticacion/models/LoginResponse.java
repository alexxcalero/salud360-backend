package pe.edu.pucp.salud360.autenticacion.models;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioLogueadoDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private UsuarioLogueadoDTO usuario;
}
