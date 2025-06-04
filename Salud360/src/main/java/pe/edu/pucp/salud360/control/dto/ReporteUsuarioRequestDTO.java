package pe.edu.pucp.salud360.control.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReporteUsuarioRequestDTO {
    private LocalDate fechaInicio;
    private String descripcion;
    private LocalDate fechaFin;
    private Integer idServicio;
    private Integer idLocal;
}
