package pe.edu.pucp.salud360.usuario.dtos.administradorDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorRegistroDTO {
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String correo;
    private String contrasenha;
    private String sexo;
    private String telefono;
    private TipoDocumentoResumenDTO tipoDocumento;
}
