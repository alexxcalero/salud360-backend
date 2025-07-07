package pe.edu.pucp.salud360.usuario.services.servicesImp;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.mappers.MedicoMapper;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.models.Medico;
import pe.edu.pucp.salud360.usuario.models.TipoDocumento;
import pe.edu.pucp.salud360.usuario.repositories.MedicoRepository;
import pe.edu.pucp.salud360.usuario.repositories.TipoDocumentoRepository;
import pe.edu.pucp.salud360.usuario.services.MedicoService;
import pe.edu.pucp.salud360.usuario.services.TipoDocumentoService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoServiceImp implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    public MedicoVistaAdminDTO crearMedico(MedicoRegistroDTO medicoDTO) {
        Medico medico = medicoMapper.mapToModel(medicoDTO);
        medico.setActivo(true);
        medico.setFechaCreacion(LocalDateTime.now());
        medico.setCitasMedicas(new ArrayList<>());
        Medico medicoCreado = medicoRepository.save(medico);
        return medicoMapper.mapToVistaAdminDTO(medicoCreado);
    }

    @Override
    public MedicoVistaAdminDTO actualizarMedico(Integer idMedico, MedicoVistaAdminDTO medicoDTO) {
        Optional<Medico> medicoSeleccionado = medicoRepository.findById(idMedico);
        if(medicoSeleccionado.isPresent()){
            Medico medico = medicoSeleccionado.get();
            medico.setNombres(medicoDTO.getNombres());
            medico.setApellidos(medicoDTO.getApellidos());
            medico.setNumeroDocumento(medicoDTO.getNumeroDocumento());
            medico.setTipoDocumento(tipoDocumentoMapper.mapToModel(medicoDTO.getTipoDocumento()));
            medico.setSexo(medicoDTO.getSexo());
            medico.setEspecialidad(medicoDTO.getEspecialidad());
            medico.setDescripcion(medicoDTO.getDescripcion());
            medico.setFotoPerfil(medicoDTO.getFotoPerfil());
            Medico medicoActualizado = medicoRepository.save(medico);
            return medicoMapper.mapToVistaAdminDTO(medicoActualizado);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarMedico(Integer idMedico) {
        Optional<Medico> medicoSeleccionado = medicoRepository.findById(idMedico);
        if(medicoSeleccionado.isPresent()) {
            Medico medicoEliminar = medicoSeleccionado.get();
            medicoEliminar.setActivo(false);
            medicoEliminar.setFechaDesactivacion(LocalDateTime.now());
            medicoRepository.save(medicoEliminar);
        }
    }

    @Override
    public void reactivarMedico(Integer idMedico) {
        Optional<Medico> medicoSeleccionado = medicoRepository.findById(idMedico);
        if(medicoSeleccionado.isPresent()) {
            Medico medicoReactivar = medicoSeleccionado.get();
            medicoReactivar.setActivo(true);
            medicoReactivar.setFechaDesactivacion(null);
            medicoRepository.save(medicoReactivar);
        }
    }

    @Override
    public List<MedicoVistaAdminDTO> listarMedicos(String nombreCompleto, String especialidad) {
        List<Medico> medicos;

        boolean filtroNombre = nombreCompleto != null && !nombreCompleto.isBlank();
        boolean filtroEspecialidad = especialidad != null && !especialidad.isBlank();

        if(filtroNombre && filtroEspecialidad) {
            medicos = medicoRepository.buscarPorNombreYEspecialidad(nombreCompleto, especialidad);
        } else if(filtroNombre) {
            medicos = medicoRepository.buscarPorNombreCompleto(nombreCompleto);
        } else if(filtroEspecialidad) {
            medicos = medicoRepository.findAllByEspecialidadContainingIgnoreCaseOrderByIdMedicoAsc(especialidad);
        } else {
            medicos = medicoRepository.findAllByOrderByIdMedicoAsc();
        }

        if(!(medicos.isEmpty())) {
            return medicos.stream().map(medicoMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public MedicoVistaAdminDTO buscarMedicoPorId(Integer idMedico) {
        Optional<Medico> medicoBuscado = medicoRepository.findById(idMedico);
        if(medicoBuscado.isPresent()) {
            Medico medico = medicoBuscado.get();
            return medicoMapper.mapToVistaAdminDTO(medico);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean cargarMasivamanteMedico(MultipartFile file) throws IOException {
        List<Medico> listaMedicos = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setDelimiter(',');
        settings.setIgnoreLeadingWhitespaces(true);
        settings.setIgnoreTrailingWhitespaces(true);

        CsvParser parser = new CsvParser(settings);
        List<Record> parseAllRecords = parser.parseAllRecords(inputStream);

        parseAllRecords.forEach(record -> {
            Medico medico = new Medico();

            medico.setNombres(record.getString("nombres"));
            medico.setApellidos(record.getString("apellidos"));
            medico.setNumeroDocumento(record.getString("numeroDocumento"));
            medico.setSexo(record.getString("sexo"));
            medico.setEspecialidad(record.getString("especialidad"));
            medico.setDescripcion(record.getString("descripcion"));
            medico.setFotoPerfil(record.getString("fotoPerfil"));
            medico.setActivo(Boolean.parseBoolean(record.getString("activo")));
            medico.setFechaCreacion(LocalDateTime.now());

            String fechaDesact = record.getString("fechaDesactivacion");
            if (fechaDesact != null && !fechaDesact.isEmpty()) {
                medico.setFechaDesactivacion(LocalDateTime.parse(fechaDesact));
            }

            // Validación de duplicidad dentro del mismo CSV
            for (Medico otro : listaMedicos) {
                if (medico.getNombres().equalsIgnoreCase(otro.getNombres()) &&
                        medico.getNumeroDocumento().equals(otro.getNumeroDocumento())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Médico duplicado en el archivo CSV con nombre " + medico.getNombres() +
                                    " y documento " + medico.getNumeroDocumento());
                }
            }

            // Validación contra la BD
            List<Medico> medicosExistentes = medicoRepository.findByNombresAndNumeroDocumento(
                    medico.getNombres(), medico.getNumeroDocumento()
            );

            for (Medico existente : medicosExistentes) {
                if (medico.getNombres().equalsIgnoreCase(existente.getNombres()) &&
                        medico.getNumeroDocumento().equals(existente.getNumeroDocumento())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El médico '" + medico.getNombres() +
                                    "' con documento " + medico.getNumeroDocumento() +
                                    " ya se encuentra registrado en la base de datos");
                }
            }

            // Asociar TipoDocumento
            Integer idTipoDocumento = Integer.parseInt(record.getString("idTipoDocumento"));
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "TipoDocumento con ID " + idTipoDocumento + " no encontrado"));
            medico.setTipoDocumento(tipoDocumento);

            listaMedicos.add(medico);
        });

        medicoRepository.saveAll(listaMedicos);
        return true;
    }

}
