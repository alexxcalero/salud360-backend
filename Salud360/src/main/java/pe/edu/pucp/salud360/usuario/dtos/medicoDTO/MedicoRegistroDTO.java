package pe.edu.pucp.salud360.usuario.dtos.medicoDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicoRegistroDTO {
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String sexo;
    private String especialidad;
    private String descripcion;
    //Ahora solo 1 imagen
    private String fotoPerfil;
    private TipoDocumentoResumenDTO tipoDocumento;
}
