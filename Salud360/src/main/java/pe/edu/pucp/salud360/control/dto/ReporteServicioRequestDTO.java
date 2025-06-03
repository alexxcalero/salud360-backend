package pe.edu.pucp.salud360.control.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReporteServicioRequestDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer idLocal;
}

