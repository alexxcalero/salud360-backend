package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;

import lombok.*;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaClienteDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalResumenDTO;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioVistaClienteDTO {
    private Integer idServicio;
    private String nombre;
    private String descripcion;
    private String tipo;
    private Boolean activo;
    //Ahora solo 1 imagen
    private String imagen;
    private List<LocalResumenDTO> locales;
    private List<CitaMedicaVistaClienteDTO> citas;
}
