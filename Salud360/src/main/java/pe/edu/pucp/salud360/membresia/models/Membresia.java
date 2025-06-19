package pe.edu.pucp.salud360.membresia.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import lombok.Builder; // <-- IMPORTANTE
import java.time.LocalDateTime;
import java.util.List;

@Builder // <-- agrega esta línea
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "membresia")
@Inheritance(strategy = InheritanceType.JOINED)
public class Membresia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMembresia;

    @Column(name = "nombre", unique = false, nullable = false, updatable = true)
    private String nombre;

    @Column(name = "descripcion", unique = false, nullable = false, updatable = true)
    private String descripcion;

    @Column(name = "tipo", unique = false, nullable = false, updatable = true)
    private String tipo;

    @Column(name = "conTope", unique = false, nullable = false, updatable = true)
    private Boolean conTope;

    @Column(name = "precio", unique = false, nullable = false, updatable = true)
    private Double precio;

    @Column(name = "cantUsuarios", unique = false, nullable = false, updatable = true)
    private Integer cantUsuarios;

    @Column(name = "maxReservas", unique = false, nullable = false, updatable = true)
    private Integer maxReservas;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @OneToMany(mappedBy = "membresia")
    private List<Afiliacion> afiliacion;

    @ManyToOne
    @JoinColumn(name = "idComunidad")
    private Comunidad comunidad;
}
