package pe.edu.pucp.salud360.cargaMasiva.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class CargaMasivaController {

    @PostMapping("/cargar")
    public ResponseEntity<String> cargarArchivo(
            @RequestParam("entidad") String entidad,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        //cargaMasivaService.procesar(entidad, archivo);
        return ResponseEntity.ok("Archivo procesado exitosamente");
    }

}
