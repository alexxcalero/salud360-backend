package pe.edu.pucp.salud360.usuario.dtos.personaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.dto.ComentarioDTO;
import pe.edu.pucp.salud360.comunidad.dto.PublicacionDTO;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ClaseDTO.ClaseResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.models.Notificacion;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersonaVistaAdminDTO extends UsuarioVistaAdminDTO {
    private String direccion;
    private List<ComunidadResumenDTO> comunidades;
    private List<ClaseResumenDTO> clases;
    private List<AfiliacionResumenDTO> afiliaciones;
    private List<MedioDePagoResumenDTO> mediosDePago;
    private List<PublicacionDTO> publicaciones;
    private List<ComentarioDTO> comentarios;
    private List<TestimonioDTO> testimonios;
    private List<Notificacion> notificaciones;
    private List<ReservaResumenDTO> reservas;
    private List<CitaMedicaResumenDTO> citasMedicas;
}
