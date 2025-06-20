package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Integer idServicio;
    private String nombre;
    private String descripcion;
    //Ahora solo 1 imagen
    private String imagen;
    //private List<LocalResumenDTO> locales;
    private String tipo;
}
