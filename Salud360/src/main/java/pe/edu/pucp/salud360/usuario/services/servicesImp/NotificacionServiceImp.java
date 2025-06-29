package pe.edu.pucp.salud360.usuario.services.servicesImp;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import pe.edu.pucp.salud360.usuario.dtos.NotificacionDTO;
import pe.edu.pucp.salud360.usuario.mappers.NotificacionMapper;
import pe.edu.pucp.salud360.usuario.models.Notificacion;
import pe.edu.pucp.salud360.usuario.repositories.NotificacionRepository;
import pe.edu.pucp.salud360.usuario.services.NotificacionService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImp implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;
    private final TemplateEngine templateEngine;

    @Value("${spring.sendgrid.api-key}")//${spring.sendgrid.api-key}

    private String sendGridApiKey;

    @Override
    public NotificacionDTO crearNotificacion(NotificacionDTO notificacionDTO, String asunto) {
        Notificacion notificacion = notificacionMapper.mapToModel(notificacionDTO);
        Notificacion notificacionCreada = notificacionRepository.save(notificacion);
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("nombre", notificacionDTO.getCliente().getNombres());
        variables.put("mensaje", notificacionDTO.getMensaje());
        context.setVariables(variables);
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        String contenidoHtml = templateEngine.process("mensaje", context);
        Email from = new Email("no-replay-salud360@deportesanmiguelpucp.online", "Salud 360");
        Email to = new Email(notificacionDTO.getCliente().getCorreo());
        Content content = new Content("text/html", contenidoHtml);
        Mail mail = new Mail(from, asunto, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Status code: " + response.getStatusCode());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return notificacionMapper.mapToDTO(notificacionCreada);
    }

    @Override
    public NotificacionDTO actualizarNotificacion(Integer idNotificacion, NotificacionDTO notificacionDTO) {
        if(notificacionRepository.findById(idNotificacion).isPresent()){
            Notificacion notificacion = notificacionRepository.findById(idNotificacion).get();
            notificacion.setMensaje(notificacionDTO.getMensaje());
            Notificacion notificacionActualizada = notificacionRepository.save(notificacion);
            return notificacionMapper.mapToDTO(notificacionActualizada);
        } else {
            return null;
        }
    }

    @Override
    public void eliminarNotificacion(Integer idNotificacion) {
        if(notificacionRepository.findById(idNotificacion).isPresent()) {
            notificacionRepository.deleteById(idNotificacion);
        }
    }

    @Override
    public List<NotificacionDTO> listarNotificacionesTodas() {
        List<Notificacion> notificaciones = notificacionRepository.findAll();
        if(notificaciones.isEmpty()) {
            return null;
        } else {
            return notificaciones.stream().map(notificacionMapper::mapToDTO).toList();
        }
    }

    @Override
    public NotificacionDTO buscarNotificacionPorId(Integer idNotificacion) {
        if(notificacionRepository.findById(idNotificacion).isPresent()) {
            Notificacion notificacion = notificacionRepository.findById(idNotificacion).get();
            return notificacionMapper.mapToDTO(notificacion);
        } else {
            return null;
        }
    }
}
