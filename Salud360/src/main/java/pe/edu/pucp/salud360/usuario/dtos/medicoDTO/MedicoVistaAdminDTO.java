package pe.edu.pucp.salud360.usuario.dtos.medicoDTO;

import lombok.*;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaMedicoDTO;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicoVistaAdminDTO {
    private Integer idMedico;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String sexo;
    private String especialidad;
    private String descripcion;
    //Ahora solo 1 imagen
    private String fotoPerfil;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private TipoDocumentoResumenDTO tipoDocumento;
    //Fabián: private List<CitaMedicaVistaMedicoDTO> citasMedicas;
    private List<CitaMedicaResumenDTO> citasMedicas;
}
