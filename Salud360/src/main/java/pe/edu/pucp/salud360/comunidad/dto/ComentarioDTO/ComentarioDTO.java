package pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {
    private Integer idComentario;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private Boolean activo;

    private Integer idUsuario;     // Persona que comenta
    private Integer idPublicacion; // A qué publicación pertenece
}


