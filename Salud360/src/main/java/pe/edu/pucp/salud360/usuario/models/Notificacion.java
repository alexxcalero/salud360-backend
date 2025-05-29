package pe.edu.pucp.salud360.usuario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.pucp.salud360.servicio.models.Reserva;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotificacion", unique = true, nullable = false, updatable = false)
    private Integer idNotificacion;

    @Column(name = "mensaje", unique = false, nullable = false, updatable = false)
    private String mensaje;

    @Column(name = "fechaEnvio", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idReserva")
    private Reserva reserva;
}
