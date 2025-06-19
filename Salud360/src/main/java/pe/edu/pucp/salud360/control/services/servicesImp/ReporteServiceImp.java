package pe.edu.pucp.salud360.control.services.servicesImp;

import org.jfree.data.general.DefaultPieDataset;
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
import pe.edu.pucp.salud360.membresia.models.Membresia;
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

import static java.util.Base64.getEncoder;

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
        ReporteDTO reporte = new ReporteDTO();
        reporte.setFechaCreacion(LocalDateTime.now());

        List<Cliente> usuarios = usuarioRepository.findAll();
        Map<String, Integer> conteoComunidadMembresia = new HashMap<>();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { font-family: Arial, sans-serif; padding: 20px; }");
        htmlBuilder.append("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        htmlBuilder.append("th, td { border: 1px solid #aaa; padding: 8px; text-align: left; }");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        htmlBuilder.append("<h2>Listado de usuarios y sus comunidades y membresías</h2>");
        htmlBuilder.append("<p>").append(filtro.getDescripcion()).append("</p>");

        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>IDUsuario</th><th>Nombre</th><th>Comunidad - Membresía</th></tr>");

        for (Cliente usuario : usuarios) {
            if (usuario.getFechaCreacion().isAfter(filtro.getFechaInicio().atStartOfDay()) &&
                    usuario.getFechaCreacion().isBefore(filtro.getFechaFin().atStartOfDay())) {

                List<Comunidad> comunidades = usuario.getComunidades();
                List<String> combinaciones = new ArrayList<>();

                for (Comunidad comunidad : comunidades) {
                    String nombreComunidad = comunidad.getNombre();
                    // Si hay membresías asociadas
                    for (Afiliacion af : usuario.getAfiliaciones()) {
                        if(Objects.equals(af.getMembresia().getComunidad().getIdComunidad(), comunidad.getIdComunidad())) {
                            String combinacion = STR."\{nombreComunidad} - \{af.getMembresia().getNombre()}";
                            combinaciones.add(combinacion);
                            conteoComunidadMembresia.merge(combinacion, 1, Integer::sum);
                        }
                    }
                }

                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(usuario.getIdCliente()).append("</td>");
                htmlBuilder.append("<td>").append(usuario.getNombres()).append("</td>");
                htmlBuilder.append("<td>").append(String.join(", ", combinaciones)).append("</td>");
                htmlBuilder.append("</tr>");
            }
        }

        htmlBuilder.append("</table>");

// Crear dataset del gráfico pastel
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : conteoComunidadMembresia.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de usuarios por comunidad y membresía",
                dataset,
                true, true, false
        );

// Convertir gráfico a base64
        String base64Image = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(baos, chart, 600, 400);
            byte[] bytes = baos.toByteArray();
            base64Image = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!base64Image.isEmpty()) {
            htmlBuilder.append("<h3>Distribución de usuarios por comunidad y membresía</h3>");
            htmlBuilder.append("<img src='data:image/png;base64,")
                    .append(base64Image)
                    .append("' width='600'/>");
        }

        htmlBuilder.append("</body></html>");

// Generar PDF
        String titulo = "Reporte de Usuarios por Comunidad y Membresía";
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
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { font-family: Arial, sans-serif; padding: 20px; }");
        htmlBuilder.append("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        htmlBuilder.append("th, td { border: 1px solid #aaa; padding: 8px; text-align: left; }");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        htmlBuilder.append("<h2>Listado de locales y sus servicios</h2>");
        htmlBuilder.append("<p>").append(filtro.getDescripcion()).append("</p>");

        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>IDLocal</th><th>Nombre</th><th>Servicios</th></tr>");

        for (Local local : locales) {
            Servicio servicio = local.getServicio();
            boolean filtros = Objects.equals(filtro.getIdServicio(), servicio.getIdServicio());
            if (filtro.getIdServicio() == null) filtros = true;

            if (servicio.getFechaCreacion().isAfter(filtro.getFechaInicio().atStartOfDay()) &&
                    servicio.getFechaCreacion().isBefore(filtro.getFechaFin().atStartOfDay()) && filtros) {

                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(local.getIdLocal()).append("</td>");
                htmlBuilder.append("<td>").append(local.getNombre()).append("</td>");
                htmlBuilder.append("<td>").append(servicio.getNombre()).append("</td>");
                htmlBuilder.append("</tr>");

                conteoServicios.merge(servicio.getNombre(), 1, Integer::sum);
            }
        }

        htmlBuilder.append("</table>");

// 👇 Generar gráfico como imagen base64
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : conteoServicios.entrySet()) {
            dataset.addValue(entry.getValue(), "Servicios", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Distribución de locales por servicio",
                "Servicio",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        String base64Image = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(baos, chart, 600, 400);
            byte[] bytes = baos.toByteArray();
            base64Image = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            base64Image = "";
        }
        if (!base64Image.isEmpty()) {
            htmlBuilder.append("<h3>Distribución de locales por servicio</h3>");
            htmlBuilder.append("<img src='data:image/png;base64,")
                    .append(base64Image)
                    .append("' width='600'/>");
        }

        htmlBuilder.append("</body></html>");

// 👉 Convertir HTML a PDF
        String titulo = "Reporte de Servicios con Gráfico Integrado";
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