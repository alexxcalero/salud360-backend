package pe.edu.pucp.salud360.comunidad.dto.comunidad;

import lombok.*;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaClienteDTO;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComunidadVistaClienteDTO {
    private Integer idComunidad;
    private String nombre;
    private String descripcion;
    private String proposito;
    private List<String> imagenes;
    private Integer cantMiembros;
    private Double calificacion;
    private List<ServicioVistaClienteDTO> servicios; //agregado
    private List<MembresiaResumenDTO> membresias;
}
