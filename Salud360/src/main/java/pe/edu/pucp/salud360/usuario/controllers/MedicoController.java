package pe.edu.pucp.salud360.usuario.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.medicoDTO.MedicoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.services.MedicoService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") //PARA QUE SE CONECTE CON EL FRONT
@RestController
@RequestMapping("/api/admin/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

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

    @PutMapping("{idMedico}/reactivar")
    public ResponseEntity<String> reactivarMedico(@PathVariable("idMedico") Integer idMedico) {
        MedicoVistaAdminDTO medicoBuscado = medicoService.buscarMedicoPorId(idMedico);
        if (medicoBuscado != null) {
            medicoService.reactivarMedico(idMedico);
            return new ResponseEntity<>("Médico reactivado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Médico no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<MedicoVistaAdminDTO>> listarMedicosTodos(@RequestParam(required = false) String nombreCompleto, @RequestParam(required = false) String especialidad) {
        List<MedicoVistaAdminDTO> medicos = medicoService.listarMedicos(nombreCompleto, especialidad);
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
