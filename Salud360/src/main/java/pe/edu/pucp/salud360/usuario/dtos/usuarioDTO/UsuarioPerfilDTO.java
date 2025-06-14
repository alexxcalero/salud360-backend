package pe.edu.pucp.salud360.usuario.dtos.usuarioDTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String sexo;
    private LocalDate fechaNacimiento;
    private String numeroDocumento;
    private Integer idTipoDocumento;
}

