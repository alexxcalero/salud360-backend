package pe.edu.pucp.salud360.cargaMasiva.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.cargaMasiva.handler.CSVHandler;
import pe.edu.pucp.salud360.cargaMasiva.handler.ClienteCSVHandler;
import pe.edu.pucp.salud360.cargaMasiva.handler.ComunidadCSVHandler;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.usuario.services.ClienteService;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.comunidad.services.ComunidadService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class CargaMasivaService {

    private final Map<String, CSVHandler> handlers = new HashMap<>();

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ComunidadService comunidadService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ComunidadRepository comunidadRepository;

    @PostConstruct
    public void init() {
        // Registrar handlers disponibles
        handlers.put("cliente", new ClienteCSVHandler(clienteService));
        handlers.put("comunidad", new ComunidadCSVHandler(comunidadService));
    }

    public void procesar(String entidad, MultipartFile archivo) {
        CSVHandler handler = handlers.get(entidad.toLowerCase());
        if (handler == null) {
            throw new IllegalArgumentException("Entidad no soportada: " + entidad);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            handler.procesarCSV(reader);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el archivo CSV: " + e.getMessage(), e);
        }
    }
}