package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;

import lombok.*;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalResumenDTO;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResumenDTO {
    private Integer idServicio;
    private String nombre;
    private String tipo;
    private Boolean activo;
    private List<String> imagenes;
    private List<LocalResumenDTO> locales;
    private List<CitaMedicaResumenDTO> citas;
}
