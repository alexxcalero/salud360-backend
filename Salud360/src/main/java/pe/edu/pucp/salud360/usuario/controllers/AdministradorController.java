package pe.edu.pucp.salud360.usuario.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.services.AdministradorService;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;
    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<AdministradorResumenDTO> crearAdministrador(@RequestBody AdministradorRegistroDTO administradorDTO) {
        AdministradorResumenDTO administradorCreado = administradorService.crearAdministrador(administradorDTO);
        return new ResponseEntity<>(administradorCreado, HttpStatus.CREATED);
    }

    @PutMapping("{idAdministrador}")
    public ResponseEntity<AdministradorLogueadoDTO> actualizarAdministrador(@PathVariable("idAdministrador") Integer idAdministrador, @RequestBody AdministradorLogueadoDTO administradorDTO) {
        AdministradorLogueadoDTO administradorBuscado = administradorService.buscarAdminPorId(idAdministrador);
        if (administradorBuscado != null) {
            AdministradorLogueadoDTO administradorActualizado = administradorService.actualizarAdministrador(idAdministrador, administradorDTO);
            return new ResponseEntity<>(administradorActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{idAdministrador}")
    public ResponseEntity<String> eliminarAdministrador(@PathVariable("idAdministrador") Integer idAdministrador) {
        AdministradorResumenDTO administradorBuscado = administradorService.buscarAdministradorPorId(idAdministrador);
        if (administradorBuscado != null) {
            administradorService.eliminarAdministrador(idAdministrador);
            return new ResponseEntity<>("Administrador eliminado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Administrador no encontrado", HttpStatus.NOT_FOUND);
    }

    @PutMapping("{idAdministrador}/reactivar")
    public ResponseEntity<String> reactivarAdministrador(@PathVariable("idAdministrador") Integer idAdministrador) {
        AdministradorResumenDTO administradorBuscado = administradorService.buscarAdministradorPorId(idAdministrador);
        if (administradorBuscado != null) {
            administradorService.reactivarAdministrador(idAdministrador);
            return new ResponseEntity<>("Administrador reactivado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Administrador no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<AdministradorResumenDTO>> listarAdministradores() {
        List<AdministradorResumenDTO> administradores = administradorService.listarAdministradores();
        return new ResponseEntity<>(administradores, HttpStatus.OK);
    }

    @GetMapping("{idAdministrador}")
    public ResponseEntity<AdministradorResumenDTO> buscarAdministradorPorId(@PathVariable("idAdministrador") Integer idAdministrador) {
        AdministradorResumenDTO administradorBuscado = administradorService.buscarAdministradorPorId(idAdministrador);
        if (administradorBuscado != null)
            return new ResponseEntity<>(administradorBuscado, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("clientes")
    public ResponseEntity<ClienteVistaAdminDTO> crearClienteVistaAdmin(@RequestBody ClienteRegistroDTO clienteDTO) {
        ClienteVistaAdminDTO clienteCreado = clienteService.crearClienteVistaAdmin(clienteDTO);
        return new ResponseEntity<>(clienteCreado, HttpStatus.CREATED);
    }

    @PutMapping("clientes/{idCliente}")
    public ResponseEntity<ClienteVistaAdminDTO> actualizarClienteVistaAdmin(@PathVariable("idCliente") Integer idCliente, @RequestBody ClienteResumenDTO clienteDTO) {
        ClienteVistaAdminDTO clienteBuscado = clienteService.buscarClientePorId(idCliente);
        if (clienteBuscado != null) {
            ClienteVistaAdminDTO clienteActualizado = clienteService.actualizarClienteVistaAdmin(idCliente, clienteDTO);
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("clientes/{idCliente}")
    public ResponseEntity<String> eliminarCliente(@PathVariable("idCliente") Integer idCliente) {
        ClienteVistaAdminDTO clienteBuscado = clienteService.buscarClientePorId(idCliente);
        if (clienteBuscado != null) {
            clienteService.eliminarCliente(idCliente);
            return new ResponseEntity<>("Cliente eliminado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
    }

    @PutMapping("clientes/{idCliente}/reactivar")
    public ResponseEntity<String> reactivarCliente(@PathVariable("idCliente") Integer idCliente) {
        ClienteVistaAdminDTO clienteBuscado = clienteService.buscarClientePorId(idCliente);
        if (clienteBuscado != null) {
            clienteService.reactivarCliente(idCliente);
            return new ResponseEntity<>("Cliente reactivado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping("clientes")
    public ResponseEntity<List<ClienteVistaAdminDTO>> listarClientes() {
        List<ClienteVistaAdminDTO> clientes = clienteService.listarClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("clientes/{idCliente}")
    public ResponseEntity<ClienteVistaAdminDTO> buscarClientePorId(@PathVariable("idCliente") Integer idCliente) {
        ClienteVistaAdminDTO clienteBuscado = clienteService.buscarClientePorId(idCliente);
        if (clienteBuscado != null)
            return new ResponseEntity<>(clienteBuscado, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
