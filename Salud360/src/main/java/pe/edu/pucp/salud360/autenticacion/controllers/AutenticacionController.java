package pe.edu.pucp.salud360.autenticacion.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.pucp.salud360.autenticacion.models.LoginRequest;
import pe.edu.pucp.salud360.autenticacion.models.LoginResponse;
import pe.edu.pucp.salud360.seguridad.JwtService;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioLogueadoDTO;
import pe.edu.pucp.salud360.usuario.mappers.AdministradorMapper;
import pe.edu.pucp.salud360.usuario.mappers.ClienteMapper;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;
import pe.edu.pucp.salud360.usuario.models.Administrador;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.AdministradorRepository;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

@RestController
@RequestMapping("/api/autenticacion")
@RequiredArgsConstructor
public class AutenticacionController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final AdministradorMapper administradorMapper;
    private final AdministradorRepository administradorRepository;

    @PostMapping("/signup")
    public ResponseEntity<ClienteLogueadoDTO> crearClientePorRegistro(@RequestBody ClienteRegistroDTO clienteDTO) {
        ClienteLogueadoDTO clienteCreado = clienteService.crearClientePorRegistro(clienteDTO);
        return new ResponseEntity<>(clienteCreado, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> iniciarSesion(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCorreo(),
                            loginRequest.getContrasenha()
                    )
            );

            Usuario usuario = (Usuario) auth.getPrincipal();
            String token = jwtService.generateToken(usuario);

            UsuarioLogueadoDTO usuarioDTO = usuarioMapper.mapToLogueadoDTO(usuario);

            if(usuario.getCliente() != null) {
                Cliente cliente = clienteRepository.findByUsuario(usuario)
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                ClienteLogueadoDTO clienteDTO = clienteMapper.mapToLogueadoDTO(cliente);
                usuarioDTO.setCliente(clienteDTO);
            } else if (usuario.getAdministrador() != null) {
                Administrador admin = administradorRepository.findByUsuario(usuario)
                        .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
                AdministradorLogueadoDTO adminDTO = administradorMapper.mapToLogueadoDTO(admin);
                usuarioDTO.setAdministrador(adminDTO);
            }

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Asegúrate de usar HTTPS en producción
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1 día

            response.addCookie(cookie);

            return ResponseEntity.ok(new LoginResponse(token, usuarioDTO));
        } catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
