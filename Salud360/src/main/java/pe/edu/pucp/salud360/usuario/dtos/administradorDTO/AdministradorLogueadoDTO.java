package pe.edu.pucp.salud360.usuario.dtos.administradorDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorLogueadoDTO {
    private Integer idAdministrador;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String sexo;
    private String telefono;
    private String fotoPerfil;
    private Boolean notificacionPorCorreo;
    private Boolean notificacionPorSMS;
    private Boolean notificacionPorWhatsApp;
    private TipoDocumentoResumenDTO tipoDocumento;
    private UsuarioResumenDTO usuario;
}
