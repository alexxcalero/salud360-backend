package pe.edu.pucp.salud360.usuario.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.autenticacion.models.ActualizarContrasenhaRequest;
import pe.edu.pucp.salud360.autenticacion.models.ActualizarCorreoRequest;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioPerfilDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import pe.edu.pucp.salud360.usuario.services.AdministradorService;
import pe.edu.pucp.salud360.usuario.services.ClienteService;
import pe.edu.pucp.salud360.usuario.services.UsuarioService;

@CrossOrigin(origins = "http://localhost:5173") // PARA QUE SE CONECTE CON EL FRONT
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final AdministradorService administradorService;

    //falta adapatar el segundo parametro para que sea compatible con admin tambien
    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer idUsuario, @RequestBody UsuarioPerfilDTO dto) {
        // Primero intenta actualizar como cliente
        try {
            ClienteLogueadoDTO actualizado = clienteService.actualizarClienteVistaPer(idUsuario, dto);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            // Si no es cliente, intenta como administrador
            try {
                AdministradorLogueadoDTO adminActualizado = administradorService.actualizarAdministrador(idUsuario, null);
                return ResponseEntity.ok(adminActualizado);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        }
    }

    @PutMapping("{idUsuario}/cambiarContrasenha")
    public ResponseEntity<String> cambiarContrasenha(@PathVariable("idUsuario") Integer idUsuario,
                                                     @RequestBody ActualizarContrasenhaRequest actualizarContrasenhaRequest) {
        UsuarioResumenDTO usuarioBuscado = usuarioService.buscarUsuarioPorId(idUsuario);
        if(usuarioBuscado != null) {
            if(usuarioService.cambiarContrasenha(idUsuario, actualizarContrasenhaRequest))
                return new ResponseEntity<>("Se actualizó la contraseña correctamente", HttpStatus.OK);
            else {
                return new ResponseEntity<>("No se pudo actualizar la contraseña", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No se encontró al usuario", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{idUsuario}/cambiarCorreo")
    public ResponseEntity<String> cambiarCorreo(@PathVariable("idUsuario") Integer idUsuario,
                                                @RequestBody ActualizarCorreoRequest actualizarCorreoRequest) {
        UsuarioResumenDTO usuarioBuscado = usuarioService.buscarUsuarioPorId(idUsuario);
        if (usuarioBuscado != null) {
            if (usuarioService.cambiarCorreo(idUsuario, actualizarCorreoRequest))
                return new ResponseEntity<>("Se actualizó el correo correctamente", HttpStatus.OK);
            else {
                return new ResponseEntity<>("No se pudo actualizar el correo", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No se encontró al usuario", HttpStatus.NOT_FOUND);
        }
    }


    /*
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

    @PostMapping("/login")
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
    }

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
    */
}
