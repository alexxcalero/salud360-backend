package pe.edu.pucp.salud360.usuario.dtos.medicoDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicoResumenDTO {
    private Integer idMedico;
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String descripcion;
    //Ahora solo 1 imagen
    private String fotoPerfil;
}
