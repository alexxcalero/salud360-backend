package pe.edu.pucp.salud360.servicio.dto.LocalDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalResumenDTO {
    private Integer idLocal;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String tipoServicio;
    private Boolean activo;
}
