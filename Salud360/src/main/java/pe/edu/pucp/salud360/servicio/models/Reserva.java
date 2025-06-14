package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.usuario.models.Cliente;
import pe.edu.pucp.salud360.usuario.models.Notificacion;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReserva", unique = true, nullable = false, updatable = false)
    private Integer idReserva;

    @Column(name = "estado", unique = false, nullable = false, updatable = true)
    private String estado;  // Confirmada (ni bien se cree la reserva), Cancelada (si es que el cliente cancela la reserva)

    @Column(name = "fechaReserva", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaReserva;  // Se asigna automaticamente al crear la reserva

    @Column(name = "fechaCancelacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaCancelacion;  // Se asigna automaticamente al cancelar la reserva

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idClase")
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "idCitaMedica")
    private CitaMedica citaMedica;

    @OneToMany(mappedBy = "reserva")
    private List<Notificacion> notificaciones;

    @ManyToOne
    @JoinColumn(name = "idComunidad")
    private Comunidad comunidad;
}
