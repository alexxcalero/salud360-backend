package pe.edu.pucp.salud360.usuario.dtos.clienteDTO;

import lombok.*;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRegistroDTO {
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String correo;
    private String contrasenha;
    private String sexo;
    //Imagen
    private String fotoPerfil;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String direccion;
    private TipoDocumentoResumenDTO tipoDocumento;
}
