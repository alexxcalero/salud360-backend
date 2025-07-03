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
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.models.Local;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.servicio.repositories.CitaMedicaRepository;
import pe.edu.pucp.salud360.servicio.repositories.ClaseRepository;
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
    @Autowired
    private CitaMedicaRepository citaMedicaRepository;
    @Autowired
    private ClaseRepository claseRepository;

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

        htmlBuilder.append("<h3>Resumen de usuarios por comunidad y membresía</h3>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>Comunidad - Membresía</th><th>Total Usuarios</th></tr>");

        for (Map.Entry<String, Integer> entry : conteoComunidadMembresia.entrySet()) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(entry.getKey()).append("</td>");
            htmlBuilder.append("<td>").append(entry.getValue()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");

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

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { font-family: Arial, sans-serif; padding: 20px; }");
        htmlBuilder.append("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        htmlBuilder.append("th, td { border: 1px solid #aaa; padding: 8px; text-align: left; }");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        htmlBuilder.append("<h2>Reporte global de reservas (citas médicas y clases)</h2>");
        htmlBuilder.append("<p>").append(filtro.getDescripcion()).append("</p>");

        //Dataset para gráfico
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        //Recorrer TODAS las clases
        List<Clase> clases = claseRepository.findAll();
        for (Clase clase : clases) {
            int cantidad = clase.getReservas() != null ? clase.getReservas().size() : 0;
            if (cantidad > 0) {
                pieDataset.setValue("Clase: " + clase.getNombre(), cantidad);
            }
        }

        //Recorrer TODAS las citas médicas
        List<CitaMedica> citas = citaMedicaRepository.findAll();
        for (CitaMedica cita : citas) {
            // Asumiendo que cada cita médica representa una reserva individual
            pieDataset.setValue("Cita: " + cita.getMedico().getEspecialidad(), 1);  // O usar alguna propiedad relevante
        }

        //Crear gráfico circular
        String base64PieChart = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JFreeChart pieChart = ChartFactory.createPieChart(
                    "Distribución de reservas por tipo",
                    pieDataset,
                    true, true, false
            );
            ChartUtils.writeChartAsPNG(baos, pieChart, 700, 450);
            byte[] bytes = baos.toByteArray();
            base64PieChart = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!base64PieChart.isEmpty()) {
            htmlBuilder.append("<h3>Distribución de reservas</h3>");
            htmlBuilder.append("<img src='data:image/png;base64,")
                    .append(base64PieChart)
                    .append("' width='700'/>");
        }

        htmlBuilder.append("</body></html>");

        //Convertir HTML a PDF
        String titulo = "Reporte de Reservas Global";
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
        Map<String, Integer> conteoReservasPorLocal = new HashMap<>();

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
        htmlBuilder.append("<tr><th>IDLocal</th><th>Nombre</th><th>Servicios</th><th>N° Reservas</th></tr>");

        for (Local local : locales) {
            Servicio servicio = local.getServicio();
            boolean filtros = Objects.equals(filtro.getIdServicio(), servicio.getIdServicio());
            if (filtro.getIdServicio() == null) filtros = true;

            if (servicio.getFechaCreacion().isAfter(filtro.getFechaInicio().atStartOfDay()) &&
                    servicio.getFechaCreacion().isBefore(filtro.getFechaFin().atStartOfDay()) && filtros) {

                // Contar reservas sumando reservas de todas las clases del local
                int totalReservas = 0;
                if (local.getClases() != null) {
                    for (Clase clase : local.getClases()) {
                        if (clase.getReservas() != null) {
                            totalReservas += clase.getReservas().size();
                        }
                    }
                }

                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(local.getIdLocal()).append("</td>");
                htmlBuilder.append("<td>").append(local.getNombre()).append("</td>");
                htmlBuilder.append("<td>").append(servicio.getNombre()).append("</td>");
                htmlBuilder.append("<td>").append(totalReservas).append("</td>");
                htmlBuilder.append("</tr>");

                conteoServicios.merge(servicio.getNombre(), 1, Integer::sum);
                conteoReservasPorLocal.put(local.getNombre(), totalReservas);
            }
        }

        htmlBuilder.append("</table>");

        // 👇 Gráfico: Distribución de locales por servicio
        DefaultCategoryDataset datasetServicios = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : conteoServicios.entrySet()) {
            datasetServicios.addValue(entry.getValue(), "Servicios", entry.getKey());
        }

        String base64ImageServicios = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JFreeChart chart = ChartFactory.createBarChart(
                    "Distribución de locales por servicio",
                    "Servicio",
                    "Cantidad",
                    datasetServicios,
                    PlotOrientation.VERTICAL,
                    false, true, false
            );
            ChartUtils.writeChartAsPNG(baos, chart, 600, 400);
            byte[] bytes = baos.toByteArray();
            base64ImageServicios = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!base64ImageServicios.isEmpty()) {
            htmlBuilder.append("<h3>Distribución de locales por servicio</h3>");
            htmlBuilder.append("<img src='data:image/png;base64,")
                    .append(base64ImageServicios)
                    .append("' width='600'/>");
        }

        // 👇 Gráfico: Distribución de reservas por local
        DefaultCategoryDataset datasetReservas = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : conteoReservasPorLocal.entrySet()) {
            datasetReservas.addValue(entry.getValue(), "Reservas", entry.getKey());
        }

        String base64ImageReservas = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JFreeChart chart = ChartFactory.createBarChart(
                    "Reservas por local",
                    "Local",
                    "N° Reservas",
                    datasetReservas,
                    PlotOrientation.VERTICAL,
                    false, true, false
            );
            ChartUtils.writeChartAsPNG(baos, chart, 600, 400);
            byte[] bytes = baos.toByteArray();
            base64ImageReservas = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!base64ImageReservas.isEmpty()) {
            htmlBuilder.append("<h3>Cantidad de reservas por local</h3>");
            htmlBuilder.append("<img src='data:image/png;base64,")
                    .append(base64ImageReservas)
                    .append("' width='600'/>");
        }

        htmlBuilder.append("</body></html>");

        // 👉 Convertir HTML a PDF
        String titulo = "Reporte de Locales con Servicios y Reservas";
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
        htmlBuilder.append("<p><strong>ID AFILIACIÓN:</strong> ").append(pago.getAfiliacion().getIdAfiliacion()).append("</p>");
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
    @Override
    public String generarHTMLVistaPreviaUsuarios(ReporteUsuarioRequestDTO filtro) {
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
                    usuario.getFechaCreacion().isBefore(filtro.getFechaFin().plusDays(1).atStartOfDay())) {

                List<Comunidad> comunidades = usuario.getComunidades();
                List<String> combinaciones = new ArrayList<>();

                for (Comunidad comunidad : comunidades) {
                    String nombreComunidad = comunidad.getNombre();

                    for (Afiliacion af : usuario.getAfiliaciones()) {
                        if (Objects.equals(af.getMembresia().getComunidad().getIdComunidad(), comunidad.getIdComunidad())) {
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

        // Gráfico pastel en base64
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : conteoComunidadMembresia.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de usuarios por comunidad y membresía",
                dataset, true, true, false
        );

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

        htmlBuilder.append("<h3>Resumen de usuarios por comunidad y membresía</h3>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>Comunidad - Membresía</th><th>Total Usuarios</th></tr>");

        for (Map.Entry<String, Integer> entry : conteoComunidadMembresia.entrySet()) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(entry.getKey()).append("</td>");
            htmlBuilder.append("<td>").append(entry.getValue()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

}