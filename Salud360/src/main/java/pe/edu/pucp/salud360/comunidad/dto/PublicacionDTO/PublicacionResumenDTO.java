package pe.edu.pucp.salud360.comunidad.dto.PublicacionDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResumenDTO {
    private Integer idPublicacion;
    private String contenido;
    private LocalDateTime fechaCreacion;

    private UsuarioResumenDTO autor;
}

