package pe.edu.pucp.salud360.usuario.dtos.usuarioDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.rolDTO.RolVistaClienteDTO;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioVistaClienteDTO {
    protected Integer idUsuario;
    protected String nombres;
    protected String apellidos;
    protected String numeroDocumento;
    protected String correo;
    protected String telefono;
    protected String sexo;
    protected LocalDate fechaNacimiento;
    protected String fotoPerfil;
    protected Boolean notiCorreo;
    protected Boolean notiSMS;
    protected Boolean notiWhatsApp;
    protected LocalDateTime fechaCreacion;
    protected TipoDocumentoResumenDTO tipoDocumento;
    protected RolVistaClienteDTO rol;
}
