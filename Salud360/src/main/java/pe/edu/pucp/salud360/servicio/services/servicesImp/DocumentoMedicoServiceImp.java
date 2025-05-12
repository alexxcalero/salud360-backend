package pe.edu.pucp.salud360.servicio.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.servicio.dto.DocumentoMedicoDTO;
import pe.edu.pucp.salud360.servicio.mappers.DocumentoMedicoMapper;
import pe.edu.pucp.salud360.servicio.models.DocumentoMedico;
import pe.edu.pucp.salud360.servicio.repositories.DocumentoMedicoRepository;
import pe.edu.pucp.salud360.servicio.services.DocumentoMedicoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentoMedicoServiceImp implements DocumentoMedicoService {

    @Autowired
    private DocumentoMedicoRepository documentoRepository;

    @Autowired
    private DocumentoMedicoMapper documentoMapper;

    @Override
    public DocumentoMedicoDTO crearDocumentoMedico(DocumentoMedicoDTO dto) {
        DocumentoMedico documento = documentoMapper.mapToModel(dto);
        //documento.setFechaCreacion(LocalDate.now());
        documento.setActivo(true);
        return documentoMapper.mapToDTO(documentoRepository.save(documento));
    }

    @Override
    public DocumentoMedicoDTO actualizarDocumentoMedico(Integer id, DocumentoMedicoDTO dto) {
        Optional<DocumentoMedico> optional = documentoRepository.findById(id);
        if (optional.isEmpty()) return null;

        DocumentoMedico documento = optional.get();
        documento.setDocumento(dto.getDocumento());

        return documentoMapper.mapToDTO(documentoRepository.save(documento));
    }

    @Override
    public void eliminarDocumentoMedico(Integer id) {
        Optional<DocumentoMedico> optional = documentoRepository.findById(id);
        if (optional.isPresent()) {
            DocumentoMedico doc = optional.get();
            doc.setActivo(false);
            documentoRepository.save(doc);
        }
    }

    @Override
    public List<DocumentoMedicoDTO> listarDocumentosMedicosTodos() {
        return documentoRepository.findAll().stream()
                .filter(DocumentoMedico::getActivo)
                .map(documentoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentoMedicoDTO buscarDocumentoMedicoPorId(Integer id) {
        return documentoRepository.findById(id)
                .filter(DocumentoMedico::getActivo)
                .map(documentoMapper::mapToDTO)
                .orElse(null);
    }
}

