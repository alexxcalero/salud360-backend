package pe.edu.pucp.salud360.comunidad.mappers;

import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Foro;
//NUEVO
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ComunidadMapper {
    public static ComunidadDTO mapToDTO(Comunidad comunidad) {
        if (comunidad == null) return null;

        List<ServicioDTO> serviciosDTO = comunidad.getServicios() != null
            ? comunidad.getServicios().stream()
                .map(s -> new ServicioDTO(
                    s.getIdServicio(),
                    s.getNombre(),
                    s.getDescripcion(),
                    null,
                    null
                ))
                .collect(Collectors.toList())
            : new ArrayList<>();

        return new ComunidadDTO(
                comunidad.getIdComunidad(),
                comunidad.getNombre(),
                comunidad.getDescripcion(),
                comunidad.getProposito(),
                comunidad.getImagenes(),
                comunidad.getActivo(),
                comunidad.getFechaCreacion(),
                comunidad.getFechaDesactivacion(),
                comunidad.getForo() != null ? comunidad.getForo().getIdForo() : null,
                serviciosDTO  // NUEVO
        );
    }

    public static Comunidad mapToModel(ComunidadDTO dto, Foro foro) {
        Comunidad comunidad = new Comunidad();
        comunidad.setIdComunidad(dto.getIdComunidad());
        comunidad.setNombre(dto.getNombre());
        comunidad.setDescripcion(dto.getDescripcion());
        comunidad.setProposito(dto.getProposito());
        comunidad.setImagenes(dto.getImagen());
        comunidad.setActivo(dto.getActivo());
        comunidad.setFechaCreacion(dto.getFechaCreacion());
        comunidad.setFechaDesactivacion(dto.getFechaDesactivacion());
        comunidad.setForo(foro);
        // Por ahora no seteamos servicios al model desde el DTO
        return comunidad;
    }
}