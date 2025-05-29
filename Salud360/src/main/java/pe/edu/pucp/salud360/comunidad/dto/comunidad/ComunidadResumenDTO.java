package pe.edu.pucp.salud360.comunidad.dto.comunidad;

import lombok.*;

import java.util.List;

import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComunidadResumenDTO {
    private Integer idComunidad;
    private String nombre;
    private String descripcion;
    private String proposito;
    private List<String> imagenes;
    private Integer cantMiembros;
    private Double calificacion;
    private List<ServicioResumenDTO> servicios; //agregado
    private List<MembresiaResumenDTO> membresias;
}
