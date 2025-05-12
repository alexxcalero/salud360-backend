package pe.edu.pucp.salud360.usuario.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaClienteDTO;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;
import pe.edu.pucp.salud360.usuario.models.Rol;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;
import pe.edu.pucp.salud360.usuario.services.UsuarioService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImp implements UsuarioService {
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
        List<Usuario> usuarios = usuarioRepository.findByCorreoContainingIgnoreCase(correo);
        if(!(usuarios.isEmpty())) {
            return usuarios.stream().map(usuarioMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }
}
