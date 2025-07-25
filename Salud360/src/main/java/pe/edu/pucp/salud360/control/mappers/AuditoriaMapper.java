package pe.edu.pucp.salud360.control.mappers;

import pe.edu.pucp.salud360.control.dto.AuditoriaDTO;
import pe.edu.pucp.salud360.control.models.Auditoria;

public class AuditoriaMapper {

    public static AuditoriaDTO mapToDTO(Auditoria a) {
        if (a == null) return null;
        return new AuditoriaDTO(
                a.getIdAuditoria(),
                a.getNombreTabla(),
                a.getFechaModificacion(),
                a.getNombreUsuarioModificador(),
                a.getDescripcion(),
                a.getOperacion()
        );
    }

    public static Auditoria mapToModel(AuditoriaDTO dto) {
        if (dto == null) return null;
        Auditoria a = new Auditoria();
        a.setIdAuditoria(dto.getIdAuditoria());
        a.setNombreTabla(dto.getNombreTabla());
        a.setFechaModificacion(dto.getFechaModificacion());
        a.setNombreUsuarioModificador(dto.getNombreUsuarioModificador());
        a.setDescripcion(dto.getDescripcion());
        a.setOperacion(dto.getOperacion());
        return a;
    }
}
