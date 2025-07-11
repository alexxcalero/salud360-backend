package pe.edu.pucp.salud360.servicio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.servicio.dto.CitaMedicaDTO.CitaMedicaDTO;
import pe.edu.pucp.salud360.servicio.services.CitaMedicaService;

import java.util.List;

@RestController
@RequestMapping("/api/citas-medicas")
@RequiredArgsConstructor
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    @PostMapping
    public ResponseEntity<CitaMedicaDTO> crearCita(@RequestBody CitaMedicaDTO citaDTO) {
        CitaMedicaDTO creada = citaMedicaService.crearCitaMedica(citaDTO);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaMedicaDTO> actualizarCita(@PathVariable("id") Integer id,
                                                        @RequestBody CitaMedicaDTO citaDTO) {
        CitaMedicaDTO existente = citaMedicaService.buscarCitaMedicaPorId(id);
        if (existente != null) {
            CitaMedicaDTO actualizada = citaMedicaService.actualizarCitaMedica(id, citaDTO);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCita(@PathVariable("id") Integer id) {
        CitaMedicaDTO existente = citaMedicaService.buscarCitaMedicaPorId(id);
        if (existente != null) {
            citaMedicaService.eliminarCitaMedica(id);
            return new ResponseEntity<>("Cita eliminada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cita no encontrada", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/reactivar")
    public ResponseEntity<String> reactivarCita(@PathVariable("id") Integer id) {
        CitaMedicaDTO existente = citaMedicaService.buscarCitaMedicaPorId(id);
        if (existente != null) {
            citaMedicaService.reactivarCitaMedica(id);
            return new ResponseEntity<>("Cita reactivada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cita no encontrada", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<CitaMedicaDTO>> listarCitas() {
        List<CitaMedicaDTO> lista = citaMedicaService.listarCitasMedicasTodas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedicaDTO> buscarCitaPorId(@PathVariable("id") Integer id) {
        CitaMedicaDTO cita = citaMedicaService.buscarCitaMedicaPorId(id);
        if (cita != null)
            return new ResponseEntity<>(cita, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    //CON FE
    @PostMapping("/cargaMasiva")
    public String cargaMasivaLocal(@RequestParam ("file") MultipartFile file) throws Exception {
        Boolean cargado = citaMedicaService.cargarMasivamante(file);
        if(cargado){
            return "Archivo cargado satisfactoriamente";
        }else
            return "Archivo no cargado";
    }

}
