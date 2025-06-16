package pe.edu.pucp.salud360.comunidad.services.servicesImp;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.dto.TestimonioDTO.TestimonioDTO;
import pe.edu.pucp.salud360.comunidad.mappers.TestimonioMapper;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Testimonio;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.repositories.TestimonioRepository;
import pe.edu.pucp.salud360.comunidad.services.TestimonioService;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestimonioServiceImp implements TestimonioService {

    @Autowired
    private TestimonioRepository testimonioRepository;

    @Autowired
    private TestimonioMapper testimonioMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Override
    public TestimonioDTO crearTestimonio(TestimonioDTO dto) {
        Testimonio testimonio = testimonioMapper.mapToModel(dto);

        Cliente autor = clienteRepository.findById(dto.getAutor().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comunidad comunidad = comunidadRepository.findById(dto.getIdComunidad())
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        testimonio.setCliente(autor);
        testimonio.setComunidad(comunidad);
        testimonio.setFechaCreacion(LocalDateTime.now());
        testimonio.setActivo(true);

        return testimonioMapper.mapToDTO(testimonioRepository.save(testimonio));
    }

    @Override
    public TestimonioDTO actualizarTestimonio(Integer id, TestimonioDTO dto) {
        Optional<Testimonio> optional = testimonioRepository.findById(id);
        if (optional.isEmpty()) return null;

        Testimonio testimonio = optional.get();
        testimonio.setComentario(dto.getComentario());
        testimonio.setCalificacion(dto.getCalificacion());

        return testimonioMapper.mapToDTO(testimonioRepository.save(testimonio));
    }

    @Override
    public boolean eliminarTestimonio(Integer id) {
        Optional<Testimonio> optional = testimonioRepository.findById(id);
        if (optional.isEmpty()) return false;

        Testimonio testimonio = optional.get();
        testimonio.setActivo(false);
        testimonio.setFechaDesactivacion(LocalDateTime.now());
        testimonioRepository.save(testimonio);
        return true;
    }

    @Override
    public List<TestimonioDTO> listarTestimonios() {
        return testimonioRepository.findAll().stream()
                .filter(Testimonio::getActivo)
                .map(testimonioMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TestimonioDTO obtenerTestimonioPorId(Integer id) {
        return testimonioRepository.findById(id)
                .filter(Testimonio::getActivo)
                .map(testimonioMapper::mapToDTO)
                .orElse(null);
    }

    @Override
    public void reactivarTestimonio(Integer idTestimonio) {
        Testimonio testimonio = testimonioRepository.findById(idTestimonio)
                .orElseThrow(() -> new EntityNotFoundException("Testimonio no encontrado"));

        testimonio.setActivo(true);
        testimonioRepository.save(testimonio);
    }

}
