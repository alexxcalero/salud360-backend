package pe.edu.pucp.salud360.servicio.dto.ServicioDTO;



import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioVistaAdminDTO {
    private Integer idServicio;
    private String nombre;
    private String descripcion;
    private List<String> imagenes;
    private String tipo;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
}
