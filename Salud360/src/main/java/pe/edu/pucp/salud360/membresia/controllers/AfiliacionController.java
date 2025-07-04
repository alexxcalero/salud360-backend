package pe.edu.pucp.salud360.membresia.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadResumenDTO;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenDTO;
import pe.edu.pucp.salud360.membresia.dtos.afiliacion.AfiliacionResumenResumenDTO;
import pe.edu.pucp.salud360.membresia.dtos.membresia.MembresiaResumenDTO;
import pe.edu.pucp.salud360.membresia.services.AfiliacionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/afiliaciones")
public class AfiliacionController {

    @Autowired
    private AfiliacionService afiliacionService;
    @Autowired
    private ComunidadService comunidadService;

    @PostMapping
    public ResponseEntity<AfiliacionResumenDTO> crearAfiliacion(@RequestBody AfiliacionDTO dto) {
        return ResponseEntity.ok(afiliacionService.crearAfiliacion(dto));
        //a
    }

    @PutMapping("/{id}/suspender")
    public ResponseEntity<?> suspenderAfiliacion(@PathVariable Integer id, @RequestParam int dias) {
        return ResponseEntity.ok(afiliacionService.desafiliar(id, dias));
    }

    @GetMapping
    public ResponseEntity<List<AfiliacionResumenResumenDTO>> listarAfiliaciones() {
        List<AfiliacionResumenDTO>zzz = afiliacionService.listarAfiliaciones();
        List<AfiliacionResumenResumenDTO> resumenes = new ArrayList<>();
        List<ComunidadDTO> comunidades = comunidadService.listarComunidades();
        for (AfiliacionResumenDTO af : zzz){
            AfiliacionResumenResumenDTO af2 = new AfiliacionResumenResumenDTO();
            af2.setFechaAfiliacion(af.getFechaAfiliacion());
            af2.setIdAfiliacion(af.getIdAfiliacion());
            af2.setMembresia(af.getMembresia());
            af2.setEstado(af.getEstado());
            af2.setFechaDesafiliacion(af.getFechaDesafiliacion());
            Integer comunidad = 0;
            boolean flag = false;
            for(ComunidadDTO c : comunidades) {
                List<MembresiaResumenDTO> m = c.getMembresias();
                for(MembresiaResumenDTO mem : m){
                    if(Objects.equals(mem.getIdMembresia(), af.getMembresia().getIdMembresia())){
                        comunidad = c.getIdComunidad();
                        flag = true;
                        break;
                    }
                }
                if(flag) break;
            }
            af2.setIdComunidad(comunidad);
            resumenes.add(af2);
        }
        return ResponseEntity.ok(resumenes);
        //return ResponseEntity.ok(afiliacionService.listarAfiliaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AfiliacionResumenDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(afiliacionService.buscarAfiliacionPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AfiliacionResumenDTO> actualizarAfiliacion(@PathVariable Integer id, @RequestBody AfiliacionDTO dto) {
        return ResponseEntity.ok(afiliacionService.actualizarAfiliacion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminarAfiliacion(@PathVariable Integer id) {
        return ResponseEntity.ok(afiliacionService.eliminarAfiliacion(id));
    }

    //@PutMapping("/{id}/suspender")
    //public ResponseEntity<Boolean> suspenderAfiliacion(@PathVariable Integer id) {
    //    return ResponseEntity.ok(afiliacionService.desafiliar(id));
    //}

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<Boolean> reactivarAfiliacion(@PathVariable Integer id) {
        return ResponseEntity.ok(afiliacionService.reactivarAfiliacion(id));
    }

    @PostMapping("/cargaMasiva")
    public String cargaMasivaAfiliaciones(@RequestParam("file") MultipartFile file) throws Exception {
        Boolean cargado = afiliacionService.cargarMasivamanteAfiliacion(file);
        if (cargado) {
            return "Archivo cargado satisfactoriamente";
        } else {
            return "Archivo no cargado";
        }
    }

}

