package pe.edu.pucp.salud360.usuario.services.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;
import pe.edu.pucp.salud360.mutex.MutexRegistro;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorLogueadoDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorRegistroDTO;
import pe.edu.pucp.salud360.usuario.dtos.administradorDTO.AdministradorResumenDTO;
import pe.edu.pucp.salud360.usuario.dtos.clienteDTO.ClienteResumenDTO;
import pe.edu.pucp.salud360.usuario.mappers.AdministradorMapper;
import pe.edu.pucp.salud360.usuario.mappers.TipoDocumentoMapper;
import pe.edu.pucp.salud360.usuario.models.Administrador;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.AdministradorRepository;
import pe.edu.pucp.salud360.usuario.repositories.RolRepository;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;
import pe.edu.pucp.salud360.usuario.services.AdministradorService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImp implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final AdministradorMapper administradorMapper;
    private final UsuarioRepository usuarioRepository;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private S3UrlGenerator s3UrlGenerator;

    @Override
    @Transactional
    public AdministradorResumenDTO crearAdministrador(AdministradorRegistroDTO administradorDTO) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(administradorDTO.getCorreo());
        usuario.setContrasenha(passwordEncoder.encode(administradorDTO.getContrasenha()));
        usuario.setRol(rolRepository.findById(1).orElseThrow(() -> new RuntimeException("No existe el rol")));
        usuario.setActivo(true);

        Administrador administrador = administradorMapper.mapToModel(administradorDTO);
        administrador.setNotificacionPorCorreo(false);
        administrador.setNotificacionPorSMS(false);
        administrador.setNotificacionPorWhatsApp(false);
        administrador.setActivo(true);
        administrador.setFechaCreacion(LocalDateTime.now());

        usuario.setAdministrador(administrador);
        administrador.setUsuario(usuario);

        Administrador administradorCreado;
        synchronized (MutexRegistro.LOCK) {
            Usuario usuarioCreado = usuarioRepository.save(usuario);
            administradorCreado = administradorRepository.save(administrador);
        }

        return administradorMapper.mapToResumenDTO(administradorCreado);
    }

    @Override
    public AdministradorLogueadoDTO actualizarAdministrador(Integer idAdministrador, AdministradorLogueadoDTO administradorDTO) {
        Optional<Administrador> administradorSeleccionado = administradorRepository.findById(idAdministrador);
        if(administradorSeleccionado.isPresent()) {
            Administrador administrador = administradorSeleccionado.get();
            administrador.setNombres(administradorDTO.getNombres());
            administrador.setApellidos(administradorDTO.getApellidos());
            administrador.setNumeroDocumento(administradorDTO.getNumeroDocumento());
            administrador.setSexo(administradorDTO.getSexo());
            administrador.setTelefono(administradorDTO.getTelefono());
            administrador.setFotoPerfil(administradorDTO.getFotoPerfil());
            administrador.setNotificacionPorCorreo(administradorDTO.getNotificacionPorCorreo());
            administrador.setNotificacionPorSMS(administradorDTO.getNotificacionPorSMS());
            administrador.setNotificacionPorWhatsApp(administradorDTO.getNotificacionPorWhatsApp());
            administrador.setTipoDocumento(tipoDocumentoMapper.mapToModel(administradorDTO.getTipoDocumento()));

            administrador.getUsuario().setCorreo(administradorDTO.getCorreo());

            Administrador administradorActualizado = administradorRepository.save(administrador);
            return administradorMapper.mapToLogueadoDTO(administradorActualizado);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarAdministrador(Integer idAdministrador) {
        Optional<Administrador> administradorSeleccionado = administradorRepository.findById(idAdministrador);
        if(administradorSeleccionado.isPresent()) {
            Administrador administradorEliminar = administradorSeleccionado.get();
            administradorEliminar.setActivo(false);
            administradorEliminar.setFechaDesactivacion(LocalDateTime.now());
            administradorRepository.save(administradorEliminar);
        }
    }

    @Override
    public void reactivarAdministrador(Integer idAdministrador) {
        Optional<Administrador> administradorSeleccionado = administradorRepository.findById(idAdministrador);
        if(administradorSeleccionado.isPresent()) {
            Administrador administradorReactivar = administradorSeleccionado.get();
            administradorReactivar.setActivo(true);
            administradorReactivar.setFechaDesactivacion(null);
            administradorRepository.save(administradorReactivar);
        }
    }

    @Override
    public List<AdministradorResumenDTO> listarAdministradores() {
        List<Administrador> administradores = administradorRepository.findAll();
        if(!(administradores.isEmpty())) {
            return administradores.stream().map(administradorMapper::mapToResumenDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public AdministradorResumenDTO buscarAdministradorPorId(Integer idAdministrador) {
        Optional<Administrador> administradorBuscado = administradorRepository.findById(idAdministrador);
        if(administradorBuscado.isPresent()) {
            Administrador administrador = administradorBuscado.get();
            return administradorMapper.mapToResumenDTO(administrador);
        } else {
            return null;
        }
    }

    @Override
    public AdministradorLogueadoDTO buscarAdminPorId(Integer idAdministrador) {
        Optional<Administrador> administradorBuscado = administradorRepository.findById(idAdministrador);
        if(administradorBuscado.isPresent()) {
            Administrador administrador = administradorBuscado.get();
            return administradorMapper.mapToLogueadoDTO(administrador);
        } else {
            return null;
        }
    }

    @Override
    public AdministradorResumenDTO cambiarFotoPerfil(Integer idAdmin, String file){
        Optional<Administrador> adminBuscado = administradorRepository.findById(idAdmin);
        if (adminBuscado.isPresent()) {
            Administrador usuario = adminBuscado.get();
            String url = s3UrlGenerator.generarUrl(file); //Genera urls
            String key = s3UrlGenerator.extraerKeyDeUrl(url); //Saca la key del archivo
            usuario.setFotoPerfil(key);
            Administrador adminActualizado = administradorRepository.save(usuario);
            adminActualizado.setFotoPerfil(url);
            return administradorMapper.mapToResumenDTO(adminActualizado);
        } else {
            return null;
        }
    }
}
