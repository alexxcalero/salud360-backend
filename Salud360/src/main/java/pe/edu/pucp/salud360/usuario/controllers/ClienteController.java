package pe.edu.pucp.salud360.usuario.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PutMapping("{idCliente}")
    public ResponseEntity<ClienteLogueadoDTO> actualizarClienteVistaPerfil(@PathVariable("idCliente") Integer idCliente, @RequestBody ClienteResumenDTO clienteDTO) {
        ClienteLogueadoDTO clienteBuscado = clienteService.buscarClienteLogueadoPorId(idCliente);
        if (clienteBuscado != null) {
            ClienteLogueadoDTO clienteActualizado = clienteService.actualizarClienteVistaPerfil(idCliente, clienteDTO);
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{idCliente}/reservas")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorCliente(@PathVariable("idCliente") Integer idCliente) {
        List<ReservaDTO> lista = clienteService.listarReservasPorCliente(idCliente);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
}
