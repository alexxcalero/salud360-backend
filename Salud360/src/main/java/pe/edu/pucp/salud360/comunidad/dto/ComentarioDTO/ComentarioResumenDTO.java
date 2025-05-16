package pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioResumenDTO {
    private Integer idComentario;
    private String contenido;
    private LocalDateTime fechaCreacion;

    private String nombreUsuario;     // nombre o alias del autor
    private String fotoPerfilUsuario; // opcional para frontend
}

