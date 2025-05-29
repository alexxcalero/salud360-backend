package pe.edu.pucp.salud360.usuario.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.pucp.salud360.usuario.dtos.NotificacionDTO;
import pe.edu.pucp.salud360.usuario.mappers.NotificacionMapper;
import pe.edu.pucp.salud360.usuario.models.Notificacion;
import pe.edu.pucp.salud360.usuario.repositories.NotificacionRepository;
import pe.edu.pucp.salud360.usuario.services.NotificacionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImp implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;

    @Override
    public NotificacionDTO crearNotificacion(NotificacionDTO notificacionDTO) {
        Notificacion notificacion = notificacionMapper.mapToModel(notificacionDTO);
        Notificacion notificacionCreada = notificacionRepository.save(notificacion);
        return notificacionMapper.mapToDTO(notificacionCreada);
    }

    @Override
    public NotificacionDTO actualizarNotificacion(Integer idNotificacion, NotificacionDTO notificacionDTO) {
        if(notificacionRepository.findById(idNotificacion).isPresent()){
            Notificacion notificacion = notificacionRepository.findById(idNotificacion).get();
            notificacion.setMensaje(notificacionDTO.getMensaje());
            Notificacion notificacionActualizada = notificacionRepository.save(notificacion);
            return notificacionMapper.mapToDTO(notificacionActualizada);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarNotificacion(Integer idNotificacion) {
        if(notificacionRepository.findById(idNotificacion).isPresent()) {
            notificacionRepository.deleteById(idNotificacion);
        }
    }

    @Override
    public List<NotificacionDTO> listarNotificacionesTodas() {
        List<Notificacion> notificaciones = notificacionRepository.findAll();
        if(notificaciones.isEmpty()) {
            return null;
        } else {
            return notificaciones.stream().map(notificacionMapper::mapToDTO).toList();
        }
    }

    @Override
    public NotificacionDTO buscarNotificacionPorId(Integer idNotificacion) {
        if(notificacionRepository.findById(idNotificacion).isPresent()) {
            Notificacion notificacion = notificacionRepository.findById(idNotificacion).get();
            return notificacionMapper.mapToDTO(notificacion);
        } else {
            return null;
        }
    }
}
