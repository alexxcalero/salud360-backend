package pe.edu.pucp.salud360.awsS3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.awsS3.S3Service;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/archivo")
public class ArchivoController {

    @Autowired
    private S3Service s3Service;
    @Autowired
    private S3UrlGenerator s3UrlGenerator;

    @PostMapping
    public ResponseEntity<Map<String, Object>> subirArchivo(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Archivo vacío"));
        }

        String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        String url = s3Service.subirArchivo(nombreArchivo, archivo);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Archivo subido exitosamente",
                "url", url,
                "nombreArchivo", nombreArchivo
        ));
    }

    //Para la descarga en el admion
    @GetMapping("/descargar/{nombreArchivo}")
    public ResponseEntity<Map<String, Object>> descargarArchivo(@PathVariable String nombreArchivo) {
        try {
            String urlTemporal = s3UrlGenerator.generarUrlLectura(nombreArchivo);
            return ResponseEntity.ok(Map.of("url", urlTemporal));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "No se pudo generar la URL de descarga"));
        }
    }
}
