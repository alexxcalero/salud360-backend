package pe.edu.pucp.salud360.servicio.dto.ClaseDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ClaseDTO {
    private Integer idClase;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer capacidad;
    private Integer cantAsistentes;
    private String estado;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDesactivacion;
    private List<ClienteResumenDTO> clientes;
    private List<ReservaResumenDTO> reservas;
    private LocalResumenDTO local;
}
