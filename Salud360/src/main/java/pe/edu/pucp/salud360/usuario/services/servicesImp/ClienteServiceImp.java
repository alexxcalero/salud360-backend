package pe.edu.pucp.salud360.usuario.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.servicio.mappers.ReservaMapper;
import pe.edu.pucp.salud360.servicio.models.Reserva;
import pe.edu.pucp.salud360.comunidad.dto.comunidad.ComunidadDTO;
import pe.edu.pucp.salud360.comunidad.mappers.ComunidadMapper;
import pe.edu.pucp.salud360.comunidad.repositories.ComunidadRepository;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioPerfilDTO;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteVistaAdminDTO;
import pe.edu.pucp.salud360.usuario.mappers.ClienteMapper;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.models.TipoDocumento;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.usuario.repositories.RolRepository;
import pe.edu.pucp.salud360.usuario.repositories.TipoDocumentoRepository;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;
import pe.edu.pucp.salud360.usuario.services.ClienteService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImp implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final UsuarioRepository usuarioRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReservaMapper reservaMapper;



    @Override
    @Transactional
    public ClienteLogueadoDTO crearClientePorRegistro(ClienteRegistroDTO clienteDTO) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(clienteDTO.getCorreo());
        usuario.setContrasenha(passwordEncoder.encode(clienteDTO.getContrasenha()));
        usuario.setRol(rolRepository.findById(2).orElseThrow(() -> new RuntimeException("No existe el rol")));
        usuario.setActivo(true);

        Cliente cliente = clienteMapper.mapToModel(clienteDTO);
        cliente.setNotificacionPorCorreo(false);
        cliente.setNotificacionPorSMS(false);
        cliente.setNotificacionPorWhatsApp(false);
        cliente.setActivo(true);
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setComunidades(new ArrayList<>());
        cliente.setAfiliaciones(new ArrayList<>());
        cliente.setReservas(new ArrayList<>());
        cliente.setClases(new ArrayList<>());
        cliente.setCitasMedicas(new ArrayList<>());
        cliente.setNotificaciones(new ArrayList<>());
        cliente.setMediosDePago(new ArrayList<>());
        cliente.setTestimonios(new ArrayList<>());

        usuario.setCliente(cliente);
        cliente.setUsuario(usuario);

        Usuario usuarioCreado = usuarioRepository.save(usuario);
        Cliente clienteCreado = clienteRepository.save(cliente);

        return clienteMapper.mapToLogueadoDTO(clienteCreado);
    }

    @Override
    @Transactional
    public ClienteVistaAdminDTO crearClienteVistaAdmin(ClienteRegistroDTO clienteDTO) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(clienteDTO.getCorreo());
        usuario.setContrasenha(passwordEncoder.encode(clienteDTO.getContrasenha()));
        usuario.setRol(rolRepository.findById(2).orElseThrow(() -> new RuntimeException("No existe el rol")));
        usuario.setActivo(true);

        Cliente cliente = clienteMapper.mapToModel(clienteDTO);
        cliente.setNotificacionPorCorreo(false);
        cliente.setNotificacionPorSMS(false);
        cliente.setNotificacionPorWhatsApp(false);
        cliente.setActivo(true);
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setComunidades(new ArrayList<>());
        cliente.setAfiliaciones(new ArrayList<>());
        cliente.setReservas(new ArrayList<>());
        cliente.setClases(new ArrayList<>());
        cliente.setCitasMedicas(new ArrayList<>());
        cliente.setNotificaciones(new ArrayList<>());
        cliente.setMediosDePago(new ArrayList<>());
        cliente.setTestimonios(new ArrayList<>());

        usuario.setCliente(cliente);
        cliente.setUsuario(usuario);

        Usuario usuarioCreado = usuarioRepository.save(usuario);
        Cliente clienteCreado = clienteRepository.save(cliente);

        return clienteMapper.mapToVistaAdminDTO(clienteCreado);
    }

    @Override
    @Transactional
    public ClienteLogueadoDTO actualizarClienteVistaPer(Integer id, UsuarioPerfilDTO dto) {
        Cliente cliente = clienteRepository.findByUsuario_IdUsuario(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
//Crear un findbyID que busque por el id del usuario pero retorne un cliente.
        System.out.println("📥 Teléfono: " + dto.getTelefono());
        System.out.println("📥 Dirección: " + dto.getDireccion());
        System.out.println("📥 ID tipo doc: " + dto.getIdTipoDocumento());

        cliente.setNombres(dto.getNombre());
        cliente.setApellidos(dto.getApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        cliente.setSexo(dto.getSexo());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        
        if (dto.getIdTipoDocumento() == null) {
            throw new RuntimeException("El ID del tipo de documento no puede ser nulo");
        }

        TipoDocumento tipo = tipoDocumentoRepository.findById(dto.getIdTipoDocumento())
        .orElseThrow(() -> new RuntimeException("Tipo de documento con id " + dto.getIdTipoDocumento() + " no encontrado"));

        cliente.setTipoDocumento(tipo);

        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.mapToLogueadoDTO(actualizado);
    }

    @Override
    public ClienteVistaAdminDTO actualizarClienteVistaAdmin(Integer idCliente, ClienteResumenDTO clienteDTO) {
        Optional<Cliente> clienteSeleccionado = clienteRepository.findById(idCliente);
        if(clienteSeleccionado.isPresent()) {
            Cliente cliente = clienteSeleccionado.get();
            cliente.setNombres(clienteDTO.getNombres());
            cliente.setApellidos(clienteDTO.getApellidos());
            cliente.setNumeroDocumento(clienteDTO.getNumeroDocumento());
            cliente.setSexo(clienteDTO.getSexo());
            cliente.setTelefono(clienteDTO.getTelefono());
            cliente.setFechaNacimiento(clienteDTO.getFechaNacimiento());
            cliente.setDireccion(clienteDTO.getDireccion());
            cliente.setFotoPerfil(clienteDTO.getFotoPerfil());
            cliente.setNotificacionPorCorreo(clienteDTO.getNotificacionPorCorreo());
            cliente.setNotificacionPorSMS(clienteDTO.getNotificacionPorSMS());
            cliente.setNotificacionPorWhatsApp(clienteDTO.getNotificacionPorWhatsApp());
            cliente.setTipoDocumento(tipoDocumentoMapper.mapToModel(clienteDTO.getTipoDocumento()));

            cliente.getUsuario().setCorreo(clienteDTO.getCorreo());


            Cliente clienteActualizado = clienteRepository.save(cliente);
            return clienteMapper.mapToVistaAdminDTO(clienteActualizado);
        } else {
            return null;
        }
    }


    @Override
    public void eliminarCliente(Integer idCliente) {
        Optional<Cliente> clienteSeleccionado = clienteRepository.findById(idCliente);
        if(clienteSeleccionado.isPresent()) {
            Cliente clienteEliminar = clienteSeleccionado.get();
            clienteEliminar.setActivo(false);
            clienteEliminar.setFechaDesactivacion(LocalDateTime.now());
            clienteRepository.save(clienteEliminar);
        }
    }

    @Override
    public void reactivarCliente(Integer idCliente) {
        Optional<Cliente> clienteSeleccionado = clienteRepository.findById(idCliente);
        if(clienteSeleccionado.isPresent()) {
            Cliente clienteReactivar = clienteSeleccionado.get();
            clienteReactivar.setActivo(true);
            clienteReactivar.setFechaDesactivacion(null);
            clienteRepository.save(clienteReactivar);
        }
    }

    @Override
    public List<ClienteVistaAdminDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        if(!(clientes.isEmpty())) {
            return clientes.stream().map(clienteMapper::mapToVistaAdminDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ClienteVistaAdminDTO buscarClientePorId(Integer idCliente) {
        Optional<Cliente> clienteBuscado = clienteRepository.findById(idCliente);
        if(clienteBuscado.isPresent()) {
            Cliente cliente = clienteBuscado.get();
            return clienteMapper.mapToVistaAdminDTO(cliente);
        } else {
            return null;
        }
    }

    @Override
    public ClienteLogueadoDTO buscarClienteLogueadoPorId(Integer idCliente) {
        Optional<Cliente> clienteBuscado = clienteRepository.findById(idCliente);
        if(clienteBuscado.isPresent()) {
            Cliente cliente = clienteBuscado.get();
            return clienteMapper.mapToLogueadoDTO(cliente);
        } else {
            return null;
        }
    }
  
    @Override
    public List<ReservaDTO> listarReservasPorCliente(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrada"));

        List<Reserva> reservas = cliente.getReservas();

        return reservas.stream()
                .map(reservaMapper::mapToDTO)
                .toList();
    }
}
