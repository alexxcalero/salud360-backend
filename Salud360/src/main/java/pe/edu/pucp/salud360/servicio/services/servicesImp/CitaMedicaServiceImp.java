package pe.edu.pucp.salud360.servicio.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.mappers.CitaMedicaMapper;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.CitaMedicaRepository;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.usuario.models.Medico;
import pe.edu.pucp.salud360.usuario.models.Persona;
import pe.edu.pucp.salud360.usuario.repositories.MedicoRepository;
import pe.edu.pucp.salud360.usuario.repositories.PersonaRepository;
import pe.edu.pucp.salud360.servicio.services.CitaMedicaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaMedicaServiceImp implements CitaMedicaService {

    @Autowired
    private CitaMedicaMapper citaMedicaMapper;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private CitaMedicaRepository citaMedicaRepository;

    @Override
    public CitaMedicaDTO crearCitaMedica(CitaMedicaDTO dto) {
        // Buscar las entidades necesarias
        Servicio servicio = servicioRepository.findById(dto.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Persona paciente = personaRepository.findById(dto.getPaciente().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedico().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Mapear el DTO a entidad
        CitaMedica cita = citaMedicaMapper.mapToModel(dto);

        // Asignar relaciones no incluidas directamente en el mapeo
        cita.setServicio(servicio);
        cita.setPaciente(paciente); // O cita.setUsuario(paciente), si así se llama el campo
        cita.setMedico(medico);

        // Campos adicionales
        cita.setFechaCreacion(LocalDateTime.now());
        cita.setActivo(true);

        // Guardar y devolver DTO
        return citaMedicaMapper.mapToDTO(citaMedicaRepository.save(cita));
    }


    @Override
    public CitaMedicaDTO actualizarCitaMedica(Integer id, CitaMedicaDTO dto) {
        Optional<CitaMedica> optional = citaMedicaRepository.findById(id);
        if (optional.isEmpty()) return null;

        CitaMedica cita = optional.get();

        // Solo se actualizan los campos editables (activo no se toca)
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora()); // asumimos que cambió de `horaInicio` a `hora` en el DTO
        cita.setEstado(dto.getEstado());
        cita.setMotivo(dto.getMotivo());

        return citaMedicaMapper.mapToDTO(citaMedicaRepository.save(cita));
    }


    @Override
    public void eliminarCitaMedica(Integer id) {
        Optional<CitaMedica> optional = citaMedicaRepository.findById(id);
        if (optional.isPresent()) {
            CitaMedica cita = optional.get();
            cita.setActivo(false);
            cita.setFechaDesactivacion(LocalDateTime.now()); // o LocalDateTime.now() si usas datetime
            citaMedicaRepository.save(cita);
        }
    }


    @Override
    public List<CitaMedicaDTO> listarCitasMedicasTodas() {
        return citaMedicaRepository.findAll().stream()
                .filter(CitaMedica::getActivo)
                .map(citaMedicaMapper::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public CitaMedicaDTO buscarCitaMedicaPorId(Integer id) {
        return citaMedicaRepository.findById(id)
                .filter(CitaMedica::getActivo) // opcional, si quieres ignorar inactivos
                .map(citaMedicaMapper::mapToDTO)
                .orElse(null);
    }

}

