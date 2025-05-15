package pe.edu.pucp.salud360.comunidad.dto.comunidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ComunidadDTO {
    private Integer idComunidad;
    private String nombre;
    private String descripcion;
    private String proposito;
    private List<String> imagenes;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private Integer idForo;
    private Integer cantMiembros;
    private Double calificacion;


    private List<ServicioResumenDTO> servicios; //agregado
    private List<MembresiaResumenDTO> membresias;
}

