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
public class PublicacionDTO {
    private Integer idPublicacion;
    private String contenido;
    private Integer cantComentarios;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;

    private UsuarioResumenDTO autor;   // persona
    private Integer idForo;            // relación por ID
}

