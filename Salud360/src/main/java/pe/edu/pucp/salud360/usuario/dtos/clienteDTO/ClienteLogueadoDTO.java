package pe.edu.pucp.salud360.usuario.dtos.clienteDTO;

import lombok.*;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioDTO;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioResumenDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadVistaClienteDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaVistaClienteDTO;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.usuario.dtos.NotificacionDTO;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteLogueadoDTO {
    private Integer idCliente;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String sexo;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String fotoPerfil;
    private Boolean notificacionPorCorreo;
    private Boolean notificacionPorSMS;
    private Boolean notificacionPorWhatsApp;
    private TipoDocumentoResumenDTO tipoDocumento;
    private UsuarioResumenDTO usuario;
    private List<ComunidadVistaClienteDTO> comunidades;
    private List<AfiliacionDTO> afiliaciones;
    private List<ReservaDTO> reservas;
    private List<ClaseDTO> clases;
    private List<CitaMedicaVistaClienteDTO> citasMedicas;
    private List<NotificacionDTO> notificaciones;
    private List<MedioDePagoDTO> mediosDePago;
    private List<TestimonioResumenDTO> testimonios;
}
