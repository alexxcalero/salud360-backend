package pe.edu.pucp.salud360.comunidad.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioDTO;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioResumenDTO;
import pe.edu.pucp.salud360.comunidad.models.Testimonio;
import pe.edu.pucp.salud360.usuario.mappers.ClienteMapper;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, ComunidadMapper.class})
public interface TestimonioMapper {

    TestimonioDTO mapToDTO(Testimonio testimonio);

    Testimonio mapToModel(TestimonioDTO dto);

    TestimonioResumenDTO mapToResumenDTO(Testimonio testimonio);

    List<TestimonioDTO> mapToDTOList(List<Testimonio> testimonios);
    List<TestimonioResumenDTO> mapToResumenList(List<Testimonio> testimonios);
}
