package pe.edu.pucp.salud360.usuario.services;

import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaClienteDTO;
import pe.edu.pucp.salud360.usuario.models.Usuario;

import java.util.List;

public interface UsuarioService {
    UsuarioVistaAdminDTO crearUsuario(UsuarioRegistroDTO usuarioDTO);
    UsuarioVistaClienteDTO actualizarUsuario(Integer idUsuario, UsuarioVistaClienteDTO usuarioDTO);
    void eliminarUsuario(Integer idUsuario);
    List<UsuarioVistaAdminDTO> listarUsuariosTodos();
    UsuarioVistaAdminDTO buscarUsuarioPorIdEnAdmin(Integer idUsuario);

    UsuarioVistaClienteDTO buscarUsuarioPorIdEnCliente(Integer idUsuario);
    UsuarioVistaClienteDTO actualizarFotoPerfil(Integer idUsuario, String fotoPerfil);
    UsuarioVistaClienteDTO actualizarMetodosDeNotificacion(Integer idUsuario, List<Boolean> ajustes);
    Boolean actualizarContrasenha(Integer idUsuario, String contrasenhaNueva);
    UsuarioVistaClienteDTO buscarUsuarioPorCorreoEnCliente(String correo);
    Usuario buscarUsuarioPorCorreoEnLogin(String correo);  // No lo devuelvo al front, así que no rompo el disenho DTO
    List<UsuarioVistaAdminDTO> listarUsuariosTodosPorCorreo(String correo);
    List<UsuarioVistaAdminDTO> listarUsuariosTodosPorNombre(String nombre);
}
