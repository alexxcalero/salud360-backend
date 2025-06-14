package pe.edu.pucp.salud360.control.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.pucp.salud360.autenticacion.models.UsuarioDetails;
import pe.edu.pucp.salud360.control.models.Auditoria;
import pe.edu.pucp.salud360.control.repositories.AuditoriaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import pe.edu.pucp.salud360.usuario.models.Usuario;


import java.time.LocalDateTime;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @AfterReturning(pointcut = "execution(* pe.edu.pucp.salud360..*.crear*(..))", returning = "resultado")
    public void auditarCreacion(JoinPoint joinPoint, Object resultado) {
        guardarAuditoria("CREAR", joinPoint, resultado);
    }

    @AfterReturning(pointcut = "execution(* pe.edu.pucp.salud360..*.actualizar*(..))", returning = "resultado")
    public void auditarActualizacion(JoinPoint joinPoint, Object resultado) {
        guardarAuditoria("ACTUALIZAR", joinPoint, resultado);
    }

    @Before(value = "execution(* pe.edu.pucp.salud360..*.eliminar*(..)) && args(id)")
    public void auditarEliminacion(JoinPoint joinPoint, Object id) {
        guardarAuditoria("ELIMINAR", joinPoint, "ID=" + id);
    }

    @Before("execution(* pe.edu.pucp.salud360..*.reactivar*(..)) && args(id)")
    public void auditarReactivacionConId(JoinPoint joinPoint, Object id) {
        guardarAuditoria("REACTIVAR", joinPoint, "ID=" + id);
    }


    private void guardarAuditoria(String operacion, JoinPoint joinPoint, Object datos) {
        try {
            String clase = joinPoint.getTarget().getClass().getSimpleName().replace("ServiceImp", "");
            String descripcion = generarDescripcion(datos);

            Auditoria auditoria = new Auditoria();
            auditoria.setNombreTabla(clase);
            auditoria.setOperacion(operacion);
            auditoria.setFechaModificacion(LocalDateTime.now());

            auditoria.setNombreUsuarioModificador(obtenerNombreUsuarioActual());
            // Simulado. Puedes conectarlo a Spring Security.
            auditoria.setDescripcion(operacion + " - " + descripcion);

            auditoriaRepository.save(auditoria);
            System.out.println("✅ Auditoría generada: " + auditoria.getDescripcion());
        } catch (Exception e) {
            System.err.println("❌ Error en auditoría: " + e.getMessage());
        }
    }

    private String obtenerNombreUsuarioActual() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UsuarioDetails usuarioDetails) {
                System.out.println("✅ Usuario autenticado: " + usuarioDetails.getUsuario().getCorreo());
                return usuarioDetails.getUsuario().getCorreo(); // ✅ tu caso actual
            } else if (principal instanceof Usuario usuario) {
                System.out.println("⚠️ No es UsuarioDetails, es: " + principal);
                return usuario.getCorreo(); // ⛑️ fallback si usaste Usuario directamente
            } else {
                System.out.println("⚠️ No es UsuarioDetails, es: " + principal);
                return "anónimo"; // 🤷‍♂️ por si es otra cosa
            }
        } catch (Exception e) {
            System.err.println("❌ Error al obtener usuario autenticado: " + e.getMessage());
            return "anónimo";
        }
    }





    private String generarDescripcion(Object datos) {
        if (datos == null) return "Datos nulos";
        try {
            var idField = datos.getClass().getDeclaredField("id" + datos.getClass().getSimpleName().replace("DTO", ""));
            idField.setAccessible(true);
            Object idValue = idField.get(datos);
            return "Clase: " + datos.getClass().getSimpleName() + ", ID: " + idValue;
        } catch (Exception e) {
            return "Clase: " + datos.getClass().getSimpleName();
        }
    }
}

