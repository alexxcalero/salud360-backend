package pe.edu.pucp.salud360.membresia.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "periodo")
public class Periodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPeriodo;

    @Column(name = "fechaInicio", unique = false, nullable = false, updatable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaFin", unique = false, nullable = false, updatable = true)
    private LocalDate fechaFin;

    @Column(name = "haSidoSuspendida", unique = false, nullable = false, updatable = true)
    private Boolean haSidoSuspendida;

    @Column(name = "cantReservas", unique = false, nullable = false, updatable = true)
    private Integer cantReservas;

    @ManyToOne
    @JoinColumn(name = "afiliacion")
    private Afiliacion afiliacion;
}
