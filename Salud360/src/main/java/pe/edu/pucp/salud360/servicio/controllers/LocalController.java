package pe.edu.pucp.salud360.servicio.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalVistaAdminDTO;
import pe.edu.pucp.salud360.servicio.services.LocalService;
import pe.edu.pucp.salud360.servicio.dto.LocalDTO.LocalDTO;

import java.util.List;

@RestController
@RequestMapping("/api/locales")
public class LocalController {

    @Autowired
    private LocalService localService;

    @PostMapping
    public ResponseEntity<LocalVistaAdminDTO> crearLocal(@RequestBody LocalVistaAdminDTO dto) {
        LocalVistaAdminDTO creado = localService.crearLocal(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalVistaAdminDTO> actualizarLocal(@PathVariable("id") Integer id,
                                                              @RequestBody LocalVistaAdminDTO dto) {
        LocalVistaAdminDTO existente = localService.buscarLocalPorId(id);
        if (existente != null) {
            LocalVistaAdminDTO actualizado = localService.actualizarLocal(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarLocal(@PathVariable("id") Integer id) {
        LocalVistaAdminDTO existente = localService.buscarLocalPorId(id);
        if (existente != null) {
            localService.eliminarLocal(id);
            return new ResponseEntity<>("Local eliminado correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Local no encontrado", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<String> reactivarLocal(@PathVariable("id") Integer idLocal) {
        LocalVistaAdminDTO localBuscado = localService.buscarLocalPorId(idLocal);
        if (localBuscado != null) {
            localService.reactivarLocal(idLocal);
            return new ResponseEntity<>("Local reactivado satisfactoriamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Local no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<LocalVistaAdminDTO>> listarLocales() {
        List<LocalVistaAdminDTO> lista = localService.listarLocalesTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LocalDTO>> listarLocalesResumen() {
        List<LocalDTO> lista = localService.listarLocalesResumen();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalVistaAdminDTO> buscarLocalPorId(@PathVariable("id") Integer id) {
        LocalVistaAdminDTO local = localService.buscarLocalPorId(id);
        if (local != null)
            return new ResponseEntity<>(local, HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }


}

