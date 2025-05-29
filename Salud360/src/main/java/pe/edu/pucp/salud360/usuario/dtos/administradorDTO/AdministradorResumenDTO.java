package pe.edu.pucp.salud360.usuario.dtos.administradorDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorResumenDTO {
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
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private TipoDocumentoResumenDTO tipoDocumento;
    private UsuarioResumenDTO usuario;
}
