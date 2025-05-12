package pe.edu.pucp.salud360.servicio.dto.LocalDTO;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LocalResumenDTO {
    private Integer idLocal;
    private String nombre;
    private String direccion;
    private String tipoServicio;
    private Boolean activo;
}

