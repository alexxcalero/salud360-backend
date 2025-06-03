package pe.edu.pucp.salud360.control.utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReportePDFGenerator {

    public static byte[] generarReporteHTML(String titulo, String contenido) {
        String html = "<html>" +
                "<head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                "h1 { color: #2E86C1; }" +
                "table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                "th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }" +
                "th { background-color: #f2f2f2; }" +
                "</style></head>" +
                "<body>" +
                "<h1>" + titulo + "</h1>" +
                "<p>" + contenido + "</p>" +
                "<table>" +
                "<tr><th>Campo</th><th>Valor</th></tr>" +
                "<tr><td>Ejemplo 1</td><td>Valor 1</td></tr>" +
                "<tr><td>Ejemplo 2</td><td>Valor 2</td></tr>" +
                "</table>" +
                "</body></html>";

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(baos);
            builder.run();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
