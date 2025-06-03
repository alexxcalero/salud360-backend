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
    private List<String> imagenes;
    //private List<LocalResumenDTO> locales;
    private String tipo;
}
