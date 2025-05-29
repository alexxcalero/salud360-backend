package pe.edu.pucp.salud360.servicio.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoMedicoDTO {
    private Integer idDocumento;
    private String documento; // puede ser base64, URL o nombre
    private LocalDate fechaCreacion;
    private Integer idUsuarioCreador;
    private LocalDateTime fechaDesactivacion;
}

