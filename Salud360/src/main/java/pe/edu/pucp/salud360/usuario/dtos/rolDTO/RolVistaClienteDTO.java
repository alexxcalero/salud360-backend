package pe.edu.pucp.salud360.usuario.dtos.rolDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolVistaClienteDTO {
    private Integer idRol;
    private String nombre;
    private String descripcion;
    //private List<PermisoResumenDTO> permisos;
}
