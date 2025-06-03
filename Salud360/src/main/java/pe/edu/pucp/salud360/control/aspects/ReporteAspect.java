package pe.edu.pucp.salud360.control.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.pucp.salud360.control.models.Auditoria;
import pe.edu.pucp.salud360.control.repositories.AuditoriaRepository;

import java.time.LocalDateTime;

@Aspect
@Component
public class ReporteAspect {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    // Punto de corte para métodos que generan reportes
    @AfterReturning(pointcut = "execution(* pe.edu.pucp.salud360.control.services.ReporteService.*(..))", returning = "resultado")
    public void auditarGeneracionReporte(JoinPoint joinPoint, Object resultado) {
        guardarAuditoria("GENERAR REPORTE", joinPoint, resultado);
    }

    private void guardarAuditoria(String operacion, JoinPoint joinPoint, Object datos) {
        try {
            String clase = joinPoint.getTarget().getClass().getSimpleName().replace("ServiceImpl", "");
            String metodo = joinPoint.getSignature().getName();
            String descripcion = generarDescripcion(datos);

            Auditoria auditoria = new Auditoria();
            auditoria.setNombreTabla("Reporte");
            auditoria.setOperacion(operacion);
            auditoria.setFechaModificacion(LocalDateTime.now());
            auditoria.setIdUsuarioModificador(1); // TODO: conectar con Spring Security más adelante
            auditoria.setDescripcion("Método: " + metodo + " - " + descripcion);

            auditoriaRepository.save(auditoria);
            System.out.println("✅ Auditoría generada (reporte): " + auditoria.getDescripcion());
        } catch (Exception e) {
            System.err.println("❌ Error al generar auditoría de reporte: " + e.getMessage());
        }
    }

    private String generarDescripcion(Object datos) {
        if (datos == null) return "Sin datos de retorno";
        try {
            var idField = datos.getClass().getDeclaredField("idReporte");
            idField.setAccessible(true);
            Object idValue = idField.get(datos);
            return "Reporte ID: " + idValue;
        } catch (Exception e) {
            return "Clase de retorno: " + datos.getClass().getSimpleName();
        }
    }
}
