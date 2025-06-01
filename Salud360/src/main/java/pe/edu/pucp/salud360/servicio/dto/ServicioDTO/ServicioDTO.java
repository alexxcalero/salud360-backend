package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalResumenDTO;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.models.Local;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import java.util.List;

@Getter
@Setter
@SuperBuilder
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