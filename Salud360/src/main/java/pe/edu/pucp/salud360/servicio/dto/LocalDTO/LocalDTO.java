package pe.edu.pucp.salud360.servicio.dto.LocalDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LocalDTO {
    private Integer idLocal;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String telefono;
    private String nombresContacto;
    private String apellidosContacto;
    private String telefonoContacto;
    private List<String> imagenes;
    private String tipoServicio;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;

    private Integer idServicio; // Relación simplificada para registrar el local con su servicio
}
