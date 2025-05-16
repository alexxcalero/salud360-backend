package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResumenDTO {
    private Integer idServicio;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Boolean activo;
    private List<String> imagenes;
}

