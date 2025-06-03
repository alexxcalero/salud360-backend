package pe.edu.pucp.salud360.control.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Auditoria")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAuditoria;

    @Column(name = "nombreTabla", unique = false, nullable = false, updatable = false)
    private String nombreTabla;

    @Column(name = "fechaModificacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaModificacion;

    @Column(name = "nombreUsuarioModificador", unique = false, nullable = false, updatable = false)
    private String nombreUsuarioModificador;

    @Column(name = "descripcion", unique = false, nullable = false, updatable = false)
    private String descripcion;

    @Column(name = "operacion", nullable = false, updatable = false)
    private String operacion;
}
