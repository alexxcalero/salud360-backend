package pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TestimonioResumenDTO {
    private Integer idTestimonio;
    private String comentario;
    private Double calificacion;
    private ClienteResumenDTO cliente;
}
