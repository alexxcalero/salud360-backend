package pe.edu.pucp.salud360.usuario.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.usuario.dtos.tipoDocumentoDTO.TipoDocumentoVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.models.TipoDocumento;
import pe.edu.pucp.salud360.usuario.repositories.TipoDocumentoRepository;
import pe.edu.pucp.salud360.usuario.services.TipoDocumentoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoDocumentoServiceImp implements TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoDocumentoMapper tipoDocumentoMapper;

    @Override
    public TipoDocumentoVistaAdminDTO crearTipoDocumento(TipoDocumentoVistaAdminDTO tipoDocumentoDTO) {
        TipoDocumento tipoDocumento = tipoDocumentoMapper.mapToModel(tipoDocumentoDTO);
        tipoDocumento.setActivo(true);
        tipoDocumento.setFechaCreacion(LocalDateTime.now());
        TipoDocumento tipoDocumentoCreado = tipoDocumentoRepository.save(tipoDocumento);
        return tipoDocumentoMapper.mapToVistaAdminDTO(tipoDocumentoCreado);
    }

    @Override
    public TipoDocumentoVistaAdminDTO actualizarTipoDocumento(Integer idTipoDocumento, TipoDocumentoVistaAdminDTO tipoDocumentoDTO) {
        Optional<TipoDocumento> tipoDocumentoSeleccionado = tipoDocumentoRepository.findById(idTipoDocumento);
        if(tipoDocumentoSeleccionado.isPresent()) {
            TipoDocumento tipoDocumento = tipoDocumentoSeleccionado.get();
            tipoDocumento.setNombre(tipoDocumentoDTO.getNombre());
            TipoDocumento tipoDocumentoActualizado = tipoDocumentoRepository.save(tipoDocumento);
            return tipoDocumentoMapper.mapToVistaAdminDTO(tipoDocumentoActualizado);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarTipoDocumento(Integer idTipoDocumento) {
        Optional<TipoDocumento> tipoDocumentoSeleccionado = tipoDocumentoRepository.findById(idTipoDocumento);
        if(tipoDocumentoSeleccionado.isPresent()) {
            TipoDocumento tipoDocumentoEliminar = tipoDocumentoSeleccionado.get();
            tipoDocumentoEliminar.setActivo(false);
            tipoDocumentoEliminar.setFechaDesactivacion(LocalDateTime.now());
            tipoDocumentoRepository.save(tipoDocumentoEliminar);
        }
    }

    @Override
    public void reactivarTipoDocumento(Integer idTipoDocumento) {
        Optional<TipoDocumento> tipoDocumentoSeleccionado = tipoDocumentoRepository.findById(idTipoDocumento);
        if(tipoDocumentoSeleccionado.isPresent()) {
            TipoDocumento tipoDocumentoReactivar = tipoDocumentoSeleccionado.get();
            tipoDocumentoReactivar.setActivo(true);
            tipoDocumentoReactivar.setFechaDesactivacion(null);
            tipoDocumentoRepository.save(tipoDocumentoReactivar);
        }
    }

    @Override
    public List<TipoDocumentoVistaAdminDTO> listarTiposDocumentos(String nombre) {
        List<TipoDocumento> tiposDocumentos;

        if(nombre == null || nombre.isBlank()) {
            tiposDocumentos = tipoDocumentoRepository.findAllByOrderByIdTipoDocumentoAsc();
        } else {
            tiposDocumentos = tipoDocumentoRepository.findAllByNombreContainingIgnoreCaseOrderByIdTipoDocumentoAsc(nombre);
        }

        if(!(tiposDocumentos.isEmpty())) {
            return tiposDocumentos.stream().map(tipoDocumentoMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public TipoDocumentoVistaAdminDTO buscarTipoDocumentoPorId(Integer idTipoDocumento) {
        Optional<TipoDocumento> tipoDocumentoBuscado = tipoDocumentoRepository.findById(idTipoDocumento);
        if(tipoDocumentoBuscado.isPresent()) {
            TipoDocumento tipoDocumento = tipoDocumentoBuscado.get();
            return tipoDocumentoMapper.mapToVistaAdminDTO(tipoDocumento);
        } else {
            return null;
        }
    }
}
