package pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TestimonioDTO {
    private Integer idTestimonio;
    private String comentario;
    private Double calificacion;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;

    private Integer idComunidad;
    private UsuarioResumenDTO autor;
}

