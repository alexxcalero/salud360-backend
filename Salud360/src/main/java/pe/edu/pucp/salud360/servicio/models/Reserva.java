package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.models.Testimonio;
import pe.edu.pucp.salud360.usuario.models.Notificacion;
import pe.edu.pucp.salud360.usuario.models.Persona;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @Column(name = "horaNotificacion", unique = false, nullable = false, updatable = true)
    private LocalDateTime horaNotificacion;

    @Column(name = "estado", unique = false, nullable = false, updatable = true)
    private String estado;

    @Column(name = "fechaReserva", unique = false, nullable = false, updatable = true)
    private LocalDateTime fechaReserva;

    @Column(name = "fechaCancelacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaCancelacion;

    @Column(name = "fechaReprogramacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaReprogramacion;

    @Column(name = "horaInicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horaFin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "idClase")
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "idCitaMedica")
    private CitaMedica citaMedica;

    @OneToMany(mappedBy = "reserva")
    private List<Notificacion> notificaciones;
}
