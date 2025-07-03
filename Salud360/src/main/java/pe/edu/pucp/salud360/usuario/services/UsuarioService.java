package pe.edu.pucp.salud360.usuario.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.autenticacion.models.ActualizarContrasenhaRequest;
import pe.edu.pucp.salud360.autenticacion.models.ActualizarCorreoRequest;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;

import java.io.IOException;

public interface UsuarioService {
    UsuarioResumenDTO buscarUsuarioPorCorreo(String correo);
    UsuarioResumenDTO buscarUsuarioPorId(Integer idUsuario);
    Boolean cambiarContrasenha(Integer idUsuario, ActualizarContrasenhaRequest request);
    Boolean cambiarCorreo(Integer idUsuario, ActualizarCorreoRequest request);
    Boolean cargarMasivamanteUsuario(MultipartFile file) throws IOException;
    /*
    UsuarioVistaAdminDTO crearUsuario(UsuarioRegistroDTO usuarioDTO);
    UsuarioVistaClienteDTO actualizarUsuario(Integer idUsuario, UsuarioVistaClienteDTO usuarioDTO);
    void eliminarUsuario(Integer idUsuario);
    List<UsuarioVistaAdminDTO> listarUsuariosTodos();
    UsuarioVistaAdminDTO buscarUsuarioPorIdEnAdmin(Integer idUsuario);

    UsuarioVistaClienteDTO actualizarFotoPerfil(Integer idUsuario, String fotoPerfil);
    UsuarioVistaClienteDTO actualizarMetodosDeNotificacion(Integer idUsuario, List<Boolean> ajustes);
    Boolean actualizarContrasenha(Integer idUsuario, String contrasenhaNueva);
    UsuarioVistaClienteDTO buscarUsuarioPorCorreoEnCliente(String correo);
    Usuario buscarUsuarioPorCorreoEnLogin(String correo);  // No lo devuelvo al front, así que no rompo el disenho DTO
    List<UsuarioVistaAdminDTO> listarUsuariosTodosPorCorreo(String correo);
    List<UsuarioVistaAdminDTO> listarUsuariosTodosPorNombre(String nombre);
    */
}
