package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalResumenDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioVistaAdminDTO {
    private Integer idServicio;
    private String nombre;
    private String descripcion;
    //Ahora solo 1 imagen
    private String imagen;
    private String tipo;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;

    private List<ComunidadResumenDTO> comunidades;
    private List<LocalResumenDTO> locales;
    private List<CitaMedicaResumenDTO> citas;
}
