package pe.edu.pucp.salud360.comunidad.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioDTO;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioResumenDTO;
import pe.edu.pucp.salud360.comunidad.models.Testimonio;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface TestimonioMapper {

    @Mapping(source = "cliente", target = "autor")
    @Mapping(source = "comunidad.idComunidad", target = "idComunidad")
    TestimonioDTO mapToDTO(Testimonio testimonio);

    @Mapping(target = "cliente", source = "autor")
    @Mapping(target = "comunidad.idComunidad", source = "idComunidad")
    Testimonio mapToModel(TestimonioDTO dto);

    @Mapping(source = "cliente", target = "autor")
    TestimonioResumenDTO mapToResumenDTO(Testimonio testimonio);

    List<TestimonioDTO> mapToDTOList(List<Testimonio> testimonios);
    List<TestimonioResumenDTO> mapToResumenList(List<Testimonio> testimonios);
}

