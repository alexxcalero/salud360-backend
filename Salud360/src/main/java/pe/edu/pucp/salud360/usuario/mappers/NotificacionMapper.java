package pe.edu.pucp.salud360.usuario.mappers;

import org.mapstruct.Mapper;
import pe.edu.pucp.salud360.usuario.dtos.NotificacionDTO;
import pe.edu.pucp.salud360.usuario.models.Notificacion;

@Mapper(componentModel = "spring")
public interface NotificacionMapper {

    NotificacionDTO mapToDTO(Notificacion notificacion);
    Notificacion mapToModel(NotificacionDTO notificacionDTO);

}
