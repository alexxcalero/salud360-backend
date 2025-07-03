package pe.edu.pucp.salud360.membresia.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;

public interface PagoService {
    List<PagoDTO> listarPagos();
    List<PagoDTO> obtenerPagosPorCliente(Integer idCliente);
    PagoDTO obtenerPagoPorId(Integer id);
    PagoDTO crearPago(PagoDTO pagoDTO);
    PagoDTO actualizarPago(Integer id, PagoDTO pagoDTO);
    void eliminarPago(Integer id);
    Boolean cargarMasivamantePago(MultipartFile file) throws IOException;
}