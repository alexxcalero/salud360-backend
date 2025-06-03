package pe.edu.pucp.salud360.servicio.dto;

import lombok.*;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoMedicoDTO {
    private Integer idDocumentoMedico;
    private String documento;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private CitaMedicaResumenDTO citaMedica;
}
