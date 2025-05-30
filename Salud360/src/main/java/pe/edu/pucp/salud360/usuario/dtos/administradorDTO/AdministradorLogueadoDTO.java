package pe.edu.pucp.salud360.usuario.dtos.administradorDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaClienteDTO;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorLogueadoDTO {
    private Integer idAdministrador;
    private String correo;
    //private String rol;
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
    private RolVistaClienteDTO rol;
    //private UsuarioResumenDTO usuario;
}
