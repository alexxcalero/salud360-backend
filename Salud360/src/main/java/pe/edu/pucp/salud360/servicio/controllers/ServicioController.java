package pe.edu.pucp.salud360.servicio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioResumenDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.dto.ServicioDTO.ServicioVistaClienteDTO;
import pe.edu.pucp.salud360.servicio.services.ServicioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @PostMapping
    public ResponseEntity<ServicioDTO> crearServicio(@RequestBody ServicioDTO dto) {
        ServicioDTO creado = servicioService.crearServicio(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable("id") Integer id,
                                                          @RequestBody ServicioDTO dto) {
        ServicioVistaAdminDTO existente = servicioService.buscarServicioPorId(id);
        if (existente != null) {
            ServicioDTO actualizado = servicioService.actualizarServicio(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarServicio(@PathVariable("id") Integer id) {
        ServicioVistaAdminDTO existente = servicioService.buscarServicioPorId(id);
        if (existente != null) {
            servicioService.eliminarServicio(id);
            return new ResponseEntity<>("Servicio eliminado correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Servicio no encontrado", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<String> reactivarServicio(@PathVariable("id") Integer idServicio) {
        ServicioVistaAdminDTO servicioBuscado = servicioService.buscarServicioPorId(idServicio);
        if (servicioBuscado != null) {
            servicioService.reactivarServicio(idServicio);
            return new ResponseEntity<>("Servicio reactivado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Servicio no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ServicioVistaAdminDTO>> listarServicios() {
        List<ServicioVistaAdminDTO> lista = servicioService.listarServiciosTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/comunidad")
    public ResponseEntity<Map<String, Integer>> cantServiciosComunidad(){
        Map<String, Integer> mapa = new HashMap<>();
        List<ServicioVistaAdminDTO> lista = servicioService.listarServiciosTodos();
        for (ServicioVistaAdminDTO serv : lista){
            List<ComunidadResumenDTO> comunidades = serv.getComunidades();
            for (ComunidadResumenDTO comunidad : comunidades) {
                String clave = comunidad.getNombre(); // o el campo que quieras usar como clave
                mapa.put(clave, mapa.getOrDefault(clave, 0) + 1);
            }
        }
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ServicioVistaClienteDTO>> listarServiciosClientes() {
        List<ServicioVistaClienteDTO> lista = servicioService.listarServiciosClientes();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioVistaAdminDTO> buscarServicioPorId(@PathVariable("id") Integer id) {
        ServicioVistaAdminDTO servicio = servicioService.buscarServicioPorId(id);
        if (servicio != null)
            return new ResponseEntity<>(servicio, HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }

    //CON FE
    @PostMapping("/cargaMasiva")
    public String cargaMasivaServicio(@RequestParam ("file") MultipartFile file) throws Exception {
        Boolean cargado = servicioService.cargarMasivamante(file);
        if(cargado){
            return "Archivo cargado satisfactoriamente";
        }else
            return "Archivo no cargado";
    }
}
