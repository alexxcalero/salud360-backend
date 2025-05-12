package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResumenDTO {
    private Integer idServicio;
    private String nombre;
    private String tipo;
    private Boolean activo;
}

