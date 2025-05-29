package pe.edu.pucp.salud360.control.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.pucp.salud360.control.models.Auditoria;
import pe.edu.pucp.salud360.control.repositories.AuditoriaRepository;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    private final ObjectMapper objectMapper;

    public AuditoriaAspect() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterReturning(
            pointcut = "execution(* pe.edu.pucp.salud360.servicio.services.servicesImp.LocalServiceImp.crearLocal(..))",
            returning = "resultado")
    public void auditarCreacion(JoinPoint joinPoint, Object resultado) {
        System.out.println("✅ Aspect CREAR interceptado para Local");
        guardarAuditoria("CREAR", "Local", resultado);
    }

    @AfterReturning(
            pointcut = "execution(* pe.edu.pucp.salud360.servicio.services.servicesImp.LocalServiceImp.actualizarLocal(..))",
            returning = "resultado")
    public void auditarActualizacion(JoinPoint joinPoint, Object resultado) {
        guardarAuditoria("ACTUALIZAR", "Local", resultado);
    }

    @Before(
            value = "execution(* pe.edu.pucp.salud360.servicio.services.servicesImp.LocalServiceImp.eliminarLocal(..)) && args(id)")
    public void auditarEliminacion(JoinPoint joinPoint, Integer id) {
        guardarAuditoria("ELIMINAR", "Local", "ID=" + id);
    }

    private void guardarAuditoria(String operacion, String tabla, Object datos) {
        try {
            System.out.println("🟢 Entrando a auditoría: " + operacion);
            Auditoria auditoria = new Auditoria();
            auditoria.setNombreTabla(tabla);
            auditoria.setOperacion(operacion);
            auditoria.setFechaModificacion(LocalDateTime.now());
            auditoria.setIdUsuarioModificador(1); // TODO: obtener de seguridad si la tienes
            auditoria.setDescripcion(objectMapper.writeValueAsString(datos));

            auditoriaRepository.save(auditoria);
        } catch (Exception e) {
            System.err.println("Error al guardar auditoría: " + e.getMessage());
        }
    }


}

