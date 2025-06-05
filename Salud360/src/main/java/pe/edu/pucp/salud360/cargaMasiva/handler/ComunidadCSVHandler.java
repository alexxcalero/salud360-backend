package pe.edu.pucp.salud360.cargaMasiva.handler;


import org.springframework.stereotype.Component;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;

import java.io.BufferedReader;
import java.time.LocalDateTime;

@Component
public class ComunidadCSVHandler implements CSVHandler {

    private final ComunidadService comunidadService;

    public ComunidadCSVHandler(ComunidadService comunidadService) {
        this.comunidadService = comunidadService;
    }

    @Override
    public void procesarCSV(BufferedReader reader) {
        reader.lines().skip(1).forEach(linea -> {
            String[] campos = linea.split(",");

            ComunidadDTO dto = ComunidadDTO.builder()
                    .nombre(campos[0].trim())
                    .descripcion(campos[1].trim())
                    .proposito(campos[2].trim())
                    .activo(true)
                    .fechaCreacion(LocalDateTime.now())
                    .build();

            comunidadService.crearComunidad(dto);
        });
    }
}

