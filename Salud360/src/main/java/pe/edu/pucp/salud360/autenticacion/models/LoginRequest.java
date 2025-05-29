package pe.edu.pucp.salud360.autenticacion.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String correo;
    private String contrasenha;
}
