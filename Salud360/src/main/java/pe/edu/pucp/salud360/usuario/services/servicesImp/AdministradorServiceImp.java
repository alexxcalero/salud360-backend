package pe.edu.pucp.salud360.usuario.services.servicesImp;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorResumenDTO;
import pe.edu.pucp.salud360.usuario.mappers.AdministradorMapper;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.models.Administrador;
import pe.edu.pucp.salud360.usuario.models.Rol;
import pe.edu.pucp.salud360.usuario.models.TipoDocumento;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.AdministradorRepository;
import pe.edu.pucp.salud360.usuario.repositories.RolRepository;
import pe.edu.pucp.salud360.usuario.repositories.TipoDocumentoRepository;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;
import pe.edu.pucp.salud360.usuario.services.AdministradorService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImp implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final AdministradorMapper administradorMapper;
    private final UsuarioRepository usuarioRepository;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    @Transactional
    public AdministradorResumenDTO crearAdministrador(AdministradorRegistroDTO administradorDTO) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(administradorDTO.getCorreo());
        usuario.setContrasenha(passwordEncoder.encode(administradorDTO.getContrasenha()));
        usuario.setRol(rolRepository.findById(1).orElseThrow(() -> new RuntimeException("No existe el rol")));
        usuario.setActivo(true);

        Administrador administrador = administradorMapper.mapToModel(administradorDTO);
        administrador.setNotificacionPorCorreo(false);
        administrador.setNotificacionPorSMS(false);
        administrador.setNotificacionPorWhatsApp(false);
        administrador.setActivo(true);
        administrador.setFechaCreacion(LocalDateTime.now());

        usuario.setAdministrador(administrador);
        administrador.setUsuario(usuario);

        Usuario usuarioCreado = usuarioRepository.save(usuario);
        Administrador administradorCreado = administradorRepository.save(administrador);

        return administradorMapper.mapToResumenDTO(administradorCreado);
    }

    @Override
    public AdministradorLogueadoDTO actualizarAdministrador(Integer idAdministrador, AdministradorLogueadoDTO administradorDTO) {
        Optional<Administrador> administradorSeleccionado = administradorRepository.findById(idAdministrador);
        if(administradorSeleccionado.isPresent()) {
            Administrador administrador = administradorSeleccionado.get();
            administrador.setNombres(administradorDTO.getNombres());
            administrador.setApellidos(administradorDTO.getApellidos());
            administrador.setNumeroDocumento(administradorDTO.getNumeroDocumento());
            administrador.setSexo(administradorDTO.getSexo());
            administrador.setTelefono(administradorDTO.getTelefono());
            administrador.setFotoPerfil(administradorDTO.getFotoPerfil());
            administrador.setNotificacionPorCorreo(administradorDTO.getNotificacionPorCorreo());
            administrador.setNotificacionPorSMS(administradorDTO.getNotificacionPorSMS());
            administrador.setNotificacionPorWhatsApp(administradorDTO.getNotificacionPorWhatsApp());
            administrador.setTipoDocumento(tipoDocumentoMapper.mapToModel(administradorDTO.getTipoDocumento()));

            administrador.getUsuario().setCorreo(administradorDTO.getCorreo());

            Administrador administradorActualizado = administradorRepository.save(administrador);
            return administradorMapper.mapToLogueadoDTO(administradorActualizado);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarAdministrador(Integer idAdministrador) {
        Optional<Administrador> administradorSeleccionado = administradorRepository.findById(idAdministrador);
        if(administradorSeleccionado.isPresent()) {
            Administrador administradorEliminar = administradorSeleccionado.get();
            administradorEliminar.setActivo(false);
            administradorEliminar.setFechaDesactivacion(LocalDateTime.now());
            administradorRepository.save(administradorEliminar);
        }
    }

    @Override
    public void reactivarAdministrador(Integer idAdministrador) {
        Optional<Administrador> administradorSeleccionado = administradorRepository.findById(idAdministrador);
        if(administradorSeleccionado.isPresent()) {
            Administrador administradorReactivar = administradorSeleccionado.get();
            administradorReactivar.setActivo(true);
            administradorReactivar.setFechaDesactivacion(null);
            administradorRepository.save(administradorReactivar);
        }
    }

    @Override
    public List<AdministradorResumenDTO> listarAdministradores() {
        List<Administrador> administradores = administradorRepository.findAll();
        if(!(administradores.isEmpty())) {
            return administradores.stream().map(administradorMapper::mapToResumenDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public AdministradorResumenDTO buscarAdministradorPorId(Integer idAdministrador) {
        Optional<Administrador> administradorBuscado = administradorRepository.findById(idAdministrador);
        if(administradorBuscado.isPresent()) {
            Administrador administrador = administradorBuscado.get();
            return administradorMapper.mapToResumenDTO(administrador);
        } else {
            return null;
        }
    }

    @Override
    public AdministradorLogueadoDTO buscarAdminPorId(Integer idAdministrador) {
        Optional<Administrador> administradorBuscado = administradorRepository.findById(idAdministrador);
        if(administradorBuscado.isPresent()) {
            Administrador administrador = administradorBuscado.get();
            return administradorMapper.mapToLogueadoDTO(administrador);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean cargarMasivamanteAdmin(MultipartFile file) throws IOException {
        List<Administrador> listaAdministradores = new ArrayList<>();
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
            Administrador admin = new Administrador();

            admin.setNombres(record.getString("nombres"));
            admin.setApellidos(record.getString("apellidos"));
            admin.setNumeroDocumento(record.getString("numeroDocumento"));
            admin.setSexo(record.getString("sexo"));
            admin.setTelefono(record.getString("telefono"));
            admin.setFotoPerfil(record.getString("fotoPerfil"));
            admin.setNotificacionPorCorreo(Boolean.parseBoolean(record.getString("notificacionPorCorreo")));
            admin.setNotificacionPorSMS(Boolean.parseBoolean(record.getString("notificacionPorSMS")));
            admin.setNotificacionPorWhatsApp(Boolean.parseBoolean(record.getString("notificacionPorWhatsApp")));
            admin.setActivo(Boolean.parseBoolean(record.getString("activo")));

            // Fecha actual como creación
            admin.setFechaCreacion(LocalDateTime.now());

            // Puede venir o no
            String fechaDesact = record.getString("fechaDesactivacion");
            if (fechaDesact != null && !fechaDesact.isEmpty()) {
                admin.setFechaDesactivacion(LocalDateTime.parse(fechaDesact));
            }

            // Asociar Usuario
            String idUsuarioStr = record.getString("idUsuario");
            Usuario usuario;

            if (idUsuarioStr != null && !idUsuarioStr.isBlank()) {
                Integer idUsuario = Integer.parseInt(idUsuarioStr);
                usuario = usuarioRepository.findById(idUsuario)
                        .orElseThrow(() -> new RuntimeException("Usuario con ID " + idUsuario + " no encontrado"));
            } else {
                // Generación automática
                String nombres = record.getString("nombres").trim();
                String apellidos = record.getString("apellidos").trim();
                String correo = (nombres + "." + apellidos + "@salud360.com")
                        .toLowerCase()
                        .replaceAll("\\s+", "")
                        .replaceAll("[^a-z0-9.@]", "");

                String contrasenha = passwordEncoder.encode(apellidos + "123");

                Rol rolAdmin = rolRepository.findByNombre("Admin")
                        .orElseThrow(() -> new RuntimeException("Rol 'Admin' no encontrado"));

                usuario = new Usuario();
                usuario.setCorreo(correo);
                usuario.setContrasenha(contrasenha);
                usuario.setRol(rolAdmin);
                usuario.setActivo(true);

                usuarioRepository.save(usuario);
            }

            admin.setUsuario(usuario);


            // Asociar TipoDocumento
            Integer idTipoDocumento = Integer.parseInt(record.getString("idTipoDocumento"));
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
                    .orElseThrow(() -> new RuntimeException("TipoDocumento con ID " + idTipoDocumento + " no encontrado"));
            admin.setTipoDocumento(tipoDocumento);

            listaAdministradores.add(admin);
        });

        administradorRepository.saveAll(listaAdministradores);
        return true;
    }
}
