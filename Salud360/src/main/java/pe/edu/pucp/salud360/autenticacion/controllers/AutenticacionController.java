package pe.edu.pucp.salud360.autenticacion.controllers;

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
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import pe.edu.pucp.salud360.usuario.mappers.UsuarioMapper;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

import java.util.Optional;

@RestController
@RequestMapping("/api/autenticacion")
@RequiredArgsConstructor
public class AutenticacionController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ClienteService clienteService;

    @PostMapping("/registro")
    public ResponseEntity<ClienteLogueadoDTO> crearClientePorRegistro(@RequestBody ClienteRegistroDTO clienteDTO) {
        ClienteLogueadoDTO clienteCreado = clienteService.crearClientePorRegistro(clienteDTO);
        return new ResponseEntity<>(clienteCreado, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> iniciarSesion(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCorreo(),
                            loginRequest.getContrasenha()
                    )
            );

            Usuario usuario = (Usuario) auth.getPrincipal();
            String token = jwtService.generateToken(usuario);

            UsuarioResumenDTO usuarioDTO = usuarioMapper.mapToResumenDTO(usuario);

            return ResponseEntity.ok(new LoginResponse(token, usuarioDTO));
        } catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
