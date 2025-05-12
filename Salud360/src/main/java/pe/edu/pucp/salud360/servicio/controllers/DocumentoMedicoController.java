package pe.edu.pucp.salud360.servicio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.salud360.servicio.dto.DocumentoMedicoDTO;
import pe.edu.pucp.salud360.servicio.services.DocumentoMedicoService;

import java.util.List;

@RestController
@RequestMapping("/api/documentos-medicos")
public class DocumentoMedicoController {

    @Autowired
    private DocumentoMedicoService documentoMedicoService;

    @PostMapping
    public ResponseEntity<DocumentoMedicoDTO> crearDocumento(@RequestBody DocumentoMedicoDTO dto) {
        DocumentoMedicoDTO creado = documentoMedicoService.crearDocumentoMedico(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoMedicoDTO> actualizarDocumento(@PathVariable("id") Integer id,
                                                                  @RequestBody DocumentoMedicoDTO dto) {
        DocumentoMedicoDTO existente = documentoMedicoService.buscarDocumentoMedicoPorId(id);
        if (existente != null) {
            DocumentoMedicoDTO actualizado = documentoMedicoService.actualizarDocumentoMedico(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDocumento(@PathVariable("id") Integer id) {
        DocumentoMedicoDTO existente = documentoMedicoService.buscarDocumentoMedicoPorId(id);
        if (existente != null) {
            documentoMedicoService.eliminarDocumentoMedico(id);
            return new ResponseEntity<>("Documento eliminado correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Documento no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<DocumentoMedicoDTO>> listarDocumentos() {
        List<DocumentoMedicoDTO> lista = documentoMedicoService.listarDocumentosMedicosTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoMedicoDTO> buscarPorId(@PathVariable("id") Integer id) {
        DocumentoMedicoDTO documento = documentoMedicoService.buscarDocumentoMedicoPorId(id);
        if (documento != null)
            return new ResponseEntity<>(documento, HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }
}

