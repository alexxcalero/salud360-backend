package pe.edu.pucp.salud360.control.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReporteLocalRequestDTO {
    private LocalDate fechaInicio;
    private String descripcion;
    private LocalDate fechaFin;
    private Integer idServicio;
}
