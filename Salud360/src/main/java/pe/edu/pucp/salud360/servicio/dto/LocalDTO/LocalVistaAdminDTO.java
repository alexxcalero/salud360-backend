package pe.edu.pucp.salud360.servicio.dto.LocalDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LocalVistaAdminDTO {
    private Integer idLocal;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String telefono;
    private Integer aforo;
    private Integer ocupados;
    //Ahora solo 1 imagen
    private String imagen;
    private String tipoServicio;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;

    private ServicioResumenDTO servicio;    // 🧠 Para mostrar nombre y tipo del servicio
}
