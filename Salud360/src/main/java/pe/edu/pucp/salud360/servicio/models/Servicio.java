package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "servicio")
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idServicio", unique = true, nullable = false, updatable = false)
    private Integer idServicio;

    @Column(name = "nombre", unique = false, nullable = false, updatable = true)
    private String nombre;

    @Column(name = "descripcion", unique = false, nullable = false, updatable = true)
    private String descripcion;

    //Vamos a mapear ahora una unicaImagen
    @Column(name = "urlImagen")
    private String imagen;

    @Column(name = "tipo", unique = false, nullable = false, updatable = true)
    private String tipo;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @ManyToMany
    private List<Comunidad> comunidad;

    @OneToMany(mappedBy = "servicio", fetch = FetchType.EAGER)
    private List<CitaMedica> citasMedicas;

    @OneToMany(mappedBy = "servicio")
    private List<Local> locales;
}
