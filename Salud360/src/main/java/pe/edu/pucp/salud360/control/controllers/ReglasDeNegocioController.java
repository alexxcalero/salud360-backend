package pe.edu.pucp.salud360.control.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.control.dto.ReglasDeNegocioDTO;
import pe.edu.pucp.salud360.control.services.ReglasDeNegocioService;

import java.util.List;

@RestController
@RequestMapping("/api/reglas")
public class ReglasDeNegocioController {
    @Autowired
    private ReglasDeNegocioService reglasDeNegocioService;

    @PostMapping
    public ResponseEntity<ReglasDeNegocioDTO> crearReglasDeNegocio(@RequestBody ReglasDeNegocioDTO reglasDTO) {
        ReglasDeNegocioDTO reglasCreadas = reglasDeNegocioService.crearReglasDeNegocio(reglasDTO);
        return new ResponseEntity<>(reglasDeNegocioService.buscarReglaDeNegocioPorId(reglasCreadas.getIdRegla()), HttpStatus.CREATED);
    }

    @PutMapping("{idReglas}")
    public ResponseEntity<ReglasDeNegocioDTO> actualizarReglasDeNegocio(@PathVariable("idReglas") Integer idReglas,
                                                                        @RequestBody ReglasDeNegocioDTO reglasDTO) {
        ReglasDeNegocioDTO reglasBuscadas = reglasDeNegocioService.buscarReglaDeNegocioPorId(idReglas);
        if(reglasBuscadas != null) {
            ReglasDeNegocioDTO reglasActualizadas = reglasDeNegocioService.actualizarReglasDeNegocio(idReglas, reglasDTO);
            return new ResponseEntity<>(reglasActualizadas, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{idReglas}")
    public ResponseEntity<String> eliminarReglasDeNegocio(@PathVariable("idReglas") Integer idReglas) {
        ReglasDeNegocioDTO reglasBuscadas = reglasDeNegocioService.buscarReglaDeNegocioPorId(idReglas);
        if(reglasBuscadas != null) {
            reglasDeNegocioService.eliminarReglasDeNegocio(idReglas);
            return new ResponseEntity<>("Reglas de negocio eliminadas satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Reglas de negocio no encontradas", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ReglasDeNegocioDTO>> listarReglasDeNegocio() {
        List<ReglasDeNegocioDTO> reglas = reglasDeNegocioService.listarReglasDeNegocio();
        return new ResponseEntity<>(reglas, HttpStatus.OK);
    }

    @GetMapping("{idReglas}")
    public ResponseEntity<ReglasDeNegocioDTO> buscarReglaDeNegocioPorId(@PathVariable("idReglas") Integer idReglas) {
        ReglasDeNegocioDTO reglasBuscadas = reglasDeNegocioService.buscarReglaDeNegocioPorId(idReglas);
        if(reglasBuscadas != null) {
            return new ResponseEntity<>(reglasBuscadas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
