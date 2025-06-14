package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.pucp.salud360.usuario.models.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clase")
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClase", unique = true, nullable = false, updatable = false)
    private Integer idClase;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "horaInicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horaFin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "cantAsistentes", nullable = false)
    private Integer cantAsistentes;

    @Column(name = "estado", nullable = false)
    private String estado;  // Disponible - Completa (cuando se llena el aforo) - Finalizada

    @Column(name = "activo", nullable = false)
    private Boolean activo;  // Esto va a servir para que el administrador pueda desactivar o activar citas (true o false)

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @ManyToMany
    @JoinTable(
            name = "clase_cliente",
            joinColumns = @JoinColumn(name = "idClase"),
            inverseJoinColumns = @JoinColumn(name = "idCliente")
    )
    private List<Cliente> clientes;

    @OneToMany(mappedBy = "clase")
    private List<Reserva> reservas;

    @ManyToOne
    @JoinColumn(name = "idLocal")
    private Local local;
}
