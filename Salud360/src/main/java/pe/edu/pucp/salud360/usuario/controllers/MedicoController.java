package pe.edu.pucp.salud360.usuario.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.services.MedicoService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") //PARA QUE SE CONECTE CON EL FRONT
@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<MedicoVistaAdminDTO> crearMedico(@RequestBody MedicoRegistroDTO medicoDTO) {
        MedicoVistaAdminDTO medicoCreado = medicoService.crearMedico(medicoDTO);
        return new ResponseEntity<>(medicoCreado, HttpStatus.CREATED);
    }

    @PutMapping("{idMedico}")
    public ResponseEntity<MedicoVistaAdminDTO> actualizarMedico(@PathVariable("idMedico") Integer idMedico, @RequestBody MedicoVistaAdminDTO medicoDTO) {
        MedicoVistaAdminDTO medicoBuscado = medicoService.buscarMedicoPorId(idMedico);
        if (medicoBuscado != null) {
            MedicoVistaAdminDTO medicoActualizado = medicoService.actualizarMedico(idMedico, medicoDTO);
            return new ResponseEntity<>(medicoActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{idMedico}")
    public ResponseEntity<String> eliminarMedico(@PathVariable("idMedico") Integer idMedico) {
        MedicoVistaAdminDTO medicoBuscado = medicoService.buscarMedicoPorId(idMedico);
        if (medicoBuscado != null) {
            medicoService.eliminarMedico(idMedico);
            return new ResponseEntity<>("Médico eliminado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Médico no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<MedicoVistaAdminDTO>> listarMedicosTodos() {
        List<MedicoVistaAdminDTO> medicos = medicoService.listarMedicosTodos();
        return new ResponseEntity<>(medicos, HttpStatus.OK);
    }

    @GetMapping("{idMedico}")
    public ResponseEntity<MedicoVistaAdminDTO> buscarMedicoPorId(@PathVariable("idMedico") Integer idMedico) {
        MedicoVistaAdminDTO medicoBuscado = medicoService.buscarMedicoPorId(idMedico);
        if (medicoBuscado != null)
            return new ResponseEntity<>(medicoBuscado, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
