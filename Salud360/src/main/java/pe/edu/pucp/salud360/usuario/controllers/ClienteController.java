package pe.edu.pucp.salud360.usuario.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.services.AfiliacionService;
import pe.edu.pucp.salud360.membresia.services.PagoService;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.services.ReservaService;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioPerfilDTO;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ComunidadService comunidadService;
    private final  PagoService pagoService;
    private final AfiliacionService afiliacionService;


    @PutMapping("{idCliente}")
    public ResponseEntity<ClienteLogueadoDTO> actualizarClienteVistaPerfil(@PathVariable("idCliente") Integer idCliente, @RequestBody UsuarioPerfilDTO clienteDTO) {
        ClienteLogueadoDTO clienteBuscado = clienteService.buscarClienteLogueadoPorId(idCliente);
        if (clienteBuscado != null) {
            ClienteLogueadoDTO clienteActualizado = clienteService.actualizarClienteVistaPer(idCliente, clienteDTO);
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{idCliente}/reservas/{idComunidad}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorCliente(@PathVariable("idCliente") Integer idCliente, @PathVariable("idComunidad") Integer idComunidad) {
        List<ReservaDTO> lista = clienteService.listarReservasPorCliente(idCliente, idComunidad);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/comunidad")
    public ResponseEntity<Map<String, Integer>> cantClientesComunidad(){
        Map<String, Integer> mapa = new HashMap<>();
        List<ClienteVistaAdminDTO> lista = clienteService.listarClientes();
        for (ClienteVistaAdminDTO cli : lista){
            List<ComunidadDTO> comunidades = cli.getComunidades();
            for (ComunidadDTO comunidad : comunidades) {
                String clave = comunidad.getNombre(); // o el campo que quieras usar como clave
                mapa.put(clave, mapa.getOrDefault(clave, 0) + 1);
            }
        }
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    @GetMapping("/membresia")
    public ResponseEntity<Map<String, Integer>> cantClientesMembresia(){
        Map<String, Integer> mapa = new HashMap<>();
        List<ClienteVistaAdminDTO> lista = clienteService.listarClientes();
        for (ClienteVistaAdminDTO cli : lista){
            List<AfiliacionDTO> afiliaciones = cli.getAfiliaciones();
            for (AfiliacionDTO af : afiliaciones) {
                String clave = af.getMembresia().getNombre(); // o el campo que quieras usar como clave
                mapa.put(clave, mapa.getOrDefault(clave, 0) + 1);
            }
        }
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    @GetMapping("/mes/{fecha}")
    public ResponseEntity<List<ClienteVistaAdminDTO>> cantClientesMes(@RequestParam LocalDateTime fecha){
        List<ClienteVistaAdminDTO> lista = clienteService.listarClientes();
        List<ClienteVistaAdminDTO> lista2 = new ArrayList<>();
        for (ClienteVistaAdminDTO cli : lista){
            if(cli.getFechaCreacion() == fecha) lista2.add(cli);
        }
        return new ResponseEntity<>(lista2, HttpStatus.OK);
    }
  
    @GetMapping("/aleatoria")
    public ResponseEntity<ComunidadDTO> obtenerComunidadAleatoria(@RequestParam Integer idCliente) {
        ComunidadDTO dto = comunidadService.obtenerComunidadAleatoriaExcluyendoCliente(idCliente);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

    @GetMapping("/excluyendo-cliente")
    public ResponseEntity<List<ComunidadDTO>> obtenerComunidadesExcluyendoCliente(@RequestParam Integer idCliente) {
        List<ComunidadDTO> dtoList = comunidadService.obtenerComunidadesExcluyendoCliente(idCliente);
        return dtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @GetMapping("/comunidades-inactivas")
    public ResponseEntity<List<ComunidadDTO>> obtenerComunidadesInactivas(@RequestParam Integer idCliente) {
        List<ComunidadDTO> comunidades = comunidadService.obtenerComunidadesInactivasCliente(idCliente);
        return comunidades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(comunidades);
    }

    @GetMapping("/comunidades-activas")
    public ResponseEntity<List<ComunidadDTO>> obtenerComunidadesActivas(@RequestParam Integer idCliente) {
        List<ComunidadDTO> comunidades = comunidadService.obtenerComunidadesActivasCliente(idCliente);
        return comunidades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(comunidades);
    }

    @GetMapping("/{idCliente}/afiliaciones-cliente")
    public ResponseEntity<List<AfiliacionResumenDTO>> listarAfiliacionesPorCliente(@PathVariable Integer idCliente) {
        List<AfiliacionResumenDTO> afiliaciones = afiliacionService.listarAfiliacionesPorCliente(idCliente);
        return afiliaciones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(afiliaciones);
    }




    @GetMapping("/{idCliente}/pagos-cliente")
    public ResponseEntity<List<PagoDTO>> listarPagosPorCliente(@PathVariable Integer idCliente) {

        List<PagoDTO> pagos = pagoService.obtenerPagosPorCliente(idCliente);
        return pagos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagos);
    }

    @PostMapping("/cargaMasiva")
    public String cargaMasivaClientes(@RequestParam("file") MultipartFile file) throws Exception {
        Boolean cargado = clienteService.cargarMasivamanteCliente(file);
        if (cargado) {
            return "Archivo cargado satisfactoriamente";
        } else {
            return "Archivo no cargado";
        }
    }

}
