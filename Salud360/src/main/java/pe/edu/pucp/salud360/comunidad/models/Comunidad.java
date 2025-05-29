package pe.edu.pucp.salud360.comunidad.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.membresia.models.Membresia;
import pe.edu.pucp.salud360.servicio.models.Servicio;
import pe.edu.pucp.salud360.usuario.models.Cliente;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comunidad")
public class Comunidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComunidad", unique = true, nullable = false, updatable = false)
    private Integer idComunidad;

    @Column(name = "nombre", unique = false, nullable = false, updatable = true)
    private String nombre;

    @Column(name = "descripcion", unique = false, nullable = false, updatable = true)
    private String descripcion;

    @Column(name = "proposito", unique = false, nullable = false, updatable = true)
    private String proposito;

    @ElementCollection
    @CollectionTable(name = "imagenesDeComunidad", joinColumns = @JoinColumn(name = "idComunidad"))
    @Column(name = "urlImagen", unique = false, nullable = true, updatable = true)
    private List<String> imagenes;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @Column(name = "cantMiembros", nullable = true)
    private Integer cantMiembros;

    @Column(name = "calificacion", nullable = true)
    private Double calificacion;

    @OneToMany(mappedBy = "comunidad")
    private List<Membresia> membresias;

    @OneToMany(mappedBy = "comunidad")
    private List<Testimonio> testimonios;

    @ManyToMany
    @JoinTable(
            name = "comunidad_servicio",
            joinColumns = @JoinColumn(name = "idComunidad"),
            inverseJoinColumns = @JoinColumn(name = "idServicio")
    )
    private List<Servicio> servicios;

    @ManyToMany
    @JoinTable(
            name = "comunidad_cliente",
            joinColumns = @JoinColumn(name = "idComunidad"),
            inverseJoinColumns = @JoinColumn(name = "idCliente")
    )
    private List<Cliente> clientes;
}
