package pe.edu.pucp.salud360.usuario.dtos.medicoDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaMedicoDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaAdminDTO;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MedicoVistaAdminDTO extends UsuarioVistaAdminDTO {
    private String especialidad;
    private String descripcion;
    private List<CitaMedicaVistaMedicoDTO> citasMedicas;
}
