package pe.edu.pucp.salud360.usuario.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.services.ClienteService;


@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ComunidadService comunidadService;



    @PutMapping("{idCliente}")
    public ResponseEntity<ClienteLogueadoDTO> actualizarClienteVistaPerfil(@PathVariable("idCliente") Integer idCliente, @RequestBody ClienteResumenDTO clienteDTO) {
        ClienteLogueadoDTO clienteBuscado = clienteService.buscarClienteLogueadoPorId(idCliente);
        if (clienteBuscado != null) {
            ClienteLogueadoDTO clienteActualizado = clienteService.actualizarClienteVistaPerfil(idCliente, clienteDTO);
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/aleatoria")
    public ResponseEntity<ComunidadDTO> obtenerComunidadAleatoria(@RequestParam Integer idCliente) {
        ComunidadDTO dto = comunidadService.obtenerComunidadAleatoriaExcluyendoCliente(idCliente);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

}
