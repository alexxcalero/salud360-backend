package pe.edu.pucp.salud360.usuario.services.servicesImp;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.autenticacion.models.ActualizarContrasenhaRequest;
import pe.edu.pucp.salud360.autenticacion.models.ActualizarCorreoRequest;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;
import pe.edu.pucp.salud360.usuario.models.Rol;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.RolRepository;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;
import pe.edu.pucp.salud360.usuario.services.UsuarioService;
import com.univocity.parsers.common.record.Record;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    @Override
    public UsuarioResumenDTO buscarUsuarioPorCorreo(String correo) {
        Optional<Usuario> usuarioBuscado = usuarioRepository.findByCorreo(correo);
        if (usuarioBuscado.isPresent()) {
            Usuario usuario = usuarioBuscado.get();
            return usuarioMapper.mapToResumenDTO(usuario);
        } else {
            return null;
        }
    }

    @Override
    public UsuarioResumenDTO buscarUsuarioPorId(Integer idUsuario) {
        Optional<Usuario> usuarioBuscado = usuarioRepository.findById(idUsuario);
        if (usuarioBuscado.isPresent()) {
            Usuario usuario = usuarioBuscado.get();
            return usuarioMapper.mapToResumenDTO(usuario);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean cambiarContrasenha(Integer idUsuario, ActualizarContrasenhaRequest request) {
        Optional<Usuario> usuarioBuscado = usuarioRepository.findById(idUsuario);
        if(usuarioBuscado.isPresent()) {
            Usuario usuario = usuarioBuscado.get();

            if(!passwordEncoder.matches(request.getContrasenhaActual(), usuario.getContrasenha())) {
                throw new IllegalArgumentException("La contraseña actual no coincide");
            }

            String contrasenhaEncriptada = passwordEncoder.encode(request.getContrasenhaNueva());

            usuario.setContrasenha(contrasenhaEncriptada);
            usuarioRepository.save(usuario);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean cambiarCorreo(Integer idUsuario, ActualizarCorreoRequest request) {
        Optional<Usuario> usuarioBuscado = usuarioRepository.findById(idUsuario);
        if(usuarioBuscado.isPresent()) {
            Usuario usuario = usuarioBuscado.get();

            if(usuarioRepository.existsByCorreo(request.getCorreoNuevo())) {
                throw new IllegalArgumentException("El correo ya está en uso.");
            }

            usuario.setCorreo(request.getCorreoNuevo());
            usuarioRepository.save(usuario);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean cargarMasivamanteUsuario(MultipartFile file) throws IOException {
        List<Usuario> listaUsuarios = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        List<Record> parseAllRecords = parser.parseAllRecords(inputStream);

        parseAllRecords.forEach(record -> {
            Usuario usuario = new Usuario();

            // correo, contrasenha, activo, id_rol
            usuario.setCorreo(record.getString("correo"));
            usuario.setContrasenha(record.getString("contrasenha"));
            usuario.setActivo(Boolean.parseBoolean(record.getString("activo")));

            // Buscar el rol
            Integer idRol = Integer.parseInt(record.getString("id_rol"));
            Rol rol = rolRepository.findById(idRol)
                    .orElseThrow(() -> new RuntimeException("Rol con ID " + idRol + " no encontrado"));
            usuario.setRol(rol);

            listaUsuarios.add(usuario);
        });

        usuarioRepository.saveAll(listaUsuarios);
        return true;
    }

    /*
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioVistaAdminDTO crearUsuario(UsuarioRegistroDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.mapToModel(usuarioDTO);

        // Cifro la contrasenha antes de guardarla en BD
        String contrasenhaCifrada = passwordEncoder.encode(usuario.getContrasenha());
        usuario.setContrasenha(contrasenhaCifrada);

        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setFechaDesactivacion(null);
        Usuario usuarioCreado = usuarioRepository.save(usuario);
        return usuarioMapper.mapToVistaAdminDTO(usuarioCreado);
    }

    @Override
    public UsuarioVistaClienteDTO actualizarUsuario(Integer idUsuario, UsuarioVistaClienteDTO usuarioDTO) {
        if(usuarioRepository.findById(idUsuario).isPresent()){
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            usuario.setNombres(usuarioDTO.getNombres());
            usuario.setApellidos(usuarioDTO.getApellidos());
            usuario.setNumeroDocumento(usuarioDTO.getNumeroDocumento());
            usuario.setTipoDocumento(tipoDocumentoMapper.mapToModel(usuarioDTO.getTipoDocumento()));
            usuario.setCorreo(usuarioDTO.getCorreo());
            usuario.setTelefono(usuarioDTO.getTelefono());
            usuario.setSexo(usuarioDTO.getSexo());
            usuario.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return usuarioMapper.mapToVistaClienteDTO(usuarioActualizado);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void eliminarUsuario(Integer idUsuario) {
        if(usuarioRepository.findById(idUsuario).isPresent()) {
            Usuario usuarioEliminar = usuarioRepository.findById(idUsuario).get();

            // Elimino al usuario de la lista de usuarios asignados al rol al que pertenezca
            Rol rol = usuarioEliminar.getRol();
            rol.getUsuarios().remove(usuarioEliminar);

            usuarioEliminar.setActivo(false);
            usuarioEliminar.setFechaDesactivacion(LocalDateTime.now());
            usuarioRepository.save(usuarioEliminar);
        }
    }

    @Override
    public List<UsuarioVistaAdminDTO> listarUsuariosTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if(!(usuarios.isEmpty())) {
            return usuarios.stream().map(usuarioMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public UsuarioVistaAdminDTO buscarUsuarioPorIdEnAdmin(Integer idUsuario) {
        if(usuarioRepository.findById(idUsuario).isPresent()) {
            Usuario usuarioBuscado = usuarioRepository.findById(idUsuario).get();
            return usuarioMapper.mapToVistaAdminDTO(usuarioBuscado);
        } else {
            return null;
        }
    }

    @Override
    public UsuarioVistaClienteDTO buscarUsuarioPorIdEnCliente(Integer idUsuario) {
        if(usuarioRepository.findById(idUsuario).isPresent()) {
            Usuario usuarioBuscado = usuarioRepository.findById(idUsuario).get();
            return usuarioMapper.mapToVistaClienteDTO(usuarioBuscado);
        } else {
            return null;
        }
    }

    @Override
    public UsuarioVistaClienteDTO actualizarFotoPerfil(Integer idUsuario, String fotoPerfil) {
        if(usuarioRepository.findById(idUsuario).isPresent()){
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            usuario.setFotoPerfil(fotoPerfil);
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return usuarioMapper.mapToVistaClienteDTO(usuarioActualizado);
        } else {
            return null;
        }
    }

    @Override
    public UsuarioVistaClienteDTO actualizarMetodosDeNotificacion(Integer idUsuario, List<Boolean> ajustes) {
        if(usuarioRepository.findById(idUsuario).isPresent()){
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            usuario.setNotiCorreo(ajustes.get(0));
            usuario.setNotiSMS(ajustes.get(1));
            usuario.setNotiWhatsApp(ajustes.get(2));
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return usuarioMapper.mapToVistaClienteDTO(usuarioActualizado);
        } else {
            return null;
        }
    }

    @Override
    public Boolean actualizarContrasenha(Integer idUsuario, String contrasenhaNueva) {
        if(usuarioRepository.findById(idUsuario).isPresent()){
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            String contrasenhaCifrada = passwordEncoder.encode(contrasenhaNueva);
            usuario.setContrasenha(contrasenhaCifrada);
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UsuarioVistaClienteDTO buscarUsuarioPorCorreoEnCliente(String correo) {
        if(usuarioRepository.findByCorreo(correo).isPresent()) {
            Usuario usuario = usuarioRepository.findByCorreo(correo).get();
            return usuarioMapper.mapToVistaClienteDTO(usuario);
        } else {
            return null;
        }
    }

    @Override
    public Usuario buscarUsuarioPorCorreoEnLogin(String correo) {
        if(usuarioRepository.findByCorreo(correo).isPresent()) {
            return usuarioRepository.findByCorreo(correo).get();
        } else {
            return null;
        }
    }

    @Override
    public List<UsuarioVistaAdminDTO> listarUsuariosTodosPorCorreo(String correo) {
        List<Usuario> usuarios = usuarioRepository.findAllByCorreoContainingIgnoreCase(correo);
        if(!(usuarios.isEmpty())) {
            return usuarios.stream().map(usuarioMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<UsuarioVistaAdminDTO> listarUsuariosTodosPorNombre(String nombre) {
        List<Usuario> usuarios = usuarioRepository.findAllByNombresContainingIgnoreCase(nombre);
        if(!(usuarios.isEmpty())) {
            return usuarios.stream().map(usuarioMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }
    */
}
