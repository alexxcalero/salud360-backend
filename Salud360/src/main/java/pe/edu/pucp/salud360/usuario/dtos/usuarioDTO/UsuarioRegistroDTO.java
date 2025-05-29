package pe.edu.pucp.salud360.usuario.dtos.usuarioDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {
    private String correo;
    private String contrasenha;
}
