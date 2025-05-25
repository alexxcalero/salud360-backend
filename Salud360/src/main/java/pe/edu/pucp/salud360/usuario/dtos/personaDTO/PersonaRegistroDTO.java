package pe.edu.pucp.salud360.usuario.dtos.personaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioRegistroDTO;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRegistroDTO extends UsuarioRegistroDTO {
    private String direccion;
}
