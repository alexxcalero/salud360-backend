package pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioVistaAdminDTO {
    private Integer idComentario;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private Boolean activo;

    private UsuarioResumenDTO usuario;
}
