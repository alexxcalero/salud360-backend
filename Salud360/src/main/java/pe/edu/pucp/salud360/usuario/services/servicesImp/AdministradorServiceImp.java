package pe.edu.pucp.salud360.usuario.services.servicesImp;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
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

            String telefono = record.getString("telefono");
            if (!telefono.matches("\\d{9}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El teléfono '" + telefono + "' no es válido. Debe tener exactamente 9 dígitos numéricos.");
            }
            admin.setTelefono(telefono);

            admin.setFotoPerfil(record.getString("fotoPerfil"));
            admin.setNotificacionPorCorreo(Boolean.parseBoolean(record.getString("notificacionPorCorreo")));
            admin.setNotificacionPorSMS(Boolean.parseBoolean(record.getString("notificacionPorSMS")));
            admin.setNotificacionPorWhatsApp(Boolean.parseBoolean(record.getString("notificacionPorWhatsApp")));
            admin.setActivo(Boolean.parseBoolean(record.getString("activo")));
            admin.setFechaCreacion(LocalDateTime.now());

            String fechaDesact = record.getString("fechaDesactivacion");
            if (fechaDesact != null && !fechaDesact.isEmpty()) {
                admin.setFechaDesactivacion(LocalDateTime.parse(fechaDesact));
            }


            // Validar duplicidad dentro del CSV
            for (Administrador otroAdmin : listaAdministradores) {
                if (admin.getNombres().equalsIgnoreCase(otroAdmin.getNombres()) &&
                        admin.getNumeroDocumento().equals(otroAdmin.getNumeroDocumento())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Administrador duplicado en el archivo CSV con nombre " + admin.getNombres() +
                                    " y documento " + admin.getNumeroDocumento());
                }
            }

            // Validar duplicidad en BD
            List<Administrador> existentes = administradorRepository.findByNombresAndNumeroDocumento(
                    admin.getNombres(), admin.getNumeroDocumento());

            for (Administrador existente : existentes) {
                if (admin.getNombres().equalsIgnoreCase(existente.getNombres()) &&
                        admin.getNumeroDocumento().equals(existente.getNumeroDocumento())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El administrador '" + admin.getNombres() +
                                    "' con documento " + admin.getNumeroDocumento() +
                                    " ya se encuentra registrado en la base de datos");
                }
            }


            // Asociar Usuario
            String idUsuarioStr = record.getString("idUsuario");
            Usuario usuario;

            if (idUsuarioStr != null && !idUsuarioStr.isBlank()) {
                Integer idUsuario = Integer.parseInt(idUsuarioStr);
                usuario = usuarioRepository.findById(idUsuario)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Usuario con ID " + idUsuario + " no encontrado"));
            } else {
                String nombres = admin.getNombres().trim();
                String apellidos = admin.getApellidos().trim();
                String correo = (nombres + "." + apellidos + "@salud360.com")
                        .toLowerCase()
                        .replaceAll("\\s+", "")
                        .replaceAll("[^a-z0-9.@]", "");

                // Verificar si el correo ya existe antes de intentar guardar
                if (usuarioRepository.findByCorreo(correo).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El correo '" + correo + "' ya está registrado en el sistema.");
                }

                String contrasenha = passwordEncoder.encode(apellidos + "123");

                Rol rolAdmin = rolRepository.findByNombre("Admin")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Rol 'Admin' no encontrado"));

                usuario = new Usuario();
                usuario.setCorreo(correo);
                usuario.setContrasenha(contrasenha);
                usuario.setRol(rolAdmin);
                usuario.setActivo(true);

                usuarioRepository.save(usuario);
            }

            admin.setUsuario(usuario);

            Integer idTipoDocumento = Integer.parseInt(record.getString("idTipoDocumento"));
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "TipoDocumento con ID " + idTipoDocumento + " no encontrado"));
            admin.setTipoDocumento(tipoDocumento);


            listaAdministradores.add(admin);
        });

        administradorRepository.saveAll(listaAdministradores);
        return true;
    }

}
