package pe.edu.pucp.salud360.usuario.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.seguridad.JwtService;
import pe.edu.pucp.salud360.usuario.dtos.LoginRequestDTO;
import pe.edu.pucp.salud360.usuario.dtos.LoginResponseDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioVistaClienteDTO;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.services.UsuarioService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // PARA QUE SE CONECTE CON EL FRONT
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<UsuarioVistaAdminDTO> crearUsuario(@RequestBody UsuarioRegistroDTO usuarioDTO) {
        UsuarioVistaAdminDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);
        return new ResponseEntity<>(usuarioService.buscarUsuarioPorIdEnAdmin(usuarioCreado.getIdUsuario()), HttpStatus.CREATED);
    }

    @PutMapping("{idUsuario}")
    public ResponseEntity<UsuarioVistaClienteDTO> actualizarUsuario(@PathVariable("idUsuario") Integer idUsuario,
                                                                    @RequestBody UsuarioVistaClienteDTO usuarioDTO) {
        UsuarioVistaClienteDTO usuarioBuscado = usuarioService.buscarUsuarioPorIdEnCliente(idUsuario);
        if(usuarioBuscado != null) {
            UsuarioVistaClienteDTO usuarioActualizado = usuarioService.actualizarUsuario(idUsuario, usuarioDTO);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{idUsuario}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("idUsuario") Integer idUsuario) {
        UsuarioVistaClienteDTO usuarioBuscado = usuarioService.buscarUsuarioPorIdEnCliente(idUsuario);
        if(usuarioBuscado != null) {
            usuarioService.eliminarUsuario(idUsuario);
            return new ResponseEntity<>("Usuario eliminado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioVistaAdminDTO>> listarUsuariosTodos() {
        List<UsuarioVistaAdminDTO> usuarios = usuarioService.listarUsuariosTodos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("{idUsuario}")
    public ResponseEntity<UsuarioVistaAdminDTO> buscarUsuarioPorIdEnAdmin(@PathVariable("idUsuario") Integer idUsuario) {
        UsuarioVistaAdminDTO usuarioBuscado = usuarioService.buscarUsuarioPorIdEnAdmin(idUsuario);
        if(usuarioBuscado != null)
            return new ResponseEntity<>(usuarioBuscado, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("{idUsuario}/cambiarFotoPerfil")
    public ResponseEntity<UsuarioVistaClienteDTO> actualizarFotoPerfil(@PathVariable("idUsuario") Integer idUsuario,
                                                                       @RequestParam("fotoPerfil") String fotoPerfil) {
        UsuarioVistaClienteDTO usuarioBuscado = usuarioService.buscarUsuarioPorIdEnCliente(idUsuario);
        if(usuarioBuscado != null) {
            UsuarioVistaClienteDTO usuarioActualizado = usuarioService.actualizarFotoPerfil(idUsuario, fotoPerfil);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("{idUsuario}/actualizarMetodosDeNotificacion")
    public ResponseEntity<UsuarioVistaClienteDTO> actualizarMetodosDeNotificacion(@PathVariable("idUsuario") Integer idUsuario,
                                                                       @RequestParam("ajustes") List<Boolean> ajustes) {
        UsuarioVistaClienteDTO usuarioBuscado = usuarioService.buscarUsuarioPorIdEnCliente(idUsuario);
        if(usuarioBuscado != null) {
            UsuarioVistaClienteDTO usuarioActualizado = usuarioService.actualizarMetodosDeNotificacion(idUsuario, ajustes);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("{idUsuario}/cambiarContrasenha")
    public ResponseEntity<String> actualizarContrasenha(@PathVariable("idUsuario") Integer idUsuario,
                                                        @RequestParam("contrasenha") String contrasenha) {
        UsuarioVistaClienteDTO usuarioBuscado = usuarioService.buscarUsuarioPorIdEnCliente(idUsuario);
        if(usuarioBuscado != null) {
            if(usuarioService.actualizarContrasenha(idUsuario, contrasenha))
                return new ResponseEntity<>("Se actualizó la contraseña correctamente", HttpStatus.OK);
            else
                return new ResponseEntity<>("No se pudo actualizar la contraseña", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> iniciarSesion(@RequestBody LoginRequestDTO loginRequest) {
        String correo = loginRequest.getCorreo();
        String contrasenha = loginRequest.getContrasenha();

        Usuario usuarioBuscado = usuarioService.buscarUsuarioPorCorreoEnLogin(correo);
        if (usuarioBuscado != null) {
            String contrasenhaUsuario = usuarioBuscado.getContrasenha();
            if (passwordEncoder.matches(contrasenha, contrasenhaUsuario)) {
                String token = jwtService.generateToken(correo); //Generamos el token para persistir la sesión en el front
                UsuarioVistaClienteDTO usuarioDTO = usuarioService.buscarUsuarioPorCorreoEnCliente(correo);
                return ResponseEntity.ok(new LoginResponseDTO(token, usuarioDTO));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /*@PostMapping("/login")
    public ResponseEntity<UsuarioVistaClienteDTO> iniciarSesion(@RequestParam("correo") String correo,
                                                                @RequestParam("contrasenha") String contrasenha) {
        // Recupero el model tal cual, y no el DTO, esto para obtener la contrasenha
        Usuario usuarioBuscado = usuarioService.buscarUsuarioPorCorreoEnLogin(correo);
        if(usuarioBuscado != null) {
            String contrasenhaUsuario = usuarioBuscado.getContrasenha();
            if(passwordEncoder.matches(contrasenha, contrasenhaUsuario)) {
                return new ResponseEntity<>(usuarioService.buscarUsuarioPorCorreoEnCliente(correo), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }*/

    @GetMapping("/listarUsuariosPorCorreo")
    public ResponseEntity<List<UsuarioVistaAdminDTO>> listarUsuariosTodosPorCorreo(@RequestParam("correo") String correo) {
        List<UsuarioVistaAdminDTO> usuarios = usuarioService.listarUsuariosTodosPorCorreo(correo);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/listarUsuariosPorNombre")
    public ResponseEntity<List<UsuarioVistaAdminDTO>> listarUsuariosTodosPorNombre(@RequestParam("nombre") String nombre) {
        List<UsuarioVistaAdminDTO> usuarios = usuarioService.listarUsuariosTodosPorNombre(nombre);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
}
