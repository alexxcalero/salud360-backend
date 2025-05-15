package pe.edu.pucp.salud360.comunidad.dto.comunidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;

@Getter
@Setter
@SuperBuilder
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




}
