package pe.edu.pucp.salud360.usuario.services;

import pe.edu.pucp.salud360.servicio.dto.ReservaDTO.ReservaDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteVistaAdminDTO;

import java.util.List;

public interface ClienteService {
    ClienteLogueadoDTO crearClientePorRegistro(ClienteRegistroDTO clienteDTO);
    ClienteVistaAdminDTO crearClienteVistaAdmin(ClienteRegistroDTO clienteDTO);

    ClienteLogueadoDTO actualizarClienteVistaPerfil(Integer idCliente, ClienteResumenDTO clienteDTO);
    ClienteVistaAdminDTO actualizarClienteVistaAdmin(Integer idCliente, ClienteResumenDTO clienteDTO);

    void eliminarCliente(Integer idCliente);
    void reactivarCliente(Integer idCliente);

    List<ClienteVistaAdminDTO> listarClientes();
    ClienteVistaAdminDTO buscarClientePorId(Integer idCliente);
    ClienteLogueadoDTO buscarClienteLogueadoPorId(Integer idCliente);

    List<ReservaDTO> listarReservasPorCliente(Integer idCliente);
}
