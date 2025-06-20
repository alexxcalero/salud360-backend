package pe.edu.pucp.salud360.comunidad.dto.comunidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
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
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private Integer cantMiembros;
    private Double calificacion;
    //Ahora solo 1 imagen
    private String imagen;


    private List<ServicioResumenDTO> servicios; //agregado
    private List<MembresiaResumenDTO> membresias;
    private List<TestimonioDTO> testimonios;
}

