package pe.edu.pucp.salud360.autenticacion.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarCorreoRequest {
    private String correoNuevo;
}
