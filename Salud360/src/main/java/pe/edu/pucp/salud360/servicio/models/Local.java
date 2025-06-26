package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "local")
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLocal", unique = true, nullable = false, updatable = false)
    private Integer idLocal;

    @Column(name = "nombre", unique = false, nullable = false, updatable = true)
    private String nombre;

    @Column(name = "descripcion", unique = false, nullable = false, updatable = true)
    private String descripcion;

    @Column(name = "direccion", unique = false, nullable = false, updatable = true)
    private String direccion;

    @Column(name = "telefono", unique = false, nullable = false, updatable = true)
    private String telefono;

    //Vamos a mapear ahora una unicaImagen
    @Column(name = "urlImagen")
    private String imagen;

    @Column(name = "tipoServicio", unique = false, nullable = false, updatable = true)
    private String tipoServicio;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @ManyToOne
    @JoinColumn(name = "idServicio")
    private Servicio servicio;

    @OneToMany(mappedBy = "local")
    private List<Clase> clases;
}
