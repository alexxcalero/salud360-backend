package pe.edu.pucp.salud360.servicio.dto.LocalDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LocalResumenDTO {
    private Integer idLocal;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String tipoServicio;
    private List<String> imagenes;
    private Boolean activo;

}

