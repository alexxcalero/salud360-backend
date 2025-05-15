package pe.edu.pucp.salud360.comunidad.dto.ForoDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ForoDTO {
    private Integer idForo;
    private String titulo;
    private String descripcion;
    private Integer cantPublicaciones;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;

    private ComunidadResumenDTO comunidad; // vínculo hacia la comunidad que lo contiene
}

