package pe.edu.pucp.salud360.usuario.dtos;

import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaClienteDTO;

public class LoginResponseDTO {
    private String token;
    private UsuarioVistaClienteDTO usuario;

    public LoginResponseDTO(String token, UsuarioVistaClienteDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public UsuarioVistaClienteDTO getUsuario() {
        return usuario;
    }
}
