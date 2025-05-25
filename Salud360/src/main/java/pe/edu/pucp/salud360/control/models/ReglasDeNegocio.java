package pe.edu.pucp.salud360.control.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reglasDeNegocio")
public class ReglasDeNegocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRegla;

    @Column(name = "maxReservas", unique = false, nullable = false, updatable = true)
    private Integer maxReservas;

    @Column(name = "maxCapacidad", unique = false, nullable = false, updatable = true)
    private Integer maxCapacidad;

    @Column(name = "maxDiasSuspension", unique = false, nullable = false, updatable = true)
    private Integer maxDiasSuspension;

    @Column(name = "maxTiempoCancelacion", unique = false, nullable = false, updatable = true)
    private Integer maxTiempoCancelacion;
}
