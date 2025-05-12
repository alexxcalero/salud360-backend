package pe.edu.pucp.salud360.servicio.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoMedicoDTO {
    private Integer idDocumento;
    private String documento; // puede ser base64, URL o nombre
    private LocalDate fechaCreacion;
    private Integer idUsuarioCreador;
}

