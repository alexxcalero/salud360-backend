package pe.edu.pucp.salud360.usuario.dtos.medicoDTO;

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
public class MedicoRegistroDTO extends UsuarioRegistroDTO {
    private String especialidad;
    private String descripcion;
}
