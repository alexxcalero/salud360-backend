package pe.edu.pucp.salud360.membresia.models;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.pucp.salud360.control.models.Reporte;
import pe.edu.pucp.salud360.usuario.models.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "afiliacion")
public class Afiliacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAfiliacion", unique = true, nullable = false, updatable = false)
    private Integer idAfiliacion;

    @Column(name = "estado", unique = false, nullable = false, updatable = true)
    private String estado;  // Activado, Cancelado, Suspendido

    @Column(name = "fechaAfiliacion", unique = false, nullable = false, updatable = true)
    private LocalDateTime fechaAfiliacion;

    @Column(name = "fechaDesafiliacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesafiliacion;

    @Column(name = "fechaSuspension", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaSuspension;

    @Column(name = "fechaReactivacion", unique = false, nullable = true, updatable = true)
    private LocalDate fechaReactivacion;

    @ManyToOne
    @JoinColumn(name = "idMembresia")
    private Membresia membresia;

    @ManyToMany(mappedBy = "afiliacion")
    private List<Reporte> reportes;

    @OneToMany(mappedBy = "afiliacion")
    private List<Pago> pagos;

    @ManyToOne
    @JoinColumn(name = "idMedioDePago")
    private MedioDePago medioDePago;

    @OneToMany(mappedBy = "afiliacion")
    private List<Periodo> periodo;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;
}
