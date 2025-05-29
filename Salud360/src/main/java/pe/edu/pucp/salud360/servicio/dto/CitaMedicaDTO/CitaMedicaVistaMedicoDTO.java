package pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.servicio.models.DocumentoMedico;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.models.Medico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CitaMedicaVistaMedicoDTO {
    private Integer idCitaMedica;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private Servicio servicio;
    private List<DocumentoMedico> documentosMedicos;
    private ClienteResumenDTO cliente;
}
