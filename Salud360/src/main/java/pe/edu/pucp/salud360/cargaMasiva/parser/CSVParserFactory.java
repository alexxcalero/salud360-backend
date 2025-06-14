package pe.edu.pucp.salud360.cargaMasiva.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.pucp.salud360.cargaMasiva.handler.*;

@Component
@RequiredArgsConstructor
public class CSVParserFactory {

    private final ComunidadCSVHandler comunidadCSVHandler;
//    private final ServicioCSVHandler servicioCSVHandler;
//    private final LocalCSVHandler localCSVHandler;
    // Agrega más handlers según sea necesario

    public CSVHandler getHandler(String entidad) {
        return switch (entidad.toLowerCase()) {
            case "comunidad" -> comunidadCSVHandler;
//            case "servicio" -> servicioCSVHandler;
//            case "local" -> localCSVHandler;
            // Agrega más casos si creas más handlers
            default -> throw new IllegalArgumentException("Entidad no soportada: " + entidad);
        };
    }
}
