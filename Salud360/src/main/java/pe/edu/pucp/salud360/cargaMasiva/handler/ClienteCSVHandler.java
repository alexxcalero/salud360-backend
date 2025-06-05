package pe.edu.pucp.salud360.cargaMasiva.handler;

import org.springframework.stereotype.Component;
import pe.edu.pucp.salud360.cargaMasiva.handler.CSVHandler;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoResumenDTO;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class ClienteCSVHandler implements CSVHandler {

    private final ClienteService clienteService;

    public ClienteCSVHandler(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public void procesarCSV(BufferedReader reader) {
        reader.lines().skip(1).forEach(linea -> {
            String[] campos = linea.split(",");
            ClienteRegistroDTO dto = new ClienteRegistroDTO();
            dto.setNombres(campos[0].trim());
            dto.setApellidos(campos[1].trim());
            dto.setNumeroDocumento(campos[2].trim());
            dto.setSexo(campos[3].trim());
            dto.setTelefono(campos[4].trim());
            dto.setFechaNacimiento(LocalDate.parse(campos[5].trim()));
            dto.setDireccion(campos[6].trim());

            TipoDocumentoResumenDTO tipoDoc = new TipoDocumentoResumenDTO();
            tipoDoc.setIdTipoDocumento(1); // puedes parametrizar esto si quieres
            dto.setTipoDocumento(tipoDoc);

            dto.setCorreo(campos.length > 7 ? campos[7].trim() : "default@correo.com");
            dto.setContrasenha("12345678");

            clienteService.crearClientePorRegistro(dto);
        });
    }
}
