package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.usuario.models.Persona;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clase")
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String estado;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion")
    private LocalDateTime fechaDesactivacion;



    @ManyToMany
    @JoinTable(
            name = "clase_persona",
            joinColumns = @JoinColumn(name = "idClase"),
            inverseJoinColumns = @JoinColumn(name = "idUsuario")
    )
    private List<Persona> personas;

    @OneToMany(mappedBy = "clase")
    private List<Reserva> reservas;

    @ManyToOne
    @JoinColumn(name = "idLocal")
    private Local local;
}
