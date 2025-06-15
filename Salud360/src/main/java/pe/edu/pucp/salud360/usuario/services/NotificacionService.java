package pe.edu.pucp.salud360.usuario.services;

import pe.edu.pucp.salud360.usuario.dtos.NotificacionDTO;

import java.util.List;
import java.util.Map;

public interface NotificacionService {
    NotificacionDTO crearNotificacion(NotificacionDTO notificacionDTO, String asunto);
    NotificacionDTO actualizarNotificacion(Integer idNotificacion, NotificacionDTO notificacionDTO);
    void eliminarNotificacion(Integer idNotificacion);
    List<NotificacionDTO> listarNotificacionesTodas();
    NotificacionDTO buscarNotificacionPorId(Integer idNotificacion);
}
