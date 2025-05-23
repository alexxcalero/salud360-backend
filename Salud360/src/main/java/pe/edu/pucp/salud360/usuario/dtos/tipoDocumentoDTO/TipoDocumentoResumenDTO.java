package pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipoDocumentoResumenDTO {
    private Integer idTipoDocumento;
    private String nombre;
}
