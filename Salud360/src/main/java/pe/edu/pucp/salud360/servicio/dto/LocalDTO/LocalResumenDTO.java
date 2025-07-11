package pe.edu.pucp.salud360.servicio.dto.LocalDTO;

import lombok.*;

import java.util.List;

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
    private String imagen;  // Ahora solo 1 imagen
    private Boolean activo;
}
