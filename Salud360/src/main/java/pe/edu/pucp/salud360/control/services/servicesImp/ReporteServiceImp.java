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
import pe.edu.pucp.salud360.membresia.dtos.PagoDTO;
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

import java.util.*;
import java.time.LocalDateTime;
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
        Map<String, Integer> conteoServicios = new HashMap<>();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head>");
        htmlBuilder.append("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
        htmlBuilder.append("</head><body>");

        htmlBuilder.append("<p>Listado de locales y sus servicios:</p>");
        htmlBuilder.append("<p>").append(filtro.getDescripcion()).append("</p>");

        htmlBuilder.append("<table border='1'>");
        htmlBuilder.append("<tr><th>IDLocal</th><th>Nombre</th><th>Servicios</th></tr>");

        for (Local local : locales) {
            Servicio servicio = local.getServicio();
            boolean filtros = Objects.equals(filtro.getIdServicio(), servicio.getIdServicio());
            if (filtro.getIdServicio() == null) filtros = true;

            if (servicio.getFechaCreacion().isAfter(filtro.getFechaInicio().atStartOfDay()) &&
                    servicio.getFechaCreacion().isBefore(filtro.getFechaFin().atStartOfDay()) &&
                    filtros) {

                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(local.getIdLocal()).append("</td>");
                htmlBuilder.append("<td>").append(local.getNombre()).append("</td>");
                htmlBuilder.append("<td>").append(servicio.getNombre()).append("</td>");
                htmlBuilder.append("</tr>");

                // Acumular para el gráfico
                conteoServicios.merge(servicio.getNombre(), 1, Integer::sum);
            }
        }

        htmlBuilder.append("</table>");

// Preparar datos JS para el gráfico
        StringBuilder labels = new StringBuilder();
        StringBuilder values = new StringBuilder();

        labels.append("[");
        values.append("[");

        for (Map.Entry<String, Integer> entry : conteoServicios.entrySet()) {
            labels.append("'").append(entry.getKey()).append("',");
            values.append(entry.getValue()).append(",");
        }

        if (!conteoServicios.isEmpty()) {
            labels.setLength(labels.length() - 1); // quitar última coma
            values.setLength(values.length() - 1);
        }

        labels.append("]");
        values.append("]");

// Gráfico de barras
        htmlBuilder.append("<h3>Distribución de locales por servicio</h3>");
        htmlBuilder.append("<canvas id='graficoServicios' width='400' height='200'></canvas>");

        htmlBuilder.append("<script>");
        htmlBuilder.append("const ctx = document.getElementById('graficoServicios').getContext('2d');");
        htmlBuilder.append("new Chart(ctx, {");
        htmlBuilder.append("  type: 'bar',");
        htmlBuilder.append("  data: {");
        htmlBuilder.append("    labels: ").append(labels).append(",");
        htmlBuilder.append("    datasets: [{");
        htmlBuilder.append("      label: 'Cantidad de locales',");
        htmlBuilder.append("      data: ").append(values).append(",");
        htmlBuilder.append("      backgroundColor: 'rgba(54, 162, 235, 0.6)',");
        htmlBuilder.append("      borderColor: 'rgba(54, 162, 235, 1)',");
        htmlBuilder.append("      borderWidth: 1");
        htmlBuilder.append("    }]");
        htmlBuilder.append("  },");
        htmlBuilder.append("  options: {");
        htmlBuilder.append("    scales: { y: { beginAtZero: true } }");
        htmlBuilder.append("  }");
        htmlBuilder.append("});");
        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

// Generar PDF con el contenido completo
        String titulo = "Reporte de Servicios con Dashboard";
        String contenidoHTML = htmlBuilder.toString();
        byte[] pdfBytes = ReportePDFGenerator.generarReporteHTML(titulo, contenidoHTML);
        reporte.setPdf(pdfBytes);

        reporte.setIdAfiliaciones(Collections.singletonList(3));
        reporte.setIdPagos(Collections.singletonList(12));
        return reporte;
    }

    @Override
    public ReporteDTO generarBoleta(PagoDTO pago){
        ReporteDTO reporte = new ReporteDTO();
        reporte.setFechaCreacion(LocalDateTime.now());
        String titulo = "BOLETA DE PAGO - SALUD 360";
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<p><strong>RUC:</strong> 20451234567</p>");
        htmlBuilder.append("<p><strong>RAZÓN SOCIAL:</strong> SERVICIOS MÉDICOS SALUD 360 S.A.C.</p>");
        htmlBuilder.append("<p><strong>DIRECCIÓN:</strong> AV. SALUD Y BIENESTAR 123 – LIMA – PERÚ</p>");
        htmlBuilder.append("<p><strong>BOLETA:</strong> B001-").append(pago.getIdPago()).append("</p>");
        htmlBuilder.append("<p><strong>FECHA:</strong> ").append(pago.getFechaPago().toLocalDate()).append("</p>");
        htmlBuilder.append("<p><strong>HORA:</strong> ").append(pago.getFechaPago().toLocalTime().withNano(0)).append("</p>");
        htmlBuilder.append("<p><strong>ID AFILIACIÓN:</strong> ").append(pago.getIdAfiliacion()).append("</p>");
        htmlBuilder.append("<p><strong>MEDIO DE PAGO:</strong> ").append(pago.getMedioDePago().getNcuenta(), 0, 4).append("****</p>");

        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>Cant</th><th>Descripción</th><th>Precio Unit</th><th>Total</th></tr>");
        htmlBuilder.append("<tr>");
        htmlBuilder.append("<td>1</td>");
        htmlBuilder.append("<td>Pago por servicio</td>");
        htmlBuilder.append("<td>S/ ").append(String.format("%.2f", pago.getMonto())).append("</td>");
        htmlBuilder.append("<td>S/ ").append(String.format("%.2f", pago.getMonto())).append("</td>");
        htmlBuilder.append("</tr>");
        htmlBuilder.append("</table>");

        double opGravadas = pago.getMonto() / 1.18;
        double igv = pago.getMonto() - opGravadas;

        htmlBuilder.append("<p><strong>OP. GRAVADAS:</strong> S/ ").append(String.format("%.2f", opGravadas)).append("</p>");
        htmlBuilder.append("<p><strong>IGV (18%):</strong> S/ ").append(String.format("%.2f", igv)).append("</p>");
        htmlBuilder.append("<p><strong>TOTAL:</strong> S/ ").append(String.format("%.2f", pago.getMonto())).append("</p>");

        String contenidoHTML = htmlBuilder.toString();
        byte[] pdfBytes = ReportePDFGenerator.generarReporteHTML(titulo, contenidoHTML);
        reporte.setPdf(pdfBytes);
        return reporte;
    }
}