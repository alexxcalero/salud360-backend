package pe.edu.pucp.salud360.membresia.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.pucp.salud360.usuario.models.Cliente;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mediodepago")
public class MedioDePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedioDePago;

    @Column(name = "tipo", unique = false, nullable = false, updatable = true)
    private String tipo;

    @Column(name = "ncuenta", unique = false, nullable = false, updatable = true)
    private String ncuenta;  // Cambiado de Integer/Long a String

    @Column(name = "vencimiento", unique = false, nullable = false, updatable = true)
    private LocalDateTime vencimiento;

    @Column(name = "cvv", unique = false, nullable = false, updatable = true)
    private Integer cvv;

    @Column(name = "totalGastado", unique = false, nullable = true, updatable = true)
    private Double totalGastado;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @OneToMany(mappedBy = "medioDePago")
    private List<Afiliacion> afiliaciones;

    @OneToMany(mappedBy = "medioDePago")
    private List<Pago> pagos;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

}
