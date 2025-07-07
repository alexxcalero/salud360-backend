package pe.edu.pucp.salud360.membresia.services.servicesImp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoDTO;
import pe.edu.pucp.salud360.membresia.dtos.mediopago.MedioDePagoResumenDTO;
import pe.edu.pucp.salud360.membresia.mappers.MedioDePagoMapper;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;
import pe.edu.pucp.salud360.membresia.repositories.MedioDePagoRepository;
import pe.edu.pucp.salud360.membresia.services.MedioDePagoService;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class MedioDePagoServiceImp implements MedioDePagoService {

    @Autowired
    private MedioDePagoRepository medioDePagoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MedioDePagoMapper medioDePagoMapper;

    @Override
    public List<MedioDePagoResumenDTO> listar() {
        return medioDePagoRepository.findAll().stream()
                .map(medioDePagoMapper::mapToMedioDePagoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedioDePagoResumenDTO> listarPorUsuario(Integer idUsuario) {
        // Obtener todos los métodos de pago asociados al usuario con el id proporcionado
        List<MedioDePago> mediosDePago = medioDePagoRepository.findByCliente_IdCliente(idUsuario);
        return mediosDePago.stream()
            .map(medioDePagoMapper::mapToMedioDePagoDTO)
            .filter(m -> m.getTipo().contains("tarjeta"))
            .collect(Collectors.toList());
    }
    

    @Override
    public MedioDePagoDTO crear(MedioDePagoDTO dto) {
        // Mapear el DTO al modelo
        MedioDePago medioDePago = medioDePagoMapper.mapToModel(dto);

        // Buscar el usuario por el id_cliente
        Cliente cliente = clienteRepository.findById(dto.getUsuario().getIdUsuario()).orElse(null);
        
        if (cliente == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Asignar el usuario al medio de pago
        medioDePago.setCliente(cliente);
        medioDePago.setActivo(true);
        medioDePago.setTotalGastado(0.0);
        // Asignar la fecha de creación
        medioDePago.setFechaCreacion(LocalDateTime.now());

        // Guardar el medio de pago en la base de datos
        medioDePago = medioDePagoRepository.save(medioDePago);

        return medioDePagoMapper.mapToDTO(medioDePago);
    }

    @Override
    public MedioDePagoResumenDTO obtenerPorId(Integer id) {
        return medioDePagoRepository.findById(id)
                .map(medioDePagoMapper::mapToMedioDePagoDTO)
                .orElse(null);
    }

    @Override
    public boolean eliminar(Integer id) {
        if (medioDePagoRepository.existsById(id)) {
            medioDePagoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public MedioDePagoDTO actualizar(Integer id, MedioDePagoDTO dto) {
        if (!medioDePagoRepository.existsById(id)) return null;
        Cliente cliente = clienteRepository.findById(dto.getUsuario().getIdUsuario()).orElse(null);
        dto.setIdMedioDePago(id);
        MedioDePago m = medioDePagoMapper.mapToModel(dto);
        return medioDePagoMapper.mapToDTO(medioDePagoRepository.save(m));
    }

    @Override
    public Boolean verificarDatosSensibles(MedioDePagoDTO dto) {
        if(!medioDePagoRepository.existsById(dto.getIdMedioDePago())) return true;
        Optional<MedioDePago> m = medioDePagoRepository.findById(dto.getIdMedioDePago());

        if(m.isEmpty()) return false;

        MedioDePago m2 = medioDePagoMapper.mapToModel(dto);
        return
                m.get().getCvv() == m2.getCvv() &&
                        m.get().getVencimiento().getMonth() == m2.getVencimiento().getMonth() &&
                        m.get().getVencimiento().getYear() == m2.getVencimiento().getYear();
    }
}

