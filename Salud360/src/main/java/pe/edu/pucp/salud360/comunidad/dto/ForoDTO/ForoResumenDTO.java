package pe.edu.pucp.salud360.comunidad.dto.ForoDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ForoResumenDTO {
    private Integer idForo;
    private String titulo;
    private Integer cantPublicaciones;
    private Boolean activo;
}

