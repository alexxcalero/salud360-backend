package pe.edu.pucp.salud360.autenticacion.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarContrasenhaRequest {
    private String contrasenhaActual;
    private String contrasenhaNueva;
}
