package pe.edu.pucp.salud360.membresia.dtos.membresia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MembresiaResumenDTO {
    private Integer idMembresia;
    private String nombre;
    private String descripcion;
    private String tipo;
    private Boolean conTope;
    private Double precio;
    private Integer cantUsuarios;
    private Integer maxReservas;
    private String icono;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
}
