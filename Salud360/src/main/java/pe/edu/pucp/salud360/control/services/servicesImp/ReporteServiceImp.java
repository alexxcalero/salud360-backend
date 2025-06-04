package pe.edu.pucp.salud360.control.services.servicesImp;

import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.control.dto.*;
import pe.edu.pucp.salud360.control.services.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;

import pe.edu.pucp.salud360.control.mappers.ReporteMapper;
import pe.edu.pucp.salud360.control.models.Reporte;
import pe.edu.pucp.salud360.control.repositories.ReporteRepository;
import pe.edu.pucp.salud360.control.utils.ReportePDFGenerator;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.Pago;
import pe.edu.pucp.salud360.membresia.repositories.AfiliacionRepository;
import pe.edu.pucp.salud360.membresia.repositories.PagoRepository;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.LocalRepository;
import pe.edu.pucp.salud360.servicio.repositories.ServicioRepository;
import pe.edu.pucp.salud360.usuario.dtos.usuarioDTO.UsuarioResumenDTO;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.models.Usuario;
import pe.edu.pucp.salud360.usuario.repositories.ClienteRepository;
import pe.edu.pucp.salud360.usuario.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImp implements ReporteService {
    @Autowired
    private ClienteRepository usuarioRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private LocalRepository localRepository;

    @Override
    public ReporteDTO generarReporteUsuarios(ReporteUsuarioRequestDTO filtro) {
        // Aquí se arma el reporte usando el filtro
        ReporteDTO reporte = new ReporteDTO();
        reporte.setFechaCreacion(LocalDateTime.now());
        // Reemplaza con lógica real
        List<Cliente> usuarios = usuarioRepository.findAll();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<p>Listado de usuarios y sus comunidades:</p>");
        htmlBuilder.append("<p>").append(filtro.getDescripcion()).append("</p>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>IDUsuario</th><th>Nombre</th><th>Comunidades</th></tr>");
        for (Cliente usuario : usuarios) {
            List<Comunidad> comunidades = usuario.getComunidades();
            List<String> c = new ArrayList<>();
            for(Comunidad com : comunidades){
                c.add(com.getNombre());
            }
            if(usuario.getFechaCreacion().isAfter(filtro.getFechaInicio().atStartOfDay()) &&
                    usuario.getFechaCreacion().isBefore(filtro.getFechaFin().atStartOfDay())) {
                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(usuario.getIdCliente()).append("</td>");
                htmlBuilder.append("<td>").append(usuario.getNombres()).append("</td>");
                htmlBuilder.append("<td>").append(String.join("\n ", c)).append("</td>");
                htmlBuilder.append("</tr>");
            }
        }
        htmlBuilder.append("</table>");
        String titulo = "Reporte de Usuarios por Comunidad";
        String contenidoHTML = htmlBuilder.toString();
        byte[] pdfBytes = ReportePDFGenerator.generarReporteHTML(titulo, contenidoHTML);
        reporte.setPdf(pdfBytes);

        reporte.setIdAfiliaciones(Collections.singletonList(1));
        reporte.setIdPagos(Collections.singletonList(10));
        return reporte;
    }

    @Override
    public ReporteDTO generarReporteServicios(ReporteServicioRequestDTO filtro) {
        ReporteDTO reporte = new ReporteDTO();
        reporte.setFechaCreacion(LocalDateTime.now());

        List<Servicio> servicios = servicioRepository.findAll();
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<p>Listado de servicios:</p>");
        htmlBuilder.append("<p>").append(filtro.getDescripcion()).append("</p>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>IDServicios</th><th>Servicios</th><th>Comunidad</th></tr>");
        for (Servicio servicio : servicios) {
            List<Comunidad> comunidades = servicio.getComunidad();
            List<String> c = new ArrayList<>();
            for(Comunidad com : comunidades){
                c.add(com.getNombre());
            }
            boolean esLocal=false;
            for(Local local : servicio.getLocales()){
                if (Objects.equals(local.getIdLocal(), filtro.getIdLocal())) {
                    esLocal = true;
                    break;
                }
            }
            if(filtro.getIdLocal() == null) esLocal = true;
            if(servicio.getFechaCreacion().isAfter(filtro.getFechaInicio().atStartOfDay()) &&
                    servicio.getFechaCreacion().isBefore(filtro.getFechaFin().atStartOfDay()) && esLocal) {
                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(servicio.getIdServicio()).append("</td>");
                htmlBuilder.append("<td>").append(servicio.getNombre()).append("</td>");
                htmlBuilder.append("<td>").append(String.join("\n ", c)).append("</td>");
                htmlBuilder.append("</tr>");
            }
        }
        htmlBuilder.append("</table>");
        String titulo = "Reporte de Servicios";
        String contenidoHTML = htmlBuilder.toString();
        byte[] pdfBytes = ReportePDFGenerator.generarReporteHTML(titulo, contenidoHTML);
        reporte.setPdf(pdfBytes);

        reporte.setIdAfiliaciones(Collections.singletonList(2));
        reporte.setIdPagos(Collections.singletonList(11));
        return reporte;
    }

    @Override
    public ReporteDTO generarReporteLocales(ReporteLocalRequestDTO filtro) {
        ReporteDTO reporte = new ReporteDTO();
        reporte.setFechaCreacion(LocalDateTime.now());

        List<Local> locales = localRepository.findAll();
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<p>Listado de locales y sus servicios:</p>");
        htmlBuilder.append("<p>").append(filtro.getDescripcion()).append("</p>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>IDLocal</th><th>Nombre</th><th>Servicios</th></tr>");
        for (Local local : locales) {
            Servicio servicio = local.getServicio();
            boolean filtros = Objects.equals(filtro.getIdServicio(), servicio.getIdServicio());
            if(filtro.getIdServicio() == null) filtros = true;
            if(servicio.getFechaCreacion().isAfter(filtro.getFechaInicio().atStartOfDay()) &&
                    servicio.getFechaCreacion().isBefore(filtro.getFechaFin().atStartOfDay()) && filtros) {
                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(local.getIdLocal()).append("</td>");
                htmlBuilder.append("<td>").append(local.getNombre()).append("</td>");
                htmlBuilder.append("<td>").append(String.join("\n ", servicio.getNombre())).append("</td>");
                htmlBuilder.append("</tr>");
            }
        }
        htmlBuilder.append("</table>");
        String titulo = "Reporte de Servicios";
        String contenidoHTML = htmlBuilder.toString();
        byte[] pdfBytes = ReportePDFGenerator.generarReporteHTML(titulo, contenidoHTML);
        reporte.setPdf(pdfBytes);

        reporte.setIdAfiliaciones(Collections.singletonList(3));
        reporte.setIdPagos(Collections.singletonList(12));
        return reporte;
    }
}